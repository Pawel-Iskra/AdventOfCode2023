package day04;

import utils.MyUtilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Scratchcards {

    private static long partOne(List<String> inputLines) {
        long result = 0;
        List<List<Integer>> myNumbersList = new ArrayList<>();
        List<List<Integer>> winningNumbersList = new ArrayList<>();

        inputLines.forEach(line -> {
            String[] winningNumStringArray = line.substring(line.indexOf(":") + 1 , line.indexOf("|")).strip().split(" ");
            String[] myNumbersStringArray = line.substring(line.indexOf("|") + 1).strip().split(" ");
            System.out.println("Winnnings:");
            Arrays.stream(winningNumStringArray).forEach(num -> System.out.println(num));
            System.out.println("\nMy nums:");
            Arrays.stream(myNumbersStringArray).forEach(num -> System.out.println(num));
//            System.out.println("winningNumString = " + winningNumString);
//            System.out.println("myNumbers = " + myNumbersString);

            List<Integer> current = new ArrayList<>();
            final List<Integer> finalCurrent = current;
            Arrays.stream(winningNumStringArray).filter(number -> !number.isBlank()).forEach(num -> {
                finalCurrent.add(Integer.parseInt(num.strip()));
            });
            winningNumbersList.add(current);
            current = new ArrayList<>();
            final List<Integer> finalCurrent1 = current;
            Arrays.stream(myNumbersStringArray).filter(number -> !number.isBlank()).forEach(num -> {
                finalCurrent1.add(Integer.parseInt(num.strip()));
            });
            myNumbersList.add(finalCurrent1);
            System.out.println("winningNumbersList = " + winningNumbersList);
            System.out.println("myNumbersList = " + myNumbersList);
        });
        int numOfSeries = winningNumbersList.size();
        for(int i = 0; i < numOfSeries; i++){
            List<Integer> currentWinnings = winningNumbersList.get(i);
            List<Integer> currentMyNums = myNumbersList.get(i);
            System.out.println("currentWinnings = " + currentWinnings);
            System.out.println("currentMyNums = " + currentMyNums);
            long currentResult = 0;
            for (Integer currentWinningNum : currentWinnings) {
                if(currentMyNums.contains(currentWinningNum)){

                    if (currentResult == 0) currentResult = 1;
                    else currentResult *=2;
                }
            }
            System.out.println("currentResult = " + currentResult);
            result +=currentResult;

        }

        return result;
    }

    private static String partTwo(List<String> inputLines) {

        return "";
    }

    public static void main(String[] args) throws IOException {
        String pathToInputFile = "src/main/resources/day04/input.txt";
        List<String> inputLines = MyUtilities.getInputLines(pathToInputFile);
        inputLines.forEach(line -> System.out.println(line));

        System.out.println("\nPART I = " + partOne(inputLines));
        System.out.println("PART II = " + partTwo(inputLines));

    }
}
