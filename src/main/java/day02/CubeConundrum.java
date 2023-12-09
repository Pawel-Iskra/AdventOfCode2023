package day02;

import utils.MyUtilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CubeConundrum {

    private static String GREEN = "green";
    private static String RED = "red";
    private static String BLUE = "blue";

    private static int RED_NUMBER = 12;
    private static int GREEN_NUMBER = 13;
    private static int BLUE_NUMBER = 14;

    private static int calculateMinimumColour(final String setsString, String colour) {
        int minimumToBe = 0;
        String[] setsArray = setsString.split(";");
        for (String oneSet : setsArray) {
            String[] shoots = oneSet.split(",");
            for (String oneShoot : shoots) {
                if (oneShoot.contains(colour)) {
                    int currentColour = Integer.parseInt(oneShoot.replaceAll("[^\\d.]", ""));
                    if (currentColour > minimumToBe) minimumToBe = currentColour;
                }
            }
        }
        return minimumToBe;
    }

    private static int calculateColour(final String oneSet, String colour) {
        int sum = 0;
        String[] singleSets = oneSet.split(",");
        for (String singleSet : singleSets) {
            if (singleSet.contains(colour)) {
                int value = Integer.parseInt(singleSet.replaceAll("[^\\d.]", ""));
                sum += value;
            }
        }
        return sum;
    }

    private static int partTwo(List<String> inputLines) {
        List<Integer> setsPower = new ArrayList<>();
        for (String line : inputLines) {
            String setsString = line.substring(line.indexOf(":") + 1);
            int minimumReds = calculateMinimumColour(setsString, RED);
            int minimumGreens = calculateMinimumColour(setsString, GREEN);
            int minimumBlues = calculateMinimumColour(setsString, BLUE);
            int power = minimumBlues * minimumGreens * minimumReds;
            setsPower.add(power);
        }
        int sum = 0;
        for (Integer power : setsPower) sum += power;
        return sum;
    }

    private static int partOne(List<String> inputLines) {
        int indexSum = 0;
        for (String line : inputLines) {
            String indexString = line.substring(0, line.indexOf(":")).replaceAll("[^\\d.]", "");
            int index = Integer.parseInt(indexString.strip());
            String setsString = line.substring(line.indexOf(":") + 1);
            String[] sets = setsString.split(";");
            int length = sets.length;
            int counter = 0;
            for (String oneSet : sets) {
                int blues = calculateColour(oneSet, BLUE);
                int reds = calculateColour(oneSet, RED);
                int greens = calculateColour(oneSet, GREEN);
                if (blues <= BLUE_NUMBER && greens <= GREEN_NUMBER && reds <= RED_NUMBER) counter++;
            }
            if (counter == length) indexSum += index;
        }
        return indexSum;
    }

    public static void main(String[] args) throws IOException {
        String pathToInputFile = "src/main/resources/day02/input.txt";
        List<String> inputLines = MyUtilities.getInputLines(pathToInputFile);

        System.out.println("PART I = " + partOne(inputLines));
        System.out.println("PART II = " + partTwo(inputLines));
    }
}
