package day16;

import utils.MyUtilities;

import java.util.*;

public class TheFloorWillBeLava {
    // empty space (.)
    // mirrors (/ and \)
    // and splitters (| and -)
    // The beam enters in the top-left corner from the left and heading to the right
    // a '/' mirror would continue upward in the mirror's column
    // a '\' mirror would continue downward from the mirror's column

    // If the beam encounters the pointy end of a splitter (| or -), spiczasty
    // the beam passes through the splitter as if the splitter were empty space. a rightward-moving beam
    // that encounters a - splitter would continue in the same direction.

    // If the beam encounters the flat side of a splitter (| or -),
    // the beam is split into two beams going in each of the two directions the splitter's pointy ends are pointing.
    // For instance, a rightward-moving beam that encounters a | splitter would split into two beams:
    // one that continues upward from the splitter's column and one that continues downward from the splitter's column.

    // A tile is energized if that tile has at least one beam pass through it, reflect in it, or split in it.

    static class StartPointWithDirections {
        private int row;
        private int column;
        private String direction;

        StartPointWithDirections(final int row, final int column, final String direction) {
            this.row = row;
            this.column = column;
            this.direction = direction;
        }

        int getRow() {
            return row;
        }

        int getColumn() {
            return column;
        }

        String getDirection() {
            return direction;
        }

        void setRow(final int row) {
            this.row = row;
        }

        void setColumn(final int column) {
            this.column = column;
        }

        void setDirection(final String direction) {
            this.direction = direction;
        }

        @Override
        public String toString() {
            return "StartPointWithDirections{" +
                    "row=" + row +
                    ", column=" + column +
                    ", firstDirection='" + direction + '\'' +
                    '}';
        }
    }

    private static int rows;
    private static int columns;
    private static char[][] contraption;
    private static boolean[][] visited;
    private static String RIGHT = "right";
    private static String LEFT = "left";
    private static String UP = "up";
    private static String DOWN = "down";

    private static boolean isInside(final int currentRow, final int currentCol) {
        return currentRow >= 0 && currentRow < rows && currentCol >= 0 && currentCol < columns;
    }

    private static List<StartPointWithDirections> getStartingPointsFromPoint(final char currentChar, final String currentDirection,
                                                                             final int currentRow, final int currentCol) {
        List<StartPointWithDirections> result = new ArrayList<>();
        if (currentChar == (char) 92) {
            if (currentDirection.equals(UP)) result.add(new StartPointWithDirections(currentRow, currentCol, LEFT));
            if (currentDirection.equals(DOWN)) result.add(new StartPointWithDirections(currentRow, currentCol, RIGHT));
            if (currentDirection.equals(RIGHT)) result.add(new StartPointWithDirections(currentRow, currentCol, DOWN));
            if (currentDirection.equals(LEFT)) result.add(new StartPointWithDirections(currentRow, currentCol, UP));
        } else if (currentChar == '/') {
            if (currentDirection.equals(UP)) result.add(new StartPointWithDirections(currentRow, currentCol, RIGHT));
            if (currentDirection.equals(DOWN)) result.add(new StartPointWithDirections(currentRow, currentCol, LEFT));
            if (currentDirection.equals(RIGHT)) result.add(new StartPointWithDirections(currentRow, currentCol, UP));
            if (currentDirection.equals(LEFT)) result.add(new StartPointWithDirections(currentRow, currentCol, DOWN));

        } else if (currentChar == '-') {
            if (currentDirection.equals(UP) || currentDirection.equals(DOWN)) {
                result.add(new StartPointWithDirections(currentRow, currentCol, RIGHT));
                result.add(new StartPointWithDirections(currentRow, currentCol, LEFT));
            }
        } else if (currentChar == '|') {
            if (currentDirection.equals(LEFT) || currentDirection.equals(RIGHT)) {
                result.add(new StartPointWithDirections(currentRow, currentCol, UP));
                result.add(new StartPointWithDirections(currentRow, currentCol, DOWN));
            }
        }

        return result;
    }

    private static boolean checkIfGoFurther(final String currentDirection, final char currentChar) {
        if (currentChar == (char) 92) {
            return false;
        } else if (currentChar == '/') {
            return false;
        } else if (currentChar == '-') {
            if (currentDirection.equals(RIGHT) || currentDirection.equals(LEFT)) {
                return true;
            }
        } else if (currentChar == '|') {
            if (currentDirection.equals(UP) || currentDirection.equals(DOWN)) {
                return true;
            }
        }
        return false;
    }

    private static List<StartPointWithDirections> goToNextObstacles(final StartPointWithDirections currentStartPoint) {
        List<StartPointWithDirections> listToAdd = new ArrayList<>();
//        char currentChar = contraption[currentStartPoint.getRow()][currentStartPoint.getColumn()];
        String currentDirection = currentStartPoint.getDirection();
        int currentRow = currentStartPoint.getRow();
        int currentCol = currentStartPoint.getColumn();

        while (true) {
            if (RIGHT.equals(currentDirection)) currentCol++;
            if (LEFT.equals(currentDirection)) currentCol--;
            if (UP.equals(currentDirection)) currentRow--;
            if (DOWN.equals(currentDirection)) currentRow++;

            if (!isInside(currentRow, currentCol)) break;

            visited[currentRow][currentCol] = true;

            if (contraption[currentRow][currentCol] != '.') {
//                System.out.println("contraption[currentRow][currentCol] = " + contraption[currentRow][currentCol]);

                char currentChar = contraption[currentRow][currentCol];
                boolean goFurther = checkIfGoFurther(currentDirection, currentChar);
                if (goFurther) continue;

                List<StartPointWithDirections> pointsToStart =
                        getStartingPointsFromPoint(currentChar, currentDirection, currentRow, currentCol);
                listToAdd.addAll(pointsToStart);
                break;
            }
        }


//        for (int i = 0; i < rows; i++) {
//            for (int j = 0; j < columns; j++) {
//                if (visited[i][j]) System.out.print("# ");
//                else System.out.print(". ");
//            }
//            System.out.println();
//        }


        return listToAdd;
    }

    private static void prepareContraptionTool(final List<String> inputLines) {
        rows = inputLines.size();
        columns = inputLines.get(0).length();
        contraption = new char[rows][columns];
        visited = new boolean[rows][columns];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                contraption[i][j] = inputLines.get(i).charAt(j);
            }
        }
    }

    private static void clearVisited() {
        visited = new boolean[rows][columns];
    }

    private static int partOne(StartPointWithDirections pointToStart) {
        int currentRow = pointToStart.getRow();
        int currentCol = pointToStart.getColumn();
        String currentDirection = pointToStart.getDirection();
        clearVisited();
        visited[currentRow][currentCol] = true;
//        char nextObstacle;
//        while (contraption[currentRow][currentCol] == '.') {
//            currentCol++;
//        }
//        nextObstacle = contraption[currentRow][currentCol];
//        System.out.println("currentRow = " + currentRow);
//        System.out.println("currentCol = " + currentCol);
//        System.out.println("nextObstacle = " + nextObstacle);


//        StartPointWithDirections firstStartPoint = new StartPointWithDirections(0, 0, RIGHT);
//        visited[0][0] = true;
        List<StartPointWithDirections> startPointsToAddAtStart = new ArrayList<>();
        if (contraption[currentRow][currentCol] != '.') {
            char currentChar = contraption[currentRow][currentCol];
            if (currentChar == (char) 92) {
                if (currentDirection.equals(UP))
                    startPointsToAddAtStart.add(new StartPointWithDirections(currentRow, currentCol, LEFT));
                if (currentDirection.equals(DOWN))
                    startPointsToAddAtStart.add(new StartPointWithDirections(currentRow, currentCol, RIGHT));
                if (currentDirection.equals(RIGHT))
                    startPointsToAddAtStart.add(new StartPointWithDirections(currentRow, currentCol, DOWN));
                if (currentDirection.equals(LEFT))
                    startPointsToAddAtStart.add(new StartPointWithDirections(currentRow, currentCol, UP));
            } else if (currentChar == '/') {
                if (currentDirection.equals(UP))
                    startPointsToAddAtStart.add(new StartPointWithDirections(currentRow, currentCol, RIGHT));
                if (currentDirection.equals(DOWN))
                    startPointsToAddAtStart.add(new StartPointWithDirections(currentRow, currentCol, LEFT));
                if (currentDirection.equals(RIGHT))
                    startPointsToAddAtStart.add(new StartPointWithDirections(currentRow, currentCol, UP));
                if (currentDirection.equals(LEFT))
                    startPointsToAddAtStart.add(new StartPointWithDirections(currentRow, currentCol, DOWN));

            } else if (currentChar == '-') {
                if (currentDirection.equals(UP) || currentDirection.equals(DOWN)) {
                    startPointsToAddAtStart.add(new StartPointWithDirections(currentRow, currentCol, RIGHT));
                    startPointsToAddAtStart.add(new StartPointWithDirections(currentRow, currentCol, LEFT));
                }
            } else if (currentChar == '|') {
                if (currentDirection.equals(LEFT) || currentDirection.equals(RIGHT)) {
                    startPointsToAddAtStart.add(new StartPointWithDirections(currentRow, currentCol, UP));
                    startPointsToAddAtStart.add(new StartPointWithDirections(currentRow, currentCol, DOWN));
                }
            }

        }

        Queue<StartPointWithDirections> startPointsList = new LinkedList<>();
        if (startPointsToAddAtStart.isEmpty()) startPointsList.add(pointToStart);
        else startPointsList.addAll(startPointsToAddAtStart);

        int iteration = 0;
        while (!startPointsList.isEmpty()) {
            iteration++;
            StartPointWithDirections currentStartPoint = startPointsList.poll();
            List<StartPointWithDirections> startPointsToAddToList = goToNextObstacles(currentStartPoint);
//            System.out.println("startPointsToAddToList = " + startPointsToAddToList);
            startPointsList.addAll(startPointsToAddToList);

            int counter = 0;
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    if (visited[i][j]) counter++;
                }
            }

//            System.out.println("iteration = " + iteration);
//            System.out.println("counter = " + counter);
            if (iteration >= 500000) break;
        }

        int counter = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (visited[i][j]) counter++;
            }
        }

        return counter;
    } // 7562 - ok

    private static int partTwo() {
        System.out.println("PART II : \n");
        List<Integer> resultsList = new ArrayList<>();
        int counter = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {

                if ((i >= 1) && (i <= rows - 2) && (j >= 1) && (j <= columns - 2)) continue;

                int result = -1;
                if (i == 0 && j == 0) { // left top
                    result = partOne(new StartPointWithDirections(i, j, DOWN));
                    resultsList.add(result);
                    System.out.println("result = " + result);
                    counter++;
                    result = partOne(new StartPointWithDirections(i, j, RIGHT));
                    resultsList.add(result);
                }

                if (i == 0 && j == columns - 1) { // right top
                    result = partOne(new StartPointWithDirections(i, j, DOWN));
                    resultsList.add(result);
                    System.out.println("result = " + result);
                    counter++;
                    result = partOne(new StartPointWithDirections(i, j, LEFT));
                    resultsList.add(result);
                }

                if (i == rows - 1 && j == 0) { // left bottom
                    result = partOne(new StartPointWithDirections(i, j, UP));
                    resultsList.add(result);
                    System.out.println("result = " + result);
                    counter++;
                    result = partOne(new StartPointWithDirections(i, j, RIGHT));
                    resultsList.add(result);
                }

                if (i == rows - 1 && j == columns - 1) { // right bottom
                    result = partOne(new StartPointWithDirections(i, j, UP));
                    resultsList.add(result);
                    System.out.println("result = " + result);
                    counter++;
                    result = partOne(new StartPointWithDirections(i, j, LEFT));
                    resultsList.add(result);
                }

                if (i == 0 && (j > 0 && j < columns - 1)) { // top
                    result = partOne(new StartPointWithDirections(i, j, DOWN));
                    resultsList.add(result);
                }

                if (i == rows - 1 && (j > 0 && j < columns - 1)) { // bottom
                    result = partOne(new StartPointWithDirections(i, j, UP));
                    resultsList.add(result);
                }

                if ((i > 0 && i < rows - 1) && (j == 0)) { // left
                    result = partOne(new StartPointWithDirections(i, j, RIGHT));
                    resultsList.add(result);
                }

                if ((i > 0 && i < rows - 1) && (j == columns - 1)) { // right
                    result = partOne(new StartPointWithDirections(i, j, LEFT));
                    resultsList.add(result);
                }
                System.out.println("counter = " + counter);
                System.out.println("result = " + result);
                counter++;
            }
        }

        System.out.println("resultsList = " + resultsList);
        Collections.sort(resultsList);
        return resultsList.get(resultsList.size() - 1);
    }


    public static void main(String[] args) throws Exception {
        String pathToInputFile = "src/main/resources/day16/input.txt";
        List<String> inputLines = MyUtilities.getInputLines(pathToInputFile);
        prepareContraptionTool(inputLines);

        System.out.println("\nPART I = " + partOne(new StartPointWithDirections(0, 0, RIGHT)));
        System.out.println("PART II = " + partTwo());
    }
}
