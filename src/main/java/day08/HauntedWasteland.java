package day08;

import utils.MyUtilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HauntedWasteland {

    private static String COMMAND_LINE = "";
    private static List<Node> NODE_LIST = new ArrayList<>();
    private static Map<String, Node> NODE_MAP = new HashMap();

    private static class Node {
        private String start;
        private String left;
        private String right;

        public Node(String start, String left, String right) {
            this.start = start;
            this.left = left;
            this.right = right;
        }

        public String getStart() {
            return start;
        }

        public String getLeft() {
            return left;
        }

        public String getRight() {
            return right;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "start='" + start + '\'' +
                    ", left='" + left + '\'' +
                    ", right='" + right + '\'' +
                    '}';
        }
    }


    private static void generateNodes(List<String> inputLines) {
        COMMAND_LINE = inputLines.get(0).strip();
        inputLines.stream()
                .skip(2)
                .forEach(line -> {
                    String start = line.substring(0, line.indexOf("=") - 1).strip();
                    String left = line.substring(line.indexOf("(") + 1, line.indexOf(",")).strip();
                    String right = line.substring(line.indexOf(",") + 2, line.indexOf(")")).strip();
                    Node currentNode = new Node(start, left, right);
                    NODE_LIST.add(currentNode);
                    NODE_MAP.put(start, currentNode);
                });
    }

    private static int goThroughNodesPartOne(String start, String end) {
        int counter = 0;
        int numOfCommands = COMMAND_LINE.length();
        char[] commands = new char[numOfCommands];
        for (int i = 0; i < numOfCommands; i++) {
            commands[i] = COMMAND_LINE.charAt(i);
        }

        Node current = NODE_MAP.get(start);
        while (!current.getStart().equals(end)) {
            char currentCommand = commands[counter % numOfCommands];
            if (currentCommand == 'L') {
                String currentStart = current.getLeft();
                current = NODE_MAP.get(currentStart);
            }
            if (currentCommand == 'R') {
                String currentStart = current.getRight();
                current = NODE_MAP.get(currentStart);
            }
            counter++;
        }

        return counter;
    }

    private static long getLeastCommonMultiple(long a, long b) {
        long rest = 0;
        long big = a;
        long small = b;
        if (small > big) {
            big = b;
            small = a;
        }
        while ((rest = Long.remainderUnsigned(big, small)) != 0) {
            big = small;
            small = rest;
        }
        return (a / small * b);
    }

    private static int goThroughNodesPartTwo(String start) {
        int counter = 0;
        int numOfCommands = COMMAND_LINE.length();
        char[] commands = new char[numOfCommands];
        for (int i = 0; i < numOfCommands; i++) {
            commands[i] = COMMAND_LINE.charAt(i);
        }

        Node current = NODE_MAP.get(start);
        while (current.getStart().charAt(2) != 'Z') {
            char currentCommand = commands[counter % numOfCommands];
            if (currentCommand == 'L') {
                String currentStart = current.getLeft();
                current = NODE_MAP.get(currentStart);
            }
            if (currentCommand == 'R') {
                String currentStart = current.getRight();
                current = NODE_MAP.get(currentStart);
            }
            counter++;
        }

        return counter;
    }


    private static long partTwo() {
        // start - ending with 'A'
        // end - ending with 'Z'
        long result = 1;
        List<Integer> resultList = new ArrayList<>();
        for (Node currentNodeFromList : NODE_LIST) {
            if (currentNodeFromList.getStart().charAt(2) == 'A') {
                int currentResult = goThroughNodesPartTwo(currentNodeFromList.getStart());
                resultList.add(currentResult);
            }
        }

        for (int currentNumOfSteps : resultList) {
            result = getLeastCommonMultiple(result, currentNumOfSteps);
        }
        return result;
    }

    private static int partOne() {
        return goThroughNodesPartOne("AAA", "ZZZ");
    }


    public static void main(String[] args) throws IOException {
        String pathToInputFile = "src/main/resources/day08/input.txt";
        List<String> inputLines = MyUtilities.getInputLines(pathToInputFile);
        generateNodes(inputLines);


        System.out.println("PART I = " + partOne());
        System.out.println("PART II = " + partTwo());
    }
}
