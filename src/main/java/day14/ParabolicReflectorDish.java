package day14;

import utils.MyUtilities;

import java.util.ArrayList;
import java.util.List;

public class ParabolicReflectorDish {

    // rocks (O)
    // cube-shaped rocks (#) will stay in place
    // the empty spaces (.)

    private static int ROWS;
    private static int COLUMNS;
    private static int[][] IMAGE;

    private static void moveSingleRockToNorth(final int row, final int col) {
        int i = row - 1;
        for (; i >= 0; i--) {
            int current = IMAGE[i][col];
            if (current == (int) 'O') break;
            if (current == (int) '#') break;
            IMAGE[i + 1][col] = '.';
        }
        i++;
        IMAGE[i][col] = 'O';
    }

    private static void moveSingleRockToSouth(final int row, final int col) {
        int i = row + 1;
        for (; i < ROWS; i++) {
            int current = IMAGE[i][col];
            if (current == (int) 'O') break;
            if (current == (int) '#') break;
            IMAGE[i - 1][col] = '.';
        }
        i--;
        IMAGE[i][col] = 'O';
    }

    private static void moveSingleRockToWest(final int row, final int col) {
        int i = col - 1;
        for (; i >= 0; i--) {
            int current = IMAGE[row][i];
            if (current == (int) 'O') break;
            if (current == (int) '#') break;
            IMAGE[row][i + 1] = '.';
        }
        i++;
        IMAGE[row][i] = 'O';
    }

    private static void moveSingleRockToEast(final int row, final int col) {
        int i = col + 1;
        for (; i < COLUMNS; i++) {
            int current = IMAGE[row][i];
            if (current == (int) 'O') break;
            if (current == (int) '#') break;
            IMAGE[row][i - 1] = '.';
        }
        i--;
        IMAGE[row][i] = 'O';
    }


    private static int getSingleResult(final int i) {
        return ROWS - i;
    }

    private static void prepareImage(final List<String> inputLines) {
        ROWS = inputLines.size();
        COLUMNS = inputLines.get(0).length();
        IMAGE = new int[ROWS][COLUMNS];
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                IMAGE[i][j] = inputLines.get(i).charAt(j);
            }
        }
    }


    private static void printImage() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                System.out.print((char) IMAGE[i][j] + " ");
            }
            System.out.println();
        }
    }

    private static String goOneCycle() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                int current = IMAGE[i][j];
                if (current == (int) 'O') {
                    moveSingleRockToNorth(i, j);
                }
            }
        }
//        System.out.println("AFTER NORTH");
//        printImage();


        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                int current = IMAGE[i][j];
                if (current == (int) 'O') {
                    moveSingleRockToWest(i, j);
                }
            }
        }
//        System.out.println("AFTER WEST");
//        printImage();


        for (int i = ROWS - 1; i >= 0; i--) {
            for (int j = 0; j < COLUMNS; j++) {
                int current = IMAGE[i][j];
                if (current == (int) 'O') {
                    moveSingleRockToSouth(i, j);
                }
            }
        }
//        System.out.println("AFTER SOUTH");
//        printImage();


        for (int i = 0; i < ROWS; i++) {
            for (int j = COLUMNS - 1; j >= 0; j--) {
                int current = IMAGE[i][j];
                if (current == (int) 'O') {
                    moveSingleRockToEast(i, j);
                }
            }
        }
//        System.out.println("AFTER EAST");
//        printImage();

        return null;
    }

    private static int calculateResult() {
        int counter = 0;
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                if (IMAGE[i][j] == 'O') {
                    int result = getSingleResult(i);
//                    System.out.println("result = " + result);
                    counter += result;
                }
            }
        }
        return counter;
    }

    private static boolean checkIfIsSameStage(final int[][] imageStart) {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                if (imageStart[i][j] != IMAGE[i][j]) return false;
            }
        }
        return true;
    }

    private static int partTwo() {
        int[][] imageStart = new int[ROWS][COLUMNS];
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                imageStart[i][j] = IMAGE[i][j];
            }
        }
        List<Integer> resultList = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            goOneCycle();
//            boolean isSameStage = checkIfIsSameStage(imageStart);
//            if(isSameStage){
//                System.out.println("Is the same. i = " + i);
//            }
            int result = calculateResult();
            resultList.add(result);
//            System.out.println(i + " -> result = " + result);
        }

        // 96001 too low
        // 96020 too high
        // 96014 too high
        // 96003 - OK
        resultList.stream().skip(resultList.size() - 10).forEach(res -> System.out.println(res));
        return calculateResult();
    }

    private static int partOne(final List<String> inputLines) {

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                int current = IMAGE[i][j];
                if (current == (int) 'O') {
                    moveSingleRockToNorth(i, j);
                }
            }
        }
        return calculateResult();
    }


    public static void main(String[] args) throws Exception {
        String pathToInputFile = "src/main/resources/day14/input.txt";
        List<String> inputLines = MyUtilities.getInputLines(pathToInputFile);
        prepareImage(inputLines);

        System.out.println("\nPART I = " + partOne(inputLines));
        System.out.println("PART II = " + partTwo());
    }
}
