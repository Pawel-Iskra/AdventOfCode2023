package day01;

import utils.MyUtilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Trebuchet {
    private static List<String> numberAsStringList = List.of("one", "two", "three", "four", "five", "six", "seven", "eight", "nine");

    private static String getFirstDigit(final String line) {
        int length = line.length();
        StringBuilder strb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            strb.append(line.charAt(i));
            if (Character.isDigit(strb.charAt(strb.length() - 1))) {
                return String.valueOf(strb.charAt(strb.length() - 1));
            }
            for (String numberAsString : numberAsStringList) {
                if (strb.toString().contains(numberAsString)) {
                    return numberAsString;
                }
            }
        }
        return "0";
    }

    private static String getLastDigit(final String line) {
        int length = line.length();
        StringBuilder strb = new StringBuilder();
        for (int i = length - 1; i >= 0; i--) {
            strb.insert(0, line.charAt(i));
            if (Character.isDigit(strb.charAt(0))) {
                return String.valueOf(strb.charAt(0));
            }
            for (String numberAsString : numberAsStringList) {
                if (strb.toString().contains(numberAsString)) {
                    return numberAsString;
                }
            }
        }
        return "0";
    }

    private static int partTwo(final List<String> inputLines) {
        List<Integer> numberList = new ArrayList<>();
        for (String line : inputLines) {
            String first = getFirstDigit(line);
            if (first.length() > 1) first = String.valueOf(numberAsStringList.indexOf(first) + 1);
            String last = getLastDigit(line);
            if (last.length() > 1) last = String.valueOf(numberAsStringList.indexOf(last) + 1);
            numberList.add(Integer.parseInt(first + last));
        }
        return numberList.stream().mapToInt(Integer::intValue).sum();
    }

    private static int partOne(List<String> inputLines) {
        List<Integer> numberList = new ArrayList<>();
        for (String line : inputLines) {
            String currentLine = line.replaceAll("[^\\d.]", "");
            String first = String.valueOf(currentLine.charAt(0));
            String last = String.valueOf(currentLine.charAt(currentLine.length() - 1));
            int number = Integer.parseInt(first + last);
            numberList.add(number);
        }
        return numberList.stream().mapToInt(Integer::intValue).sum();
    }

    public static void main(String[] args) throws IOException {
        String pathToInputFile = "src/main/resources/day01/input.txt";
        List<String> inputLines = MyUtilities.getInputLines(pathToInputFile);

        System.out.println("PART I = " + partOne(inputLines));
        System.out.println("PART II = " + partTwo(inputLines));

    }
}
