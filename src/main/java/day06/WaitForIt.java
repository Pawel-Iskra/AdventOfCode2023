package day06;

import utils.MyUtilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class WaitForIt {
    private static List<Long> distancesList = new ArrayList<>();
    private static List<Integer> timesList = new ArrayList<>();

    private static void generateResultMapForPartOne(final List<String> inputLines) {
        String timesString = inputLines.get(0).substring(inputLines.get(0).indexOf(":") + 1);
        String distancesString = inputLines.get(1).substring(inputLines.get(1).indexOf(":") + 1);
        Scanner scn = new Scanner(timesString);
        while (scn.hasNext()) timesList.add(scn.nextInt());
        scn = new Scanner(distancesString);
        while (scn.hasNext()) distancesList.add(scn.nextLong());

//        System.out.println("timesList = " + timesList);
//        System.out.println("distancesList = " + distancesList);
    }

    private static void generateResultMapForPartTwo(final List<String> inputLines) {
        String timesString = inputLines.get(0).substring(inputLines.get(0).indexOf(":") + 1);
        String distancesString = inputLines.get(1).substring(inputLines.get(1).indexOf(":") + 1);
        timesList = new ArrayList<>();
        distancesList = new ArrayList<>();
        timesList.add(Integer.parseInt(timesString.replaceAll(" ", "")));
        distancesList.add(Long.parseLong(distancesString.replaceAll(" ", "")));

//        System.out.println("timesList = " + timesList);
//        System.out.println("distancesList = " + distancesList);
    }


    private static long goOneAttemptWithGivenSpeedAndTime(long time, long speed) {
        return time * speed;
    }

    private static List<Long> getPossibleResultsForGivenTime(final int currentTime) {
        List<Long> possibleResults = new ArrayList<>();
        for (int i = 0; i <= currentTime; i++) {
            possibleResults.add(goOneAttemptWithGivenSpeedAndTime(currentTime - i, i));
        }
        //System.out.println("possibleResults = " + possibleResults);
        return possibleResults;
    }

    private static long partOne() {
        long result = 1;
        List<Long> numOfPossibleWinnings = new ArrayList<>();
        int amount = distancesList.size();
        for (int i = 0; i < amount; i++) {
            int currentTime = timesList.get(i);
            long currentDistance = distancesList.get(i);
            List<Long> possibleResults = getPossibleResultsForGivenTime(currentTime);
            //System.out.println("possibleResults = " + possibleResults);
            numOfPossibleWinnings.add(possibleResults.stream().filter(current -> current > currentDistance).count());
        }
        //System.out.println("numOfPossibleWinnings = " + numOfPossibleWinnings);

        return numOfPossibleWinnings.stream().reduce(1L, (a, b) -> a * b);
    }

    private static long partTwo() {
        long counter = 0;
        int currentTime = timesList.get(0);
        long currentDistance = distancesList.get(0);
        for (int i = 0; i <= currentTime; i++) {
            long result = goOneAttemptWithGivenSpeedAndTime(currentTime - i, i);
            if (result > currentDistance) counter++;
        }
        return counter;
    }


    public static void main(String[] args) throws IOException {
        String pathToInputFile = "src/main/resources/day06/input.txt";
        List<String> inputLines = MyUtilities.getInputLines(pathToInputFile);
        //inputLines.forEach(System.out::println);

        generateResultMapForPartOne(inputLines);
        System.out.println("PART I = " + partOne());

        generateResultMapForPartTwo(inputLines);
        System.out.println("\nPART II = " + partTwo());


    }

}
