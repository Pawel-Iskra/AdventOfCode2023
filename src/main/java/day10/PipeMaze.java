package day10;

import utils.MyUtilities;

import java.util.*;

public class PipeMaze {
//    | is a vertical pipe connecting north and south. N -> S
//    - is a horizontal pipe connecting east and west. E -> W
//    L is a 90-degree bend connecting north and east. N -> E
//    J is a 90-degree bend connecting north and west. N -> W
//    7 is a 90-degree bend connecting south and west. S -> W
//    F is a 90-degree bend connecting south and east. S -> E
//    . is ground; there is no pipe in this tile.
//    S is the starting position of the animal; there is a pipe on this tile, but your sketch doesn't show what shape the pipe has.

    private static StringBuilder STRB;
    private static Graph MY_GRAPH;

    static class Graph {
        private int positionS;
        private int vertices;
        private List<Integer>[] adjacencyList;
        private boolean[] visited;
        private int counter = 0;
        private int edges;
        private List<DirectedEdge>[] directedEdgesList;
        private int[] numOfSteps = new int[vertices];
        public Graph(int vertices) {
            this.vertices = vertices;
            this.visited = new boolean[vertices];
            this.adjacencyList = (List<Integer>[]) new List[vertices];
            for (int i = 0; i < vertices; i++) {
                adjacencyList[i] = new ArrayList<Integer>();
            }
            this.directedEdgesList = new List[vertices];
            for (int i = 0; i < vertices; i++) {
                directedEdgesList[i] = new ArrayList<>();
            }
        }

        int getPositionS() {
            return positionS;
        }

        void setPositionS(final int positionS) {
            this.positionS = positionS;
        }

        public int getVertices() {
            return vertices;
        }

        public void addEdge(int v, int w) {
            adjacencyList[v].add(w);
        }

        public Iterable<Integer> getAdjacencyList(int v) {
            return adjacencyList[v];
        }

        public int getEdges() {
            return edges;
        }

        public List<DirectedEdge>[] getDirectedEdgesList() {
            return directedEdgesList;
        }

        public void addEdge(DirectedEdge directedEdge) {
            directedEdgesList[directedEdge.getFrom()].add(directedEdge);
            edges++;
        }

        public List<DirectedEdge> getDirectedEdgesListFromNode(int vertex) {
            return directedEdgesList[vertex];
        }


        public void clearVisited() {
            STRB = new StringBuilder();
            counter = 0;
            this.visited = new boolean[vertices];
        }

        public String printGraph() {
            StringBuilder strb = new StringBuilder();
            strb.append("[ ");
            for (int i = 0; i < adjacencyList.length; i++) {
                strb.append("[");
                for (int j = 0; j < adjacencyList[i].size(); j++) {
                    strb.append(adjacencyList[i].get(j) + 1);
                    if (j < adjacencyList[i].size() - 1) strb.append(" ");
                }
                strb.append("], ");
            }
            strb.delete(strb.length() - 2, strb.length());
            strb.append(" ]");
            return strb.toString();
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
        private Graph myGraph;
        private int vertices;
        private long[] shortestPaths;
        private int[] previous;
        private boolean[] visited;

        public DijkstraShortestPath(int start, Graph myGraph) {
            this.start = start;
            this.myGraph = myGraph;
            this.vertices = myGraph.getVertices();
            this.visited = new boolean[vertices];
            this.shortestPaths = new long[vertices];
            this.previous = new int[vertices];
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

            while (!currentDistancesQueue.isEmpty()) {
                visited[start] = true;
                start = currentDistancesQueue.poll().getTo();

                List<DirectedEdge> directedEdgesFromNode = myGraph.getDirectedEdgesListFromNode(start);
                long currDistToSrc = shortestPaths[start];

                //if... -> update shortestPaths for neighbors, update previous
                int size = directedEdgesFromNode.size();
                for (int i = 0; i < size; i++) {
                    DirectedEdge current = directedEdgesFromNode.get(i);
//                    System.out.println("current = " + current);
                    long calcDist = currDistToSrc + current.getWeight();
                    int to = current.getTo();
                    if (calcDist < shortestPaths[to]) {
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


    private static void generateGraph(final List<String> inputLines) {
        int height = inputLines.size();
        int width = inputLines.get(0).length();

        MY_GRAPH = new Graph(height * width);
        int vertices = MY_GRAPH.getVertices();
        for (int i = 0; i < height; i++) {
            char[] lineCharArray = inputLines.get(i).toCharArray();
            for (int j = 0; j < width; j++) {
                int currentVertex = width * i + j;

                switch (lineCharArray[j]) {

                    case 'F' -> {
                        if (currentVertex + 1 < (i + 1) * width) {
                            MY_GRAPH.addEdge(new DirectedEdge(currentVertex, currentVertex + 1, 1));
                        }
                        if (currentVertex + width < vertices)
                            MY_GRAPH.addEdge(new DirectedEdge(currentVertex, currentVertex + width, 1));

                        if (currentVertex + 1 < (i + 1) * width) {
                            char symbol = lineCharArray[j + 1];
                            if (symbol == '-' || symbol == '7' || symbol == 'J' || symbol == 'S') {
                                MY_GRAPH.addEdge(new DirectedEdge(currentVertex + 1, currentVertex, 1));
                            }
                        }

                        if (currentVertex + width < vertices) {
                            char symbol = inputLines.get(i + 1).charAt(j);
                            if (symbol == '|' || symbol == 'L' || symbol == 'J' || symbol == 'S') {
                                MY_GRAPH.addEdge(new DirectedEdge(currentVertex + width, currentVertex, 1));
                            }
                        }
                    }
                    case '7' -> {
                        if (currentVertex - 1 >= 0)
                            MY_GRAPH.addEdge(new DirectedEdge(currentVertex, currentVertex - 1, 1));
                        if (currentVertex + width < vertices)
                            MY_GRAPH.addEdge(new DirectedEdge(currentVertex, currentVertex + width, 1));

                        if (j - 1 >= 0) {
                            char symbol = lineCharArray[j - 1];
                            if (symbol == '-' || symbol == 'L' || symbol == 'F' || symbol == 'S') {
                                MY_GRAPH.addEdge(new DirectedEdge(currentVertex - 1, currentVertex, 1));
                            }
                        }
                        if (currentVertex + width < vertices) {
                            char symbol = inputLines.get(i + 1).charAt(j);
                            if (symbol == '|' || symbol == 'L' || symbol == 'J' || symbol == 'S') {
                                MY_GRAPH.addEdge(new DirectedEdge(currentVertex + width, currentVertex, 1));
                            }
                        }
                    }
                    case 'L' -> {
                        if (currentVertex - width >= 0)
                            MY_GRAPH.addEdge(new DirectedEdge(currentVertex, currentVertex - width, 1));
                        if (currentVertex + 1 < (i + 1) * width)
                            MY_GRAPH.addEdge(new DirectedEdge(currentVertex, currentVertex + 1, 1));

                        if (currentVertex - width >= 0) {
                            char symbol = inputLines.get(i - 1).charAt(j);
                            if (symbol == '|' || symbol == '7' || symbol == 'F' || symbol == 'S') {
                                MY_GRAPH.addEdge(new DirectedEdge(currentVertex - width, currentVertex, 1));
                            }
                        }


                        if (currentVertex + 1 < (i + 1) * width) {
                            char symbol = lineCharArray[j + 1];
                            if (symbol == '-' || symbol == '7' || symbol == 'J' || symbol == 'S') {
                                MY_GRAPH.addEdge(new DirectedEdge(currentVertex + 1, currentVertex, 1));
                            }
                        }
                    }
                    case 'J' -> {
                        if (currentVertex - 1 >= i * width)
                            MY_GRAPH.addEdge(new DirectedEdge(currentVertex, currentVertex - 1, 1));
                        if (currentVertex - width >= 0)
                            MY_GRAPH.addEdge(new DirectedEdge(currentVertex, currentVertex - width, 1));

                        if (currentVertex - 1 >= i * width) {
                            char symbol = lineCharArray[j - 1];
                            if (symbol == '-' || symbol == 'L' || symbol == 'F' || symbol == 'S') {
                                MY_GRAPH.addEdge(new DirectedEdge(currentVertex - 1, currentVertex, 1));
                            }
                        }

                        if (currentVertex - width >= 0) {
                            char symbol = inputLines.get(i - 1).charAt(j);
                            if (symbol == '|' || symbol == '7' || symbol == 'F' || symbol == 'S') {
                                MY_GRAPH.addEdge(new DirectedEdge(currentVertex - width, currentVertex, 1));
                            }
                        }
                    }
                    case '-' -> {
                        if (currentVertex + 1 <= (i + 1) * width)
                            MY_GRAPH.addEdge(new DirectedEdge(currentVertex, currentVertex + 1, 1));
                        if (currentVertex - 1 >= i * width)
                            MY_GRAPH.addEdge(new DirectedEdge(currentVertex, currentVertex - 1, 1));

                        if (j + 1 < width) {
                            char symbol = lineCharArray[j + 1];
                            if (symbol == '-' || symbol == '7' || symbol == 'J' || symbol == 'S') {
                                MY_GRAPH.addEdge(new DirectedEdge(currentVertex + 1, currentVertex, 1));
                            }
                        }
                        if (currentVertex - 1 >= i * width) {
                            char symbol = lineCharArray[j - 1];
                            if (symbol == '-' || symbol == 'L' || symbol == 'F' || symbol == 'S') {
                                MY_GRAPH.addEdge(new DirectedEdge(currentVertex - 1, currentVertex, 1));
                            }
                        }
                    }
                    case '|' -> {
                        if (currentVertex - width >= 0)
                            MY_GRAPH.addEdge(new DirectedEdge(currentVertex, currentVertex - width, 1));
                        if (currentVertex + width < vertices)
                            MY_GRAPH.addEdge(new DirectedEdge(currentVertex, currentVertex + width, 1));

                        if (currentVertex - width >= 0) {
                            char symbol = inputLines.get(i - 1).charAt(j);
                            if (symbol == '|' || symbol == '7' || symbol == 'F' || symbol == 'S') {
                                MY_GRAPH.addEdge(new DirectedEdge(currentVertex - width, currentVertex, 1));
                            }
                        }
                        if (currentVertex + width < vertices) {
                            char symbol = inputLines.get(i + 1).charAt(j);
                            if (symbol == '|' || symbol == 'L' || symbol == 'J' || symbol == 'S') {
                                MY_GRAPH.addEdge(new DirectedEdge(currentVertex + width, currentVertex, 1));
                            }
                        }
                    }
                    case 'S' -> {
                        MY_GRAPH.setPositionS(currentVertex);
                    }

                }

            }

        }

        System.out.println("MY_GRAHP:");
        List<Integer>[] adjacencyList = MY_GRAPH.adjacencyList;
        int size = adjacencyList.length;
        for (int i = 0; i < size; i++) {
            List<DirectedEdge> directedEdges = MY_GRAPH.directedEdgesList[i];
            System.out.println(i + " -> " + directedEdges);
        }
//
//        System.out.println("MY_GRAPH.printGraph() = " + MY_GRAPH.printGraph());
    }


    private static long partOne() {

        int positionOfS = MY_GRAPH.getPositionS();
        System.out.println("positionOfS = " + positionOfS);


        int vertices = MY_GRAPH.getVertices();
        DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(positionOfS, MY_GRAPH);
        dijkstraShortestPath.runDijkstraAlgorithm();
        long[] shortestPaths = dijkstraShortestPath.getShortestPaths();
        System.out.println("shortestPaths = " + Arrays.toString(shortestPaths));

        long longestPath = 0;
        for (int i = 0; i < vertices; i++) {
            if (shortestPaths[i] != Long.MAX_VALUE && shortestPaths[i] > longestPath) longestPath = shortestPaths[i];
        }
        System.out.println("longestPath = " + longestPath);

        return longestPath;
    }


    private static String partTwo() {

        return null;
    }

    public static void main(String[] args) throws Exception {
        String pathToInputFile = "src/main/resources/day10/input.txt";
        List<String> inputLines = MyUtilities.getInputLines(pathToInputFile);
//        inputLines.forEach(line -> System.out.println(line));
        generateGraph(inputLines);

        System.out.println("\nPART I = " + partOne());
        System.out.println("PART II = " + partTwo());
    }
}
