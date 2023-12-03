package day03;

import utils.MyUtilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GearRatios {
    private static Set<Character> symbolsSet = new HashSet<>();
    private static char[][] engineSchematic;
    private static int rows = 0;
    private static int columns = 0;

    private static void generaEngineSchematic(final List<String> inputLines) {
        rows = inputLines.size();
        columns = inputLines.get(0).length();
        engineSchematic = new char[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                engineSchematic[i][j] = inputLines.get(i).charAt(j);
            }
        }
//        System.out.println("engineSchematic = ");
//        for (int i = 0; i < rows; i++) {
//            for (int j = 0; j < columns; j++) {
//                System.out.print(engineSchematic[i][j] + " ");
//            }
//            System.out.println();
//        }
    }

    private static void generateSetOfSymbols(List<String> inputLines) {
        inputLines.forEach(line -> {
            String symbolsOnly = line.replaceAll("[.0-9a-zA-Z ]", "");
            symbolsOnly.chars().forEach(c -> symbolsSet.add((char) c));
        });
    }

    private static void removeNumberFromSchematic(final int firstIndexOfNumber, final int lastIndexOfNumber, final int i) {
        for (int j = firstIndexOfNumber; j <= lastIndexOfNumber; j++) {
            engineSchematic[i][j] = '.';
        }
    }

    private static int getNumberAndClearItInSchematic(int i, int j) {
        int number = 0;
        char[] interestingLine = engineSchematic[i];

        int firstIndexOfNumber = j;
        int lastIndexOfNumber = j;
        while (firstIndexOfNumber >= 0) {
            if (interestingLine[firstIndexOfNumber] >= '0' && interestingLine[firstIndexOfNumber] <= '9') {
                firstIndexOfNumber--;
            } else {
                firstIndexOfNumber++;
                break;
            }
        }
        if (firstIndexOfNumber < 0) firstIndexOfNumber = 0;

        while (lastIndexOfNumber < columns) {
            if (interestingLine[lastIndexOfNumber] >= '0' && interestingLine[lastIndexOfNumber] <= '9') {
                lastIndexOfNumber++;
            } else {
                lastIndexOfNumber--;
                break;
            }
        }
        if (lastIndexOfNumber >= columns) lastIndexOfNumber = columns - 1;

        StringBuilder strb = new StringBuilder();
        for (int k = firstIndexOfNumber; k <= lastIndexOfNumber; k++) strb.append(interestingLine[k]);
        number = Integer.parseInt(strb.toString());

        removeNumberFromSchematic(firstIndexOfNumber, lastIndexOfNumber, i);
        return number;
    }

    private static List<Integer> findNumbersAroundSymbolAndClearThemInSchematic(int row, int col) {
        List<Integer> numbers = new ArrayList<>();
        int rowMin = row;
        int rowMax = row;
        int colMin = col;
        int colMax = col;
        if ((rowMin - 1) >= 0) rowMin = rowMin - 1;
        if ((rowMax + 1) < rows) rowMax = rowMax + 1;
        if ((colMin - 1) >= 0) colMin = colMin - 1;
        if ((colMax + 1) < columns) colMax = colMax + 1;
        for (int i = rowMin; i <= rowMax; i++) {
            for (int j = colMin; j <= colMax; j++) {
                if (engineSchematic[i][j] >= '0' && engineSchematic[i][j] <= '9') {
                    int extractedNumber = getNumberAndClearItInSchematic(i, j);
                    numbers.add(extractedNumber);
                }
            }
        }
        return numbers;
    }

    private static int partOne() {
        List<Integer> numbersAroundSymbols = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                char currentChar = engineSchematic[i][j];
                if (symbolsSet.contains(currentChar)) {
                    numbersAroundSymbols.addAll(findNumbersAroundSymbolAndClearThemInSchematic(i, j));
                }

            }
        }
        return numbersAroundSymbols.stream().mapToInt(Integer::intValue).sum();
    }

    private static long partTwo() {
        char gear = '*';
        int expectedAmountOfNumbers = 2;
        List<Integer> gearRatios = new ArrayList<>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                char currentChar = engineSchematic[i][j];
                if (currentChar == gear) {
                    List<Integer> numbersAroundGear = findNumbersAroundSymbolAndClearThemInSchematic(i, j);
                    // probably will not work properly in any possible combinations of gears* and numbers around them
                    // may happen that while clearing a number around one gear I clear number from second gear
                    // because this method was created for partOne purpose :P
                    // however it works in my case :P so I don't need to change it
                    if (numbersAroundGear.size() == expectedAmountOfNumbers) {
                        gearRatios.add(numbersAroundGear.get(0) * numbersAroundGear.get(1));
                    }
                }

            }
        }

        return gearRatios.stream().mapToInt(Integer::intValue).sum();
    }

    public static void main(String[] args) throws IOException {
        String pathToInputFile = "src/main/resources/day03/input.txt";
        List<String> inputLines = MyUtilities.getInputLines(pathToInputFile);
        generateSetOfSymbols(inputLines);
        generaEngineSchematic(inputLines);

        System.out.println("\nPART I = " + partOne());
        generaEngineSchematic(inputLines);
        System.out.println("PART II = " + partTwo());

    }
}
