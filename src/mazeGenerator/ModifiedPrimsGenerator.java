package mazeGenerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import maze.Cell;
import maze.Maze;

public class ModifiedPrimsGenerator implements MazeGenerator {

	private int startR;
	private int startC;
	
	private Random rand = new Random();
	
	private static final int IN       = 0;
	private static final int FRONTIER = 1;
	private static final int OUT       = 2;
	
	int[][] cell;
	ArrayList<Cell> frontier = new ArrayList<Cell>();
	int[] inNeighbours = new int[6];
	
	@Override
	public void generateMaze(Maze maze) {
		processGenerateMaze(maze);
	} // end of generateMaze()
	
	private void processGenerateMaze(Maze maze){
		cell = new int[maze.sizeR][maze.sizeC];
		
		for(int i = 0; i < maze.sizeR; i++){
			Arrays.fill(cell[i], OUT);
		}
		
		startR = rand.nextInt(maze.sizeR);
		startC = rand.nextInt(maze.sizeC);
		
		cell[startR][startC] = IN;
		
		addFrontierCell(maze, startR, startC);

		while(!frontier.isEmpty()){
			Cell currCell = frontier.remove(rand.nextInt(frontier.size()));
			cell[currCell.r][currCell.c] = IN;
			
			int inNeighbourCount = 0;
			
			inNeighbourCount = inNeighbourCell(maze, inNeighbourCount, currCell);
			
			if(inNeighbourCount > 0){
				unblockNeighbourCell(maze, currCell, inNeighbourCount);
			}
			
			addFrontierCell(maze, currCell.r, currCell.c);
			
			if(maze.type == maze.TUNNEL){
				blockTunnelCell(maze, currCell);
			}
		}
		

	}
	
	private void addFrontierCell(Maze maze, int row, int col)
	{
		if(maze.type == maze.NORMAL || maze.type == maze.TUNNEL){
			for (int i = 0; i < maze.NUM_DIR; i++) {
				switch (i) {
					case 0: //EAST
						if (col < maze.sizeC - 1 && cell[row][col + 1] == OUT)  {
							cell[row][col+1] = FRONTIER;
							frontier.add(new Cell(row, col+1));
						}
						break;
					case 2: //NORTH
						if (row < maze.sizeR - 1 && cell[row + 1][col] == OUT) {
							cell[row + 1][col] = FRONTIER;
							frontier.add(new Cell(row+1, col));
						}
						break;
					case 3: //WEST
						if (col > 0 && cell[row][col-1] == OUT) {
							cell[row][col- 1] = FRONTIER;
							frontier.add(new Cell(row, col-1));
						}
						break;
					case 5: //SOUTH
						if (row > 0 && cell[row-1][col] == OUT) {
							cell[row-1][col] = FRONTIER;
							frontier.add(new Cell(row-1, col));
						}
						break;
				}
			}
		}else if(maze.type == maze.HEX){
			for (int i = 0; i < maze.NUM_DIR; i++) {
				switch (i) {
					case 0: //EAST
						if (col < maze.sizeC - 1 && cell[row][col+1] == OUT)  {
							cell[row][col+1] = FRONTIER;
							frontier.add(new Cell(row, col+1));
						}
						break;
					case 1: //NORTHEAST
						if(row % 2 == 0){
							if(row <  maze.sizeR -1 && col <= maze.sizeC - 1 && cell[row+1][col] == OUT){
								cell[row+1][col] = FRONTIER;
								frontier.add(new Cell(row+1, col));
							}
						}else{
							if(row <  maze.sizeR -1 && col < maze.sizeC - 1 && cell[row+1][col+1] == OUT){
								cell[row+1][col+1] = FRONTIER;
								frontier.add(new Cell(row+1, col+1));
							}
						}
						break;
					case 2: //NORTHWEST
						if(row % 2 == 0){
							if(row <  maze.sizeR -1 && col > 0 && cell[row+1][col-1] == OUT){
								cell[row+1][col-1] = FRONTIER;
								frontier.add(new Cell(row+1, col-1));
							}
						}else{
							if(row <  maze.sizeR -1 && col >= 0 && cell[row+1][col] == OUT){
								cell[row+1][col] = FRONTIER;
								frontier.add(new Cell(row+1, col));
							}
						}
						break;
					case 3: //WEST
						if (col > 0 && cell[row][col -1] == OUT) {
							cell[row][col - 1] = FRONTIER;
							frontier.add(new Cell(row, col-1));
						}
						break;
					case 4: //SOUTHWEST
						if(row % 2 == 0){
							if(row > 0 && col > 0 && cell[row-1][col-1] == OUT){
								cell[row-1][col-1] = FRONTIER;
								frontier.add(new Cell(row-1, col-1));
							}
						}else{
							if(row > 0 && col >= 0 && cell[row-1][col] == OUT){
								cell[row-1][col] = FRONTIER;
								frontier.add(new Cell(row-1, col));
							}
						}
						break;
					case 5: //SOUTHEAST
						if(row % 2 == 0){
							if(row > 0 && col <= maze.sizeC - 1 && cell[row-1][col] == OUT){
								cell[row-1][col] = FRONTIER;
								frontier.add(new Cell(row-1, col));
							}
						}else{
							if(row > 0 && col < maze.sizeC - 1 && cell[row-1][col+1] == OUT){
								cell[row-1][col+1] = FRONTIER;
								frontier.add(new Cell(row-1, col+1));
							}
						}
						break;
						
				}
			}
		}
		
	}
	
	private int inNeighbourCell(Maze maze, int inNeighbourCount, Cell currCell){
		if(maze.type == maze.NORMAL){
			for (int i = 0; i < maze.NUM_DIR; i++) {
				switch (i) {
					case 0: //EAST
						if (currCell.c < maze.sizeC - 1 && cell[currCell.r][currCell.c + 1] == IN) {
							inNeighbours[inNeighbourCount++] = i;
						}
						break;
					case 2: //NORTH
						if (currCell.r < maze.sizeR - 1 && cell[currCell.r + 1][currCell.c] == IN) {
							inNeighbours[inNeighbourCount++] = i;
						}
						break;
					case 3: //WEST
						if (currCell.c > 0 && cell[currCell.r][currCell.c -1] == IN) {
							inNeighbours[inNeighbourCount++] = i;
						}
						break;
					case 5: //SOUTH
						if (currCell.r > 0 && cell[currCell.r - 1][currCell.c] == IN) {
							inNeighbours[inNeighbourCount++] = i;
						}
						break;
				}
			}
		}else if(maze.type == maze.TUNNEL){
			for (int i = 0; i < maze.NUM_DIR; i++) {
				switch (i) {
					case 0: //EAST
						if (currCell.c < maze.sizeC - 1 && cell[currCell.r][currCell.c + 1] == IN) {
							if(!isNeightbourCellTunnel(maze, currCell, i))
								inNeighbours[inNeighbourCount++] = i;
						}
						break;
					case 2: //NORTH
						if (currCell.r < maze.sizeR - 1 && cell[currCell.r + 1][currCell.c] == IN) {
							if(!isNeightbourCellTunnel(maze, currCell, i))
								inNeighbours[inNeighbourCount++] = i;
						}
						break;
					case 3: //WEST
						if (currCell.c > 0 && cell[currCell.r][currCell.c -1] == IN) {
							if(!isNeightbourCellTunnel(maze, currCell, i))
								inNeighbours[inNeighbourCount++] = i;
						}
						break;
					case 5: //SOUTH
						if (currCell.r > 0 && cell[currCell.r - 1][currCell.c] == IN) {
							if(!isNeightbourCellTunnel(maze, currCell, i))
								inNeighbours[inNeighbourCount++] = i;
						}
						break;
				}
			}
		}else if(maze.type == maze.HEX){
			for (int i = 0; i < maze.NUM_DIR; i++) {
				switch (i) {
					case 0: //EAST
						if (currCell.c < maze.sizeC - 1 && cell[currCell.r][currCell.c + 1] == IN) {
							inNeighbours[inNeighbourCount++] = i;
						}
						break;
					case 1: //NORTHEAST
						if(currCell.r % 2 == 0){
							if(currCell.r <  maze.sizeR -1 && currCell.c <= maze.sizeC - 1 && cell[currCell.r+1][currCell.c] == IN){
								inNeighbours[inNeighbourCount++] = i;
							}
						}else{
							if(currCell.r <  maze.sizeR -1 && currCell.c < maze.sizeC - 1 && cell[currCell.r+1][currCell.c+1] == IN){
								inNeighbours[inNeighbourCount++] = i;
							}
						}
						break;
					case 2: //NORTHWEST
						if(currCell.r % 2 == 0){
							if(currCell.r <  maze.sizeR -1 && currCell.c > 0 && cell[currCell.r+1][currCell.c-1] == IN){
								inNeighbours[inNeighbourCount++] = i;
							}
						}else{
							if(currCell.r <  maze.sizeR -1 && currCell.c >= 0 && cell[currCell.r+1][currCell.c] == IN){
								inNeighbours[inNeighbourCount++] = i;
							}
						}
						break;
					case 3: //WEST
						if (currCell.c > 0 && cell[currCell.r][currCell.c-1] == IN) {
							inNeighbours[inNeighbourCount++] = i;
						}
						break;
					case 4: //SOUTHWEST
						if(currCell.r % 2 == 0){
							if(currCell.r > 0 && currCell.c > 0 && cell[currCell.r-1][currCell.c-1] == IN){
								inNeighbours[inNeighbourCount++] = i;
							}
						}else{
							if(currCell.r > 0 && currCell.c >= 0 && cell[currCell.r-1][currCell.c] == IN){
								inNeighbours[inNeighbourCount++] = i;
							}
						}
						break;
					case 5: //SOUTHEAST
						if(currCell.r % 2 == 0){
							if(currCell.r > 0 && currCell.c <= maze.sizeC - 1 && cell[currCell.r-1][currCell.c] == IN){
								inNeighbours[inNeighbourCount++] = i;
							}
						}else{
							if(currCell.r > 0 && currCell.c < maze.sizeC - 1 && cell[currCell.r-1][currCell.c+1] == IN){
								inNeighbours[inNeighbourCount++] = i;
							}
						}
						break;	
				}
			}
		}
		return inNeighbourCount;
	}
	
	private void unblockNeighbourCell(Maze maze, Cell currCell, int direction){
		if(maze.type == maze.NORMAL || maze.type == maze.TUNNEL)
			maze.map[currCell.r][currCell.c].wall[inNeighbours[rand.nextInt(direction)]].present = false;
		else if(maze.type == maze.HEX)
			maze.map[currCell.r][currCell.c + (currCell.r + 1) / 2].wall[inNeighbours[rand.nextInt(direction)]].present = false;
	}

	private boolean isNeightbourCellTunnel(Maze maze, Cell currCell, int direction){
		if(currCell.tunnelTo != null && currCell.tunnelTo.r == currCell.r + maze.deltaR[direction] && currCell.tunnelTo.c == currCell.c + maze.deltaC[direction]){
			return true;
		}
		return false;
	}
	
	private void blockTunnelCell(Maze maze, Cell currCell){
		if(maze.map[currCell.r][currCell.c].tunnelTo != null){
			Cell tunnel = maze.map[currCell.r][currCell.c].tunnelTo;
			cell[tunnel.r][tunnel.c] = IN;
			
			for(int i = 0; i < frontier.size(); i++){
				if(frontier.get(i).r == tunnel.r && frontier.get(i).c == tunnel.c)
					frontier.remove(i);
			}
		}
	}
} // end of class ModifiedPrimsGenerator
