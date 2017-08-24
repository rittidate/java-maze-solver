package mazeGenerator;

import maze.Cell;
import maze.Maze;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;

public class RecursiveBacktrackerGenerator implements MazeGenerator {
	
	private int startR;
	private int startC;
	
	//private int freeNeighbourCount;
	
	private Random rand = new Random();
	
	boolean[][] visited;
	LinkedList<Cell> stack = new LinkedList<Cell>();
	int[] neighbours = new int[6];
	
	@Override
	public void generateMaze(Maze maze) {
		
		processGenerateMaze(maze);
		
	} // end of generateMaze()
	public void processGenerateMaze(Maze maze){
		visited = new boolean[maze.sizeR][maze.sizeC];
		startR = rand.nextInt(maze.sizeR);
		startC = rand.nextInt(maze.sizeC);

		Cell cell = new Cell(startR, startC);
		stack.addFirst(cell);
		
		visited[cell.r][cell.c] = true;
		
		while(!stack.isEmpty()){
			int freeNeighbourCount = 0;
			freeNeighbourCount = freeNeighbourBlock(maze, freeNeighbourCount, cell);
			
			if (freeNeighbourCount > 0) {
				stack.addFirst(cell);
				cell = new Cell(cell.r, cell.c);
				int direction = neighbours[rand.nextInt(freeNeighbourCount)];
				
				unblockNeighbourCell(maze, cell, direction);
				visited[cell.r][cell.c] = true;
				
				if(maze.type == maze.TUNNEL){
					if(maze.map[cell.r][cell.c].tunnelTo != null){
						Cell tunnel = maze.map[cell.r][cell.c].tunnelTo;
						cell = tunnel;
						visited[tunnel.r][tunnel.c] = true;
					}
				}
			}else{
				cell = stack.removeFirst();
			}
		}
	}
	
	private int freeNeighbourBlock(Maze maze, int freeNeighbourCount, Cell currCell){
		if(maze.type == maze.NORMAL){
			for (int i = 0; i < maze.NUM_DIR; i++) {
				switch (i) {
					case 0: //EAST
						if (currCell.c < maze.sizeC - 1 && !visited[currCell.r + maze.deltaR[i]][currCell.c + maze.deltaC[i]]) {
							neighbours[freeNeighbourCount++] = i;
						}
						break;
					case 2: //NORTH
						if (currCell.r < maze.sizeR - 1 && !visited[currCell.r + maze.deltaR[i]][currCell.c + maze.deltaC[i]]) {
							neighbours[freeNeighbourCount++] = i;
						}
						break;
					case 3: //WEST
						if (currCell.c > 0 && !visited[currCell.r + maze.deltaR[i]][currCell.c + maze.deltaC[i]]) {
							neighbours[freeNeighbourCount++] = i;
						}
						break;
					case 5: //SOUTH
						if (currCell.r > 0 && !visited[currCell.r + maze.deltaR[i]][currCell.c + maze.deltaC[i]]) {
							neighbours[freeNeighbourCount++] = i;
						}
						break;
				}
			}
		}else if(maze.type == maze.TUNNEL){
			for (int i = 0; i < maze.NUM_DIR; i++) {
				switch (i) {
					case 0: //EAST
						if (currCell.c < maze.sizeC - 1 && !visited[currCell.r + maze.deltaR[i]][currCell.c + maze.deltaC[i]]) {
							if(!isNeightbourCellTunnel(maze, currCell, i))
								neighbours[freeNeighbourCount++] = i;
						}
						break;
					case 2: //NORTH
						if (currCell.r < maze.sizeR - 1 && !visited[currCell.r + maze.deltaR[i]][currCell.c + maze.deltaC[i]]) {
							if(!isNeightbourCellTunnel(maze, currCell, i))
								neighbours[freeNeighbourCount++] = i;
						}
						break;
					case 3: //WEST
						if (currCell.c > 0 && !visited[currCell.r + maze.deltaR[i]][currCell.c + maze.deltaC[i]]) {
							if(!isNeightbourCellTunnel(maze, currCell, i))
								neighbours[freeNeighbourCount++] = i;
						}
						break;
					case 5: //SOUTH
						if (currCell.r > 0 && !visited[currCell.r + maze.deltaR[i]][currCell.c + maze.deltaC[i]]) {
							if(!isNeightbourCellTunnel(maze, currCell, i))
								neighbours[freeNeighbourCount++] = i;
						}
						break;
				}
			}
		}else if(maze.type == maze.HEX){
			for (int i = 0; i < maze.NUM_DIR; i++) {
				switch (i) {
					case 0: //EAST
						if (currCell.c < maze.sizeC -1 && !visited[currCell.r][currCell.c + 1]) {
							neighbours[freeNeighbourCount++] = i;
						}
						break;
					case 1: //NORTHEAST
						if(currCell.r % 2 == 0){
							if(currCell.r <  maze.sizeR -1 && currCell.c <= maze.sizeC - 1 && !visited[currCell.r + 1][currCell.c]){
								neighbours[freeNeighbourCount++] = i;
							}
						}else{
							if(currCell.r <  maze.sizeR -1 && currCell.c < maze.sizeC - 1 && !visited[currCell.r + 1][currCell.c + 1]){
								neighbours[freeNeighbourCount++] = i;
							}
						}
						break;
					case 2: //NORTHWEST
						if(currCell.r % 2 == 0){
							if(currCell.r <  maze.sizeR -1 && currCell.c > 0 && !visited[currCell.r + 1][currCell.c - 1]){
								neighbours[freeNeighbourCount++] = i;
							}
						}else{
							if(currCell.r <  maze.sizeR -1 && currCell.c >= 0 && !visited[currCell.r + 1][currCell.c]){
								neighbours[freeNeighbourCount++] = i;
							}
						}
						break;
					case 3: //WEST
						if(currCell.c > 0 && !visited[currCell.r][currCell.c - 1]){
							neighbours[freeNeighbourCount++] = i;
						}
						break;
					case 4: //SOUTHWEST
						if(currCell.r % 2 == 0){
							if(currCell.r > 0 && currCell.c > 0 && !visited[currCell.r - 1][currCell.c - 1]){
								neighbours[freeNeighbourCount++] = i;
							}
						}else{
							if(currCell.r > 0 && currCell.c >= 0 && !visited[currCell.r - 1][currCell.c]){
								neighbours[freeNeighbourCount++] = i;
							}
						}
						break;
					case 5: //SOUTHEAST
						if(currCell.r % 2 == 0){
							if(currCell.r > 0 && currCell.c <= maze.sizeC - 1 && !visited[currCell.r - 1][currCell.c]){
								neighbours[freeNeighbourCount++] = i;
							}
						}else{
							if(currCell.r > 0 && currCell.c < maze.sizeC - 1 && !visited[currCell.r - 1][currCell.c + 1]){
								neighbours[freeNeighbourCount++] = i;
							}
						}
						break;
				}
			}
		}
		return freeNeighbourCount;
	}
	
	private void unblockNeighbourCell(Maze maze, Cell currCell, int direction){
		if(maze.type == maze.NORMAL || maze.type == maze.TUNNEL){
			switch (direction) {
				case 0:
					if(maze.map[currCell.r][currCell.c] != null){
						maze.map[currCell.r][currCell.c].wall[direction].present = false;
						currCell.c++;
					}
					break;
				case 2:
					if(maze.map[currCell.r][currCell.c] != null){
						maze.map[currCell.r][currCell.c].wall[direction].present = false;
						currCell.r++;
					}
					break;
				case 3:
					if(maze.map[currCell.r][currCell.c] != null){
						maze.map[currCell.r][currCell.c].wall[direction].present = false;
						currCell.c--;
					}
					break;
				case 5:
					if(maze.map[currCell.r][currCell.c] != null){
						maze.map[currCell.r][currCell.c].wall[direction].present = false;
						currCell.r--;
					}
					break;
			}
		}else if(maze.type == maze.HEX){
			switch (direction) {
				case 0: //EAST
					maze.map[currCell.r][currCell.c + (currCell.r + 1) / 2].wall[direction].present = false;
					currCell.c++;
					break;
				case 1: //NORTHEAST
					maze.map[currCell.r][currCell.c + (currCell.r + 1) / 2].wall[direction].present = false;
					if(currCell.r % 2 == 0){
						currCell.r++;
					}else{
						currCell.r++;
						currCell.c++;
					}
					break;
				case 2: //NORTHWEST
					maze.map[currCell.r][currCell.c + (currCell.r + 1) / 2].wall[direction].present = false;
					if(currCell.r % 2 == 0){
						currCell.r++;
						currCell.c--;
					}else{
						currCell.r++;
					}
					break;
				case 3: //WEST
					maze.map[currCell.r][currCell.c + (currCell.r + 1) / 2].wall[direction].present = false;
					currCell.c--;
					break;
				case 4: //SOUTHWEST
					maze.map[currCell.r][currCell.c + (currCell.r + 1) / 2].wall[direction].present = false;
					if(currCell.r % 2 == 0){
						currCell.r--;
						currCell.c--;
					}else{
						currCell.r--;
					}
					break;
				case 5: //SOUTHEAST
					maze.map[currCell.r][currCell.c + (currCell.r + 1) / 2].wall[direction].present = false;
					if(currCell.r % 2 == 0){
						currCell.r--;
					}else{
						currCell.r--;
						currCell.c++;
					}
					break;
			}
		}
	}
	
	private boolean isNeightbourCellTunnel(Maze maze, Cell currCell, int direction){
		if(currCell.tunnelTo != null && currCell.tunnelTo.r == currCell.r + maze.deltaR[direction] && currCell.tunnelTo.c == currCell.c + maze.deltaC[direction]){
			return true;
		}
		return false;
	}
} // end of class RecursiveBacktrackerGenerator
