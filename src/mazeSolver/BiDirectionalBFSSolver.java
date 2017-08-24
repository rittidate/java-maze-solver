package mazeSolver;

import java.util.LinkedList;
import java.util.Random;

import maze.Cell;
import maze.Maze;

/** 
 * Implements Bi-directional BFS maze solving algorithm.
 */
public class BiDirectionalBFSSolver implements MazeSolver {

	int cellExploredCount = 0;
	private Random rand = new Random();
	boolean isSolved = false;
	
	boolean visited[][];
	LinkedList<Cell> queue = new LinkedList<Cell>();
	
	@Override
	public void solveMaze(Maze maze) {
		visited = new boolean[maze.sizeR][maze.sizeC];
		
		processSolveMaze(maze, maze.entrance);
		if(!isSolved){
			processSolveMaze(maze, maze.exit);
			isSolved = true;
		}
	} // end of solveMaze()

	@Override
	public boolean isSolved() {
		return isSolved;
	} // end of isSolved()
	
	@Override
	public int cellsExplored() {
		return cellExploredCount;
	} // end of cellsExplored()

	private void processSolveMaze(Maze maze, Cell startCell){
		Cell cell = startCellProcess(maze, startCell);
		
		drawPath(maze, cell);
		addNeighbourCellQueue(maze, cell);
		
		while(!queue.isEmpty()){
			cell = queue.remove();
			addNeighbourCellQueue(maze, cell);
			
			if(!visited[cell.r][cell.c]){
				drawPath(maze, cell);
			}
	
			if(isTerminateProcess(maze, cell)){
				isSolved = true;
				break;
			}
		}
	}
	
	private Cell startCellProcess(Maze maze, Cell startCell){
		Cell cell = null;
		if(maze.type == maze.NORMAL || maze.type == maze.TUNNEL)
			cell = startCell;
		else if(maze.type == maze.HEX)
			cell = new Cell(startCell.r, startCell.c - (startCell.r + 1) / 2);
		
		return cell;
	}

	private void drawPath(Maze maze, Cell currCell){
		if(maze.type == maze.NORMAL || maze.type == maze.TUNNEL){
			maze.drawFtPrt(currCell);
			visited[currCell.r][currCell.c] = true;
			cellExploredCount++;
		}else if(maze.type == maze.HEX){
			maze.drawFtPrt(maze.map[currCell.r][currCell.c + (currCell.r + 1) / 2]);
			visited[currCell.r][currCell.c] = true;
			cellExploredCount++;
		}
	}
	
	private void addNeighbourCellQueue(Maze maze, Cell currCell){
		if(maze.type == maze.NORMAL || maze.type == maze.TUNNEL){
			for (int i = 0; i < maze.NUM_DIR; i++) {
				switch (i) {
					case 0: //EAST
						if (currCell.c < maze.sizeC - 1 && !visited[currCell.r + maze.deltaR[i]][currCell.c + maze.deltaC[i]] && !maze.map[currCell.r][currCell.c].wall[i].present) {
							queue.add(new Cell(currCell.r + maze.deltaR[i], currCell.c + maze.deltaC[i]));
						}
						break;
					case 2: //NORTH
						if (currCell.r < maze.sizeR - 1 && !visited[currCell.r + maze.deltaR[i]][currCell.c + maze.deltaC[i]] && !maze.map[currCell.r][currCell.c].wall[i].present) {
							queue.add(new Cell(currCell.r + maze.deltaR[i], currCell.c + maze.deltaC[i]));
						}
						break;
					case 3: //WEST
						if (currCell.c > 0 && !visited[currCell.r + maze.deltaR[i]][currCell.c + maze.deltaC[i]] && !maze.map[currCell.r][currCell.c].wall[i].present) {
							queue.add(new Cell(currCell.r + maze.deltaR[i], currCell.c + maze.deltaC[i]));
						}
						break;
					case 5: //SOUTH
						if (currCell.r > 0 && !visited[currCell.r + maze.deltaR[i]][currCell.c + maze.deltaC[i]] && !maze.map[currCell.r][currCell.c].wall[i].present) {
							queue.add(new Cell(currCell.r + maze.deltaR[i], currCell.c + maze.deltaC[i]));
						}
						break;
				}
			}
		}else if(maze.type == maze.HEX){
			for (int i = 0; i < maze.NUM_DIR; i++) {
				switch (i) {
					case 0: //EAST
						if (currCell.c < maze.sizeC -1 && !visited[currCell.r][currCell.c + 1] && !maze.map[currCell.r][currCell.c + (currCell.r + 1) / 2].wall[i].present) {
							queue.add(new Cell(currCell.r, currCell.c + 1));
						}
						break;
					case 1: //NORTHEAST
						if(currCell.r % 2 == 0){
							if(currCell.r <  maze.sizeR -1 && currCell.c <= maze.sizeC - 1 && !visited[currCell.r + 1][currCell.c] && !maze.map[currCell.r][currCell.c + (currCell.r + 1) / 2].wall[i].present){
								queue.add(new Cell(currCell.r+1, currCell.c));
							}
						}else{
							if(currCell.r <  maze.sizeR -1 && currCell.c < maze.sizeC - 1 && !visited[currCell.r + 1][currCell.c + 1] && !maze.map[currCell.r][currCell.c + (currCell.r + 1) / 2].wall[i].present){
								queue.add(new Cell(currCell.r+1, currCell.c+1));
							}
						}
						break;
					case 2: //NORTHWEST
						if(currCell.r % 2 == 0){
							if(currCell.r <  maze.sizeR -1 && currCell.c > 0 && !visited[currCell.r + 1][currCell.c - 1] && !maze.map[currCell.r][currCell.c + (currCell.r + 1) / 2].wall[i].present){
								queue.add(new Cell(currCell.r+1, currCell.c-1));
							}
						}else{
							if(currCell.r <  maze.sizeR -1 && currCell.c >= 0 && !visited[currCell.r + 1][currCell.c] && !maze.map[currCell.r][currCell.c + (currCell.r + 1) / 2].wall[i].present){
								queue.add(new Cell(currCell.r+1, currCell.c));
							}
						}
						break;
					case 3: //WEST
						if(currCell.c > 0 && !visited[currCell.r][currCell.c - 1] && !maze.map[currCell.r][currCell.c + (currCell.r + 1) / 2].wall[i].present){
							queue.add(new Cell(currCell.r, currCell.c-1));
						}
						break;
					case 4: //SOUTHWEST
						if(currCell.r % 2 == 0){
							if(currCell.r > 0 && currCell.c > 0 && !visited[currCell.r - 1][currCell.c - 1] && !maze.map[currCell.r][currCell.c + (currCell.r + 1) / 2].wall[i].present){
								queue.add(new Cell(currCell.r-1, currCell.c-1));
							}
						}else{
							if(currCell.r > 0 && currCell.c >= 0 && !visited[currCell.r - 1][currCell.c] && !maze.map[currCell.r][currCell.c + (currCell.r + 1) / 2].wall[i].present){
								queue.add(new Cell(currCell.r-1, currCell.c));
							}
						}
						break;
					case 5: //SOUTHEAST
						if(currCell.r % 2 == 0){
							if(currCell.r > 0 && currCell.c <= maze.sizeC - 1 && !visited[currCell.r - 1][currCell.c] && !maze.map[currCell.r][currCell.c + (currCell.r + 1) / 2].wall[i].present){
								queue.add(new Cell(currCell.r-1, currCell.c));
							}
						}else{
							if(currCell.r > 0 && currCell.c < maze.sizeC - 1 && !visited[currCell.r - 1][currCell.c + 1] && !maze.map[currCell.r][currCell.c + (currCell.r + 1) / 2].wall[i].present){
								queue.add(new Cell(currCell.r-1, currCell.c+1));
							}
						}
						break;
				}
			}
		}
	}
	
	private boolean isTerminateProcess(Maze maze, Cell currCell){
		if(maze.type == maze.NORMAL || maze.type == maze.TUNNEL){
			if(maze.exit.r == currCell.r && maze.exit.c == currCell.c)
				return true;
		}else if(maze.type == maze.HEX){
			if(maze.exit.r == currCell.r && maze.exit.c == currCell.c + (currCell.r + 1) / 2 )
				return true;
		}
		return false;
	}
} // end of class BiDirectionalBFSSolver

