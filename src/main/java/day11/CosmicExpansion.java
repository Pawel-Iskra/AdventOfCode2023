package day11;

import utils.MyUtilities;

import java.util.*;

public class CosmicExpansion {

    //# galaxy
    // . empty space
    // sum of the lengths of the shortest path between every pair of galaxies

    private static char ONE_GALAXY = '#';
    private static String ONE_GALAXY_STR = "#";
    private static char EMPTY_SPACE = '.';
    private static char[][] GALAXY;
    private static List<String> GALAXY_STR_LIST = new ArrayList<>();
    private static Graph MY_GRAPH;
    private static StringBuilder STRB;


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
            return "DirectedEdge{" +
                    "from=" + from +
                    ", to=" + to +
                    ", weight=" + weight +
                    '}';
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

    static class Graph {
        private int vertices;
        private List<Integer>[] adjacencyList;
        private boolean[] visited;
        private int edges;
        private List<DirectedEdge>[] directedEdgesList;

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
            this.visited = new boolean[vertices];
        }

        public void printGraph() {

            for (int i = 0; i < directedEdgesList.length; i++) {
                System.out.println(directedEdgesList[i]);

            }

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

    private static void generateExpandedGalaxy(List<String> inputLines) {
        List<String> galaxyImage = new ArrayList<>(inputLines);
        int size = galaxyImage.size();

        for (int i = 0; i < size; i++) {
            String currentLine = galaxyImage.get(i);
            if (!currentLine.contains(ONE_GALAXY_STR)) {
                galaxyImage.add(i, currentLine);
                i++;
                size++;
            }
        }
        size = galaxyImage.size();
        int length = galaxyImage.get(0).length();
        for (int i = 0; i < length; i++) {
            Set<Character> currentColumnCharSet = new HashSet<>();
            for (int j = 0; j < size; j++) {
                currentColumnCharSet.add(galaxyImage.get(j).charAt(i));
            }
            if (currentColumnCharSet.size() == 1) {
                for (int j = 0; j < size; j++) {
                    StringBuilder currentLineStrb = new StringBuilder(galaxyImage.get(j));
                    currentLineStrb.insert(i, EMPTY_SPACE);
                    galaxyImage.remove(j);
                    galaxyImage.add(j, currentLineStrb.toString());
                }
                i++;
                length++;
            }
        }
        System.out.println("galaxyImage = ");
        int amount = galaxyImage.size();
        for (int i = 0; i < amount; i++) {
            System.out.println(galaxyImage.get(i));
        }

        int rows = galaxyImage.size();
        int columns = galaxyImage.get(0).length();
        GALAXY = new char[rows][columns];
        for (int i = 0; i < rows; i++) {
            String currentLine = galaxyImage.get(i);
            GALAXY_STR_LIST.add(currentLine);
            for (int j = 0; j < columns; j++) {
                GALAXY[i][j] = currentLine.charAt(j);
            }
        }

        System.out.println("GALAXY CHAR:");
        for (int i = 0; i < rows; i++) {
            char[] current = GALAXY[i];
            for (int j = 0; j < columns; j++) {
                System.out.print(GALAXY[i][j]);
            }
            System.out.println();
        }

        System.out.println("GALAXY STRING LIST");
        for (int i = 0; i < rows; i++) {
            System.out.println(GALAXY_STR_LIST.get(i));
        }
    }

    private static void generateGraph() {
        int rows = GALAXY_STR_LIST.size();
        int columns = GALAXY_STR_LIST.get(0).length();
        int vertices = rows * columns;
        MY_GRAPH = new Graph(vertices);
        int weight = 1;

        for (int i = 0; i < vertices; i++) {
            int currentRow = i / columns;
            String currentMapLine = GALAXY_STR_LIST.get(currentRow);
            int currentColumn = i - currentRow * columns;

            int upperRow = currentRow - 1;
            if (upperRow >= 0) { // go up
                MY_GRAPH.addEdge(new DirectedEdge(i, (i - columns), weight));
            }
            int lowerRow = currentRow + 1;
            if (lowerRow < rows) { // go down
                MY_GRAPH.addEdge(new DirectedEdge(i, (i + columns), weight));
            }
            int rightColumn = currentColumn + 1;
            if (rightColumn < columns) { // go right
                MY_GRAPH.addEdge(new DirectedEdge(i, (i + 1), weight));

            }
            int leftColumn = currentColumn - 1;
            if (leftColumn >= 0) { // go left
                MY_GRAPH.addEdge(new DirectedEdge(i, (i - 1), weight));
            }
        }

        System.out.println("MY GRAPH:");
        MY_GRAPH.printGraph();

    }

    private static String partOne() {
//        List<Integer> results = new ArrayList<>();
        List<Integer> posOfGalaxies = new ArrayList<>();
        int size = GALAXY_STR_LIST.size();
        int length = GALAXY_STR_LIST.get(0).length();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < length; j++) {
                if (GALAXY[i][j] == ONE_GALAXY) posOfGalaxies.add(i * length + j);
            }
        }
        System.out.println("posOfGalaxies = " + posOfGalaxies);
        List<long[]> results = new ArrayList<>();
        int numOfStars = posOfGalaxies.size();
        for (int i = 0; i < numOfStars; i++) {
            DijkstraShortestPath dijkstraShortestPath = new DijkstraShortestPath(posOfGalaxies.get(i), MY_GRAPH);
            dijkstraShortestPath.runDijkstraAlgorithm();
            results.add(dijkstraShortestPath.getShortestPaths());
        }

        long sum = 0;
        for (int i = 0; i < numOfStars; i++) {
            long[] currentShortest = results.get(i);
            int currentStarPos = posOfGalaxies.get(i);
            System.out.println("currentStarPos = " + currentStarPos);
            System.out.println("currentShortest = " + Arrays.toString(currentShortest));

            for (int j = i + 1; j < numOfStars; j++) {
                sum += currentShortest[posOfGalaxies.get(j)];
            }
            System.out.println("sum = " + sum);
        }
        System.out.println("sum = " + sum);


        return null;
    }


    private static String partTwo() {

        return null;
    }


    public static void main(String[] args) throws Exception {
        String pathToInputFile = "src/main/resources/day11/input.txt";
        List<String> inputLines = MyUtilities.getInputLines(pathToInputFile);
        inputLines.forEach(line -> System.out.println(line));
        generateExpandedGalaxy(inputLines);
        generateGraph();


        System.out.println("\nPART I = " + partOne());
        System.out.println("PART II = " + partTwo());
    }
}
