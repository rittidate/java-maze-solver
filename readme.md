
Compiling and Executing
To compile the files, run the following command from the root directory (the directory that MazeTester.java is in):
                   javac -cp .:mazeSolver/SampleSolver.jar *.java
To run the maze visualisation and evaluation algorithm, run the following command:
   java -cp .:mazeSolver/SampleSolver.jar MazeTester <parameter file> <visualise maze
and solver path>
where
• parameter file: name of the file that contains the parameter specifications of the run. • visualise maze and solver path: [y/n], to indicate whether to visualise (y) or not (n).
The jar file contains bytecode for a solver we have already coded up. This is to help you see a solver in action on your maze (before you have implemented your own solvers).
We now describe the contents of the parameter file.
Parameters
The parameter file specifies all the settings for the maze generation, evaluation and visualisation. This file has the followng format:
mazeType
generatorName solverName
numRow numCol entranceRow entranceCol exitRow exitCol tunnelList
Where parameters are as follows:
• mazeType: The type of maze to generate, where it should be one of {normal, tunnel, hex}.
• generatorName: Name of maze generation algorithm, where it should be one of {recurBack, kruskal, modiPrim}. recurBack is recursive backtracker, kruskal is kruskal’s algorithm, and modiPrim is (modified) prim’s algorithm.
• solverName: Name of the maze generation algorithm, where it should be one of {bidir, recurBack, sample, none}. bidir is bidirectional BFS, recurback is recursive backtracker (solver), sample is a sample solver for you to visualise solving and none is to specify that no solving is wanted.
• numRow: Number of rows in the generated maze, it should be a positive integer (1 or greater).
• numCol: Number of columns in the generated maze, it should be a positive integer (1 or greater).
• entranceRow: the row of the entrance cell, should be in range [0, numRow-1].
• entranceCol: the column of the entrance cell, should be in range [0, numCol-1].
• exitRow: the row of the exit cell, should be in range [0, numRow-1].
• exitCol: the column of the exit cell, should be in range [0, numCol-1].
For tunnel mazes, we have additional parameters (the non-tunnelled mazes will ignore these pa- rameter settings if they are specified):
• tunnelList: list of tunnels, one per line. Each line consists of the (row, column) of the cells on each end of the tunnel. For example, “a b c d” means one end of the tunnel is at (a, b) while the other end is (c, d). Each row must be in range [0, numRow-1], each column in range [0, numCol-1].

