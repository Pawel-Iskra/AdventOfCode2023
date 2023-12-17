package day17;

import utils.MyUtilities;

import java.util.*;

public class ClumsyCrucible {


    // you'll need to find the best way to get the crucible from the lava pool to the machine parts factory.
    // To do this, you need to minimize heat loss while choosing a route that doesn't require the crucible
    // to go in a straight line for too long.

    // how much heat loss can be expected for a crucible entering any particular city block.

    // Each city block is marked by a single digit that represents the amount of heat loss if the crucible enters that block.
    // The starting point, the lava pool, is the top-left city block; the destination,
    // the machine parts factory, is the bottom-right city block.
    //

    // it can move at most three blocks in a single direction before it must turn 90 degrees left or right.
    // The crucible also can't reverse direction; after entering each city block, it may only turn left, continue straight, or turn right.

    private static int[][] MAP;
    private static int ROWS;
    private static int COLUMNS;
    private static DirectedGraph GRAPH;

    static class DirectedGraph {
        private int vertices;
        private int edges;
        private List<DirectedEdge>[] adjacencyList;

        public DirectedGraph(int vertices) {
            this.vertices = vertices;
            this.edges = 0;
            this.adjacencyList = new List[vertices];
            for (int i = 0; i < vertices; i++) {
                adjacencyList[i] = new ArrayList<>();
            }
        }

        public int getVertices() {
            return vertices;
        }

        public void addEdge(DirectedEdge directedEdge) {
            adjacencyList[directedEdge.getFrom()].add(directedEdge);
            edges++;
        }

        public List<DirectedEdge> getDirectedEdgesFromNode(int vertex) {
            return adjacencyList[vertex];
        }
    }

    static class DirectedEdge {
        private int from;
        private int to;
        private int weight;

        public DirectedEdge(int from, int to, int weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }

        public int getFrom() {
            return from;
        }

        public int getTo() {
            return to;
        }

        public int getWeight() {
            return weight;
        }

        @Override
        public String toString() {
            return String.format("%d -> %d (%d)", from, to, weight);
        }
    }


    static class CurrentDistanceToStart {
        private int from;
        private int to;
        private long dist;

        public CurrentDistanceToStart(int from, int to, long dist) {
            this.from = from;
            this.to = to;
            this.dist = dist;
        }

        public int getFrom() {
            return from;
        }

        public int getTo() {
            return to;
        }

        public long getDist() {
            return dist;
        }
    }

    static class DijkstraShortestPath {
        private int start;
        private DirectedGraph myGraph;
        private int vertices;
        private long[] shortestPaths;
        private int[] previous;
        private boolean[] visited;
        private int stepsInCurrentDir;

        public DijkstraShortestPath(int start, DirectedGraph myGraph) {
            this.start = start;
            this.myGraph = myGraph;
            this.vertices = myGraph.getVertices();
            this.visited = new boolean[vertices];
            this.shortestPaths = new long[vertices];
            this.previous = new int[vertices];
            this.stepsInCurrentDir = 0;
            for (int i = 0; i < vertices; i++) {
                shortestPaths[i] = Long.MAX_VALUE;
            }
            shortestPaths[start] = 0;
            previous[start] = start;
        }


        public void runDijkstraAlgorithm() {
            Queue<CurrentDistanceToStart> currentDistancesQueue =
                    new PriorityQueue<>(vertices, Comparator.comparingLong(o -> o.getDist()));
            currentDistancesQueue.add(new CurrentDistanceToStart(start, start, 0));
            String dir;
            int previousVert = start;
            List<Integer> previousList = new ArrayList<>();
            previousList.add(start);
            while (!currentDistancesQueue.isEmpty()) {
                visited[start] = true;
                start = currentDistancesQueue.poll().getTo();

                List<DirectedEdge> directedEdgesFromNode = myGraph.getDirectedEdgesFromNode(start);
                long currDistToSrc = shortestPaths[start];

                //if... -> update shortestPaths for neighbors, update previous
                int size = directedEdgesFromNode.size();
                for (int i = 0; i < size; i++) {
                    DirectedEdge current = directedEdgesFromNode.get(i);
                    long calcDist = currDistToSrc + current.getWeight();
                    int to = current.getTo();


//                    if (stepsInCurrentDir == 3) {
//                        stepsInCurrentDir = 0;
//                        continue;
//                    }


                    if (previousList.size() > 0) {
//                        int firstBefore = previousList.get(previousList.size() - 1);
//                        int secondBefore = previousList.get(previousList.size() - 2);
//                        int thirdBefore = previousList.get(previousList.size() - 3);
//                        System.out.println("\nfirstBefore = " + firstBefore);
//                        System.out.println("secondBefore = " + secondBefore);
//                        System.out.println("thirdBefore = " + thirdBefore);
//                        System.out.println("to = " + to);
//                        System.out.println("isInOneDir = " + isInOneDir);

                        List<Integer> previouses = getThreePrevoiusVertex(current.getFrom());
                        boolean isInOneDir = checkIfIsInOneDir(to, current.getFrom(), previouses.get(0), previouses.get(1), previouses.get(2));
                        System.out.println("\npreviouses = " + previouses);
                        System.out.println("current.getFrom() = " + current.getFrom());
                        System.out.println("to = " + to);
                        System.out.println("isInOneDir = " + isInOneDir);
                        if (isInOneDir) continue;

                    }

                    if (calcDist < shortestPaths[to]) {
                        previousList.add(to);
                        shortestPaths[to] = calcDist;
                        previous[to] = start;
                        if (!visited[to]) {
                            currentDistancesQueue.add(
                                    new CurrentDistanceToStart(current.getFrom(), current.getTo(), calcDist));
                        }
                    }
                }
            }
        }

        private List<Integer> getThreePrevoiusVertex(final int from) {
            List<Integer> prevoiusList = new ArrayList<>();
            int firstPrev = previous[from];
            int secPrev = previous[firstPrev];
            int thirdPrev = previous[secPrev];
            prevoiusList.add(firstPrev);
            prevoiusList.add(secPrev);
            prevoiusList.add(thirdPrev);

            return prevoiusList;
        }

        private boolean checkIfIsInOneDir(int to, final int firstBefore, final int secondBefore, final int thirdBefore, int fourthBefore) {
            if (to / COLUMNS == firstBefore / COLUMNS && firstBefore / COLUMNS == secondBefore / COLUMNS && secondBefore / COLUMNS == thirdBefore / COLUMNS && thirdBefore / COLUMNS == fourthBefore / COLUMNS) {
                if (to == (firstBefore + 1) && firstBefore == (secondBefore + 1) && secondBefore == (thirdBefore + 1) && thirdBefore == (fourthBefore + 1))
                    return true;
                if (to == (firstBefore - 1) && firstBefore == (secondBefore - 1) && secondBefore == (thirdBefore - 1) && thirdBefore == (fourthBefore - 1))
                    return true;
            }

            if (to % COLUMNS == firstBefore % COLUMNS && firstBefore % COLUMNS == secondBefore % COLUMNS && secondBefore % COLUMNS == thirdBefore % COLUMNS && thirdBefore % COLUMNS == fourthBefore % COLUMNS) {
                if (to == (firstBefore + COLUMNS) && firstBefore == (secondBefore + COLUMNS) && secondBefore == (thirdBefore + COLUMNS) && thirdBefore == (fourthBefore + COLUMNS))
                    return true;
                if (to == (firstBefore - COLUMNS) && firstBefore == (secondBefore - COLUMNS) && secondBefore == (thirdBefore - COLUMNS) && thirdBefore == (fourthBefore - COLUMNS))
                    return true;
            }
            return false;
        }

        public long[] getShortestPaths() {
            return shortestPaths;
        }

        public int[] getPrevious() {
            return previous;
        }

        public boolean[] getVisited() {
            return visited;
        }
    }


    private static void prepareMap(final List<String> inputLines) {
        ROWS = inputLines.size();
        COLUMNS = inputLines.get(0).length();
        MAP = new int[ROWS][COLUMNS];
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                MAP[i][j] = Integer.parseInt(String.valueOf(inputLines.get(i).charAt(j)));
            }
        }
    }

    private static void printGraph() {
        int vertices = GRAPH.getVertices();
        System.out.println("GRAPH:");
        for (int i = 0; i < vertices; i++) {
            List<DirectedEdge> edgeList = GRAPH.adjacencyList[i];
            System.out.println(i + " -> " + edgeList);
        }

    }

    private static void generateGraph(List<String> inputLines) {
        int vertices = ROWS * COLUMNS;
        GRAPH = new DirectedGraph(vertices);

        for (int i = 0; i < vertices; i++) {
            int currentRow = i / COLUMNS;
            int currentColumn = i - currentRow * COLUMNS;

            int upperRow = currentRow - 1;
            if (upperRow >= 0) { // go up
                int heatLoss = MAP[upperRow][currentColumn];
                GRAPH.addEdge(new DirectedEdge(i, (i - COLUMNS), heatLoss));
            }
            int lowerRow = currentRow + 1;
            if (lowerRow < ROWS) { // go down
                int heatLoss = MAP[lowerRow][currentColumn];
                GRAPH.addEdge(new DirectedEdge(i, (i + COLUMNS), heatLoss));
            }
            int rightColumn = currentColumn + 1;
            if (rightColumn < COLUMNS) { // go right
                int heatLoss = MAP[currentRow][rightColumn];
                GRAPH.addEdge(new DirectedEdge(i, (i + 1), heatLoss));

            }
            int leftColumn = currentColumn - 1;
            if (leftColumn >= 0) { // go left
                int heatLoss = MAP[currentRow][leftColumn];
                GRAPH.addEdge(new DirectedEdge(i, (i - 1), heatLoss));
            }
        }
        printGraph();
    }

    private static String partTwo() {

        return null;
    }

    private static long partOne() {
        int myStart = 1;
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(myStart, GRAPH);
        dijkstraShortestPath.runDijkstraAlgorithm();
        long[] shortestPaths = dijkstraShortestPath.getShortestPaths();
        System.out.println("shortestPaths = " + Arrays.toString(shortestPaths));

        int[] previouses = dijkstraShortestPath.getPrevious();
        int start = GRAPH.getVertices()-1;
        System.out.print("\nstart = " + start + " -> ");
        int currentVert = start;
        List<Integer> prevList = new ArrayList<>();
        prevList.add(start);
        while (currentVert != myStart){
            currentVert = previouses[currentVert];
//            System.out.print(currentVert + " -> ");
            prevList.add(currentVert);
        }
        System.out.println();
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                if(prevList.contains(i * COLUMNS + j)){
                    System.out.print("# ");
                } else System.out.print(". ");
            }
            System.out.println();
        }
        System.out.println("prevList = " + prevList);

        return shortestPaths[GRAPH.getVertices() -1];
    }
    // 999 - too high
    // 833 - too high
    //       1    2    3    4    5    6    7    8    9   10   11   12   13
    // NOT: 763, 769, 774, 779, 783, 795, 813, 817, 819, 822, 827; 828; 829;
    // 751 - too low



    public static void main(String[] args) throws Exception {
        String pathToInputFile = "src/main/resources/day15/input.txt";
        List<String> inputLines = MyUtilities.getInputLines(pathToInputFile);
        prepareMap(inputLines);
        generateGraph(inputLines);

        System.out.println("\nPART I = " + partOne());
        System.out.println("PART II = " + partTwo());
    }
}
