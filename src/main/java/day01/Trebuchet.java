package day01;

import utils.MyUtilities;

import java.io.IOException;
import java.util.List;

public class Trebuchet {

    private static List<String> stringDigitList = List.of("zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine");

    private static String findFirstDigit(final String line) {

        return null;
    }

    private static String findLastDigit(final String line) {
        return null;
    }

    private static String partOne(List<String> inputLines) {
        int[] sumOfCalibrationValues = new int[1];
        inputLines.forEach(line -> {
            String onlyDigitsString = line.replaceAll("[^\\d.]", "");
            String firstDigit = String.valueOf(onlyDigitsString.charAt(0));
            String lastDigit = String.valueOf(onlyDigitsString.charAt(onlyDigitsString.length() - 1));
            int calibrationValue = Integer.parseInt(firstDigit + lastDigit);
            sumOfCalibrationValues[0] += calibrationValue;
        });

        return String.valueOf(sumOfCalibrationValues[0]);
    }

    private static String partTwo(List<String> inputLines) {
        int[] sumOfCalibrationValues = new int[1];
        inputLines.forEach(line -> {
            String firstDigit = findFirstDigit(line);
            String lastDigit = findLastDigit(line);
            int length = line.length();



            int calibrationValue = Integer.parseInt(firstDigit + lastDigit);
            sumOfCalibrationValues[0] += calibrationValue;
        });

        return "";
    }

    public static void main(String[] args) throws IOException {
        String pathToInputFile = "src/main/resources/day01/input.txt";
        List<String> inputLines = MyUtilities.getInputLines(pathToInputFile);


        System.out.println("Part 1 = " + partOne(inputLines));
        System.out.println("Part 2 = " + partTwo(inputLines));
    }
}
