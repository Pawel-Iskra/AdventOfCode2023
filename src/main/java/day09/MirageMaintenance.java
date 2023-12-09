package day09;

import utils.MyUtilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MirageMaintenance {

    private static List<Integer> generateNextLine(final List<Integer> integers) {
        int size = integers.size();
        List<Integer> resultLine = new ArrayList<>();
        for (int i = 1; i < size; i++) {
            int difference = integers.get(i) - integers.get(i - 1);
            resultLine.add(difference);
        }
        return resultLine;
    }

    private static int generateFinalResultFromAllLists(final List<List<Integer>> listOfLists) {
        int size = listOfLists.size();
        int finalResult = 0;
        for (int i = 0; i < size; i++) {
            finalResult += listOfLists.get(i).get(listOfLists.get(i).size() - 1);
        }
        return finalResult;
    }

    private static long predictNextValueForAList(final List<Integer> numberList) {
        List<List<Integer>> listOfLists = new ArrayList<>();
        listOfLists.add(numberList);

        int size = listOfLists.size();
        while (!isAllZeros(listOfLists.get(size - 1))) {
            List<Integer> nextLine = generateNextLine(listOfLists.get(size - 1));
            listOfLists.add(nextLine);
            size = listOfLists.size();
        }
        return generateFinalResultFromAllLists(listOfLists);
    }

    private static boolean isAllZeros(final List<Integer> integers) {
        return integers.stream().noneMatch(num -> num != 0);
    }

    private static long partOne(List<String> inputLines) throws Exception {
        List<Long> results = new ArrayList<>();
        inputLines.forEach(line -> {
            String[] numbersStringArray = line.strip().split(" ");
            List<Integer> numberList = Arrays.stream(numbersStringArray).map(Integer::parseInt).toList();
            long currentResult = predictNextValueForAList(numberList);
            results.add(currentResult);
        });
        System.out.println("results = " + results);
        return results.stream().mapToLong(Long::longValue).sum();
    }

    private static long generateFinalResultFromAllListsPartTwo(final List<List<Integer>> listOfLists) {
        int size = listOfLists.size();
        long currentLeftValue = 0;
        for (int i = size - 2; i >= 0; i--) {
            currentLeftValue = listOfLists.get(i).get(0) - currentLeftValue;
        }
        return currentLeftValue;
    }

    private static long predictNextValueForAListPartTwo(final List<Integer> numberList) {
        List<List<Integer>> listOfLists = new ArrayList<>();
        listOfLists.add(numberList);

        int size = listOfLists.size();
        while (!isAllZeros(listOfLists.get(size - 1))) {
            List<Integer> nextLine = generateNextLine(listOfLists.get(size - 1));
            listOfLists.add(nextLine);
            size = listOfLists.size();
        }
        return generateFinalResultFromAllListsPartTwo(listOfLists);
    }

    private static long partTwo(List<String> inputLines) {
        List<Long> results = new ArrayList<>();
        inputLines.forEach(line -> {
            String[] numbersStringArray = line.strip().split(" ");
            List<Integer> numberList = Arrays.stream(numbersStringArray).map(Integer::parseInt).toList();
            long currentResult = predictNextValueForAListPartTwo(numberList);
            results.add(currentResult);
        });
        System.out.println("results = " + results);
        return results.stream().mapToLong(Long::longValue).sum();
    }

    public static void main(String[] args) throws Exception {
        String pathToInputFile = "src/main/resources/day09/input.txt";
        List<String> inputLines = MyUtilities.getInputLines(pathToInputFile);

        System.out.println("\nPART I = " + partOne(inputLines));
        System.out.println("PART II = " + partTwo(inputLines));
    }
}
