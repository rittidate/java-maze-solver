package mazeSolver;

import java.util.LinkedList;
import java.util.Random;

import maze.Cell;
import maze.Maze;

/**
 * Implements the recursive backtracking maze solving algorithm.
 */
public class RecursiveBacktrackerSolver implements MazeSolver {
	
	int cellExploredCount = 0;
	boolean isSolved = false;
	
	boolean visited[][];
	int[] neighbours = new int[6];
	LinkedList<Cell> stack = new LinkedList<Cell>();
	
	int maxR = 0;
	int maxC = 0;
	
	private Random rand = new Random();
	
	@Override
	public void solveMaze(Maze maze) {
		maxR = maze.sizeR;
		maxC = maze.sizeC;
		visited = new boolean[maxR][maxC];

		processSolveMaze(maze, maze.entrance);
		if(!isSolved){
			processSolveMaze(maze, maze.exit);
			isSolved = true;
		}

	} // end of solveMaze()

	@Override
	public boolean isSolved() {
		return isSolved;
	} // end if isSolved()


	@Override
	public int cellsExplored() {
		return cellExploredCount;
	} // end of cellsExplored()
	
	private void processSolveMaze(Maze maze, Cell startCell){
		Cell cell = startCellProcess(maze, startCell);
		stack.addFirst(cell);
		drawPath(maze, cell);
		while(!stack.isEmpty()){
			int freeNeighbourCount = 0;
			freeNeighbourCount = freeNeighbourBlock(maze, freeNeighbourCount, cell);
			if (freeNeighbourCount > 0) {
				stack.addFirst(cell);
				cell = new Cell(cell.r, cell.c);
				int direction = neighbours[rand.nextInt(freeNeighbourCount)];
				moveToNextCell(maze, cell, direction);
				drawPath(maze, cell);
				if(isTerminateProcess(maze, cell)){
					isSolved = true;
					break;
				}
			}else{
				cell = stack.removeFirst();
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
	
	private int freeNeighbourBlock(Maze maze, int freeNeighbourCount, Cell currCell){
		if(maze.type == maze.NORMAL || maze.type == maze.TUNNEL){
			for (int i = 0; i < maze.NUM_DIR; i++) {
				switch (i) {
					case 0: //EAST
						if (currCell.c < maxC - 1 && !visited[currCell.r + maze.deltaR[i]][currCell.c + maze.deltaC[i]] && !maze.map[currCell.r][currCell.c].wall[i].present) {
							neighbours[freeNeighbourCount++] = i;
						}
						break;
					case 2: //NORTH
						if (currCell.r < maxR - 1 && !visited[currCell.r + maze.deltaR[i]][currCell.c + maze.deltaC[i]] && !maze.map[currCell.r][currCell.c].wall[i].present) {
							neighbours[freeNeighbourCount++] = i;
						}
						break;
					case 3: //WEST
						if (currCell.c > 0 && !visited[currCell.r + maze.deltaR[i]][currCell.c + maze.deltaC[i]] && !maze.map[currCell.r][currCell.c].wall[i].present) {
							neighbours[freeNeighbourCount++] = i;
						}
						break;
					case 5: //SOUTH
						if (currCell.r > 0 && !visited[currCell.r + maze.deltaR[i]][currCell.c + maze.deltaC[i]] && !maze.map[currCell.r][currCell.c].wall[i].present) {
							neighbours[freeNeighbourCount++] = i;
						}
						break;
				}
			}
		}else if(maze.type == maze.HEX){
			for (int i = 0; i < maze.NUM_DIR; i++) {
				switch (i) {
					case 0: //EAST
						if (currCell.c < maxC -1 && !visited[currCell.r][currCell.c + 1] && !maze.map[currCell.r][currCell.c + (currCell.r + 1) / 2].wall[i].present) {
							neighbours[freeNeighbourCount++] = i;
						}
						break;
					case 1: //NORTHEAST
						if(currCell.r % 2 == 0){
							if(currCell.r <  maxR -1 && currCell.c <= maze.sizeC - 1 && !visited[currCell.r + 1][currCell.c] && !maze.map[currCell.r][currCell.c + (currCell.r + 1) / 2].wall[i].present){
								neighbours[freeNeighbourCount++] = i;
							}
						}else{
							if(currCell.r <  maxR -1 && currCell.c < maze.sizeC - 1 && !visited[currCell.r + 1][currCell.c + 1] && !maze.map[currCell.r][currCell.c + (currCell.r + 1) / 2].wall[i].present){
								neighbours[freeNeighbourCount++] = i;
							}
						}
						break;
					case 2: //NORTHWEST
						if(currCell.r % 2 == 0){
							if(currCell.r <  maxR -1 && currCell.c > 0 && !visited[currCell.r + 1][currCell.c - 1] && !maze.map[currCell.r][currCell.c + (currCell.r + 1) / 2].wall[i].present){
								neighbours[freeNeighbourCount++] = i;
							}
						}else{
							if(currCell.r <  maxR -1 && currCell.c >= 0 && !visited[currCell.r + 1][currCell.c] && !maze.map[currCell.r][currCell.c + (currCell.r + 1) / 2].wall[i].present){
								neighbours[freeNeighbourCount++] = i;
							}
						}
						break;
					case 3: //WEST
						if(currCell.c > 0 && !visited[currCell.r][currCell.c - 1] && !maze.map[currCell.r][currCell.c + (currCell.r + 1) / 2].wall[i].present){
							neighbours[freeNeighbourCount++] = i;
						}
						break;
					case 4: //SOUTHWEST
						if(currCell.r % 2 == 0){
							if(currCell.r > 0 && currCell.c > 0 && !visited[currCell.r - 1][currCell.c - 1] && !maze.map[currCell.r][currCell.c + (currCell.r + 1) / 2].wall[i].present){
								neighbours[freeNeighbourCount++] = i;
							}
						}else{
							if(currCell.r > 0 && currCell.c >= 0 && !visited[currCell.r - 1][currCell.c] && !maze.map[currCell.r][currCell.c + (currCell.r + 1) / 2].wall[i].present){
								neighbours[freeNeighbourCount++] = i;
							}
						}
						break;
					case 5: //SOUTHEAST
						if(currCell.r % 2 == 0){
							if(currCell.r > 0 && currCell.c <= maxC - 1 && !visited[currCell.r - 1][currCell.c] && !maze.map[currCell.r][currCell.c + (currCell.r + 1) / 2].wall[i].present){
								neighbours[freeNeighbourCount++] = i;
							}
						}else{
							if(currCell.r > 0 && currCell.c < maxC - 1 && !visited[currCell.r - 1][currCell.c + 1] && !maze.map[currCell.r][currCell.c + (currCell.r + 1) / 2].wall[i].present){
								neighbours[freeNeighbourCount++] = i;
							}
						}
						break;
				}
			}
		}
		return freeNeighbourCount;
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
	
	private void moveToNextCell(Maze maze, Cell currCell, int direction){
		if(maze.type == maze.NORMAL || maze.type == maze.TUNNEL){
			switch (direction) {
				case 0: //EAST
					if(maze.map[currCell.r][currCell.c] != null){
						currCell.c++;
					}
					break;
				case 2: //NORTH
					if(maze.map[currCell.r][currCell.c] != null){
						currCell.r++;
					}
					break;
				case 3: //WEST
					if(maze.map[currCell.r][currCell.c] != null){
						currCell.c--;
					}
					break;
				case 5: //SOUTH
					if(maze.map[currCell.r][currCell.c] != null){
						currCell.r--;
					}
					break;
			}
		}else if(maze.type == maze.HEX){
			switch (direction) {
				case 0: //EAST
					currCell.c++;
					break;
				case 1: //NORTHEAST
					if(currCell.r % 2 == 0){
						currCell.r++;
					}else{
						currCell.r++;
						currCell.c++;
					}
					break;
				case 2: //NORTHWEST
					if(currCell.r % 2 == 0){
						currCell.r++;
						currCell.c--;
					}else{
						currCell.r++;
					}
					break;
				case 3: //WEST
					currCell.c--;
					break;
				case 4: //SOUTHWEST
					if(currCell.r % 2 == 0){
						currCell.r--;
						currCell.c--;
					}else{
						currCell.r--;
					}
					break;
				case 5: //SOUTHEAST
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
	
} // end of class RecursiveBackTrackerSolver
