package mazeGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

import maze.Maze;

public class KruskalGenerator implements MazeGenerator {
	private static List<List<Tree>> _sets;
	private static Stack<Edge> _edges;
	int[][] _grid = null;
	
	Random random = new Random();
	
	private static final int E = 1;
	private static final int NE = 2;
	private static final int N = 4;
	private static final int NW = 4;
	private static final int W = 8;
	private static final int SW = 16;
	private static final int SE = 32;
	private static final int S = 32;
	
	private static int maxR;
	private static int maxC;
	
	@Override
	public void generateMaze(Maze maze) {
		processGenerateMaze(maze);
	} // end of generateMaze()
	
	private void processGenerateMaze(Maze maze)
	{
		maxR = maze.sizeR;
		maxC = maze.sizeC;
		
		//initial
		initialGrid();
		initialSetTree();
		initialEdge(maze.type);

		shuffle(_edges);
		
		carvePassages(maze.type);

		// draw each row
		drawUnblockCell(maze);
	}
	
	private void initialGrid(){
		_grid = new int[maxR][maxC];
		for ( int j=0; j < maxR; ++j ) {
			for ( int i=0; i < maxC; ++i ) {
				_grid[j][i] = 0;
			}
		}
	}
	
	private void initialSetTree(){
		_sets = new ArrayList<List<Tree>>();
		for ( int y=0; y < maxR; ++y ) {
			List<Tree> tmp = new ArrayList<Tree>();
			for ( int x=0; x < maxC; ++x ) {
				tmp.add(new Tree());
			}
			_sets.add(tmp);
		}
	}
	
	private void initialEdge(int type){
		// Build the collection of edges and randomize. 
		// Edges are "north" and "west" sides of cell, 
		// if index is greater than 0.
		if(type == 0 || type == 1){
			_edges = new Stack<Edge>();
			for ( int y=0; y < maxR; ++y ) {
				for (int x=0; x < maxC; ++x ) {
					if ( y > 0 ) 	{ _edges.add(new Edge(x,y,N)); }
					if ( x > 0 ) 	{ _edges.add(new Edge(x,y,W)); }
				}
			}
		}else if(type == 2){
			_edges = new Stack<Edge>();
			for ( int y=0; y < maxR; ++y ) {
				for (int x=0; x < maxC; ++x ) {
					if ( y > 0 && x > 0 ) 		{ _edges.add(new Edge(x,y,NW)); }
					if ( y > 0 && x < maxC -1 ) { _edges.add(new Edge(x,y,NE)); }
					if ( x > 0 ) 				{ _edges.add(new Edge(x,y,W)); }
				}
			}
		}
		
	}
	
	private void shuffle(List<Edge> args) {
		for ( int i=0; i < _edges.size(); ++i ) {
			int pos = random.nextInt(_edges.size());
			Edge tmp1 = _edges.get(i);
			Edge tmp2 = _edges.get(pos);
			_edges.set(i,tmp2);
			_edges.set(pos,tmp1);
		}
	}
	
	private void drawUnblockCell(Maze maze){
		if(maze.type == maze.NORMAL || maze.type == maze.TUNNEL){
			for ( int j=0; j < maxR; ++j ) {
				for ( int i=0; i < maxC; ++i ) {
					if((_grid[j][i] & S) != 0){
						maze.map[maxR - j - 1][i].wall[5].present = false;
					}
					
					if ( (_grid[j][i] & E) != 0 ) {
						maze.map[maxR - j - 1][i].wall[0].present = false;
					}
				}
			}
		}else if(maze.type == maze.HEX){
			for ( int j=0; j < maxR; ++j ) {
				for ( int i=0; i < maxC; ++i ) {
					int h = maxR - j - 1;
					if((_grid[j][i] & SW) != 0){
						maze.map[h][i + (h + 1) / 2].wall[4].present = false;
					}
					
					if((_grid[j][i] & SE) != 0){
						maze.map[h][i + (h + 1) / 2].wall[5].present = false;
					}
					// render "side" using "E" switch
					if ( (_grid[j][i] & E) != 0 ) {
						maze.map[h][i + (h + 1) / 2].wall[0].present = false;
					}
				}
			}
		}
	}
	
	private void carvePassages(int type) {
		while ( _edges.size() > 0 ) {
			// Select the next edge, and decide which direction we are going in.
			Edge tmp = _edges.pop();
			int x = tmp.getX();
			int y = tmp.getY();
			int direction = tmp.getDirection();
			int dx = x + DX(direction, type, y, maxR), dy = y + DY(direction, type);
			
			// Pluck out the corresponding sets
			Tree set1 = (_sets.get(y)).get(x);
			Tree set2 = (_sets.get(dy)).get(dx);
			
			if ( !set1.connected(set2) ) {
				// Connect the two sets and "knock down" the wall between them.
				set1.connect(set2);
				_grid[y][x] |= direction;
				_grid[dy][dx] |= OPPOSITE(direction, type);
			}
		}
	}
	
	
	// Define class methods
	public static int DX(int direction, int type, int row, int maxRow) {
		if(type == 0 || type == 1){
			switch ( direction ) {
				case E:
					return +1;
				case W:
					return -1;
				case N:
				case S:
					return 0;
			}
		}else if(type == 2){
			if(maxRow % 2 == 0){
				if(row %2 == 0){
					switch ( direction ) {
						case E:
							return 1;
						case W:
							return -1;
						case NE:
							return 1;
						case NW:
							return 0;
						case SE:
							return 1;
						case SW:
							return 0;
					}
				}else{
					switch ( direction ) {
						case E:
							return 1;
						case W:
							return -1;
						case NE:
							return 0;
						case NW:
							return -1;
						case SE:
							return 0;
						case SW:
							return -1;
					}
				}
			}else{
				if(row %2 == 0){
					switch ( direction ) {
						case E:
							return 1;
						case W:
							return -1;
						case NE:
							return 0;
						case NW:
							return -1;
						case SE:
							return 0;
						case SW:
							return -1;
					}
				}else{
					switch ( direction ) {
						case E:
							return 1;
						case W:
							return -1;
						case NE:
							return 1;
						case NW:
							return 0;
						case SE:
							return 1;
						case SW:
							return 0;
					}
				}
			}
		}
		// error condition, but should never reach here
		return -1;
	}

	public static int DY(int direction, int type) {
		if(type == 0 || type == 1){
			switch ( direction ) {
				case E:
					return 0;
				case W:
					return 0;
				case N:
					return -1;
				case S:
					return 1;
			}
		}else if(type == 2){
			switch ( direction ) {
			case E:
				return 0;
			case W:
				return 0;
			case NE:
				return -1;
			case NW:
				return -1;
			case SE:
				return 1;
			case SW:
				return 1;
		}
			
		}
		// error condition, but should never reach here
		return -1;
	}

	public static int OPPOSITE(int direction, int type) {
		if(type == 0 || type == 1){
			switch ( direction ) {
				case E:
					return W;
				case W:
					return E;
				case N:
					return S;
				case S:
					return N;
			}
		}else if(type == 2){
			switch ( direction ) {
				case E:
					return W;
				case W:
					return E;
				case NE:
					return SW;
				case NW:
					return SE;
				case SE:
					return NW;
				case SW:
					return NE;
			}
		}
		// error condition, but should never reach here
		return -1;
	}
}

/***********************************************************************
 * We will use a tree structure to model the "set" (or "vertex") 
 * that is used in Kruskal to build the graph.
 * 
 * @author psholtz
 ***********************************************************************/
class Tree {
	
	private Tree _parent = null;
	
	//
	// Build a new tree object
	//
	public Tree() {
		
	}
	
	// 
	// If we are joined, return the root. Otherwise, return this object instance.
	//
	public Tree root() {
		return _parent != null ? _parent.root() : this;
	}
	
	// 
	// Are we connected to this tree?
	//
	public boolean connected(Tree tree) {
		return this.root() == tree.root();
	}
	
	//
	// Connect to the tree
	//
	public void connect(Tree tree) {
		tree.root().setParent(this);
	}
	
	//
	// Set the parent of the object instance
	public void setParent(Tree parent) {
		this._parent = parent;
	}
}

/*********************************************************************************************
 * Encapsulates the x,y coord of where the edge starts, and the direction in which it points.
 * 
 * @author psholtz
 *********************************************************************************************/
class Edge {
	private int _x;
	private int _y;
	private int _direction;
	
	public Edge(int x, int y, int direction) {
		_x = x; 
		_y = y;
		_direction = direction;
	}
	
	public int getX() { return _x; }
	public int getY() { return _y; }
	public int getDirection() { return _direction; }
}