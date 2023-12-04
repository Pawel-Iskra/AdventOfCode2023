package day04;

import utils.MyUtilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Scratchcards {


    private static List<List<Integer>> myNumbersListOfLists = new ArrayList<>();
    private static List<List<Integer>> winningNumbersListOfLists = new ArrayList<>();
    private static int amountOfCards = 0;

    private static void generateCardsBeforePlay(List<String> inputLines) {
        inputLines.forEach(line -> {
            String[] winningNumStringArray = line.substring(line.indexOf(":") + 1, line.indexOf("|")).strip().split(" ");
            String[] myNumbersStringArray = line.substring(line.indexOf("|") + 1).strip().split(" ");

            List<Integer> current = new ArrayList<>();
            final List<Integer> finalCurrent = current;
            Arrays.stream(winningNumStringArray).filter(number -> !number.isBlank()).forEach(num -> {
                finalCurrent.add(Integer.parseInt(num.strip()));
            });
            winningNumbersListOfLists.add(current);
            current = new ArrayList<>();
            final List<Integer> finalCurrent1 = current;
            Arrays.stream(myNumbersStringArray).filter(number -> !number.isBlank()).forEach(num -> {
                finalCurrent1.add(Integer.parseInt(num.strip()));
            });
            myNumbersListOfLists.add(finalCurrent1);
//            System.out.println("winningNumbersListOfLists = " + winningNumbersListOfLists);
//            System.out.println("myNumbersListOfLists = " + myNumbersListOfLists);
        });
        amountOfCards = winningNumbersListOfLists.size();
    }

    private static long partOne() {
        long result = 0;

        for (int i = 0; i < amountOfCards; i++) {
            List<Integer> currentWinnings = winningNumbersListOfLists.get(i);
            List<Integer> currentMyNums = myNumbersListOfLists.get(i);
//            System.out.println("currentWinnings = " + currentWinnings);
//            System.out.println("currentMyNums = " + currentMyNums);
            long currentResult = 0;
            for (Integer currentWinningNum : currentWinnings) {
                if (currentMyNums.contains(currentWinningNum)) {
                    if (currentResult == 0) currentResult = 1;
                    else currentResult *= 2;
                }
            }
//            System.out.println("currentResult = " + currentResult);
            result += currentResult;
        }
        return result;
    }

    private static int partTwo() {
        int[] numbersOfCardsInstances = new int[amountOfCards];
        for (int i = 0; i < amountOfCards; i++) {
            numbersOfCardsInstances[i] = 1;
        }

        for (int i = 0; i < amountOfCards; i++) {
            List<Integer> currentWinnings = winningNumbersListOfLists.get(i);
            List<Integer> currentMyNums = myNumbersListOfLists.get(i);
            int counter = 0;
            int currentCardInstances = numbersOfCardsInstances[i];
            for (Integer currentWinningNum : currentWinnings) {
                if (currentMyNums.contains(currentWinningNum)) {
                    counter++;
                }
            }

            for (int z = 0; z < currentCardInstances; z++) {
                for (int j = 0; j < counter; j++) {
                    numbersOfCardsInstances[i + 1 + j]++;
                }
            }

        }
//        for (int i = 0; i < amountOfCards; i++) {
//            System.out.println(i + " -> " + numbersOfCardsInstances[i]);
//        }
        return Arrays.stream(numbersOfCardsInstances).sum();
    }

    public static void main(String[] args) throws IOException {
        String pathToInputFile = "src/main/resources/day04/input.txt";
        List<String> inputLines = MyUtilities.getInputLines(pathToInputFile);
        generateCardsBeforePlay(inputLines);

        System.out.println("\nPART I = " + partOne());
        System.out.println("PART II = " + partTwo());
    }
}
