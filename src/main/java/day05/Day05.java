package day05;

import utils.MyUtilities;

import java.io.IOException;
import java.util.*;

public class Day05 {

    private static List<String> foodProductionMapsNamesList = new ArrayList<>();
    private static List<Long> seedsList = new ArrayList<>();
    private static Map<String, List<Conditions>> foodProductionMaps = new HashMap<>();

    private static class Conditions {
        private long leftValue;
        private long rightValue;
        private long length;

        Conditions(final long leftValue, final long rightValue, final long length) {
            this.leftValue = leftValue;
            this.rightValue = rightValue;
            this.length = length;
        }

        long getLeftValue() {
            return leftValue;
        }

        long getRightValue() {
            return rightValue;
        }

        long getLength() {
            return length;
        }

        @Override
        public String toString() {
            return "Conditions{" +
                    "leftValue=" + leftValue +
                    ", rightValue=" + rightValue +
                    ", length=" + length +
                    '}';
        }
    }


    private static void generateFoodProductionMaps(final List<String> inputLines) {
        int size = inputLines.size();
        String[] seedsArray = inputLines.get(0).substring("seeds:".length()).split(" ");
        Arrays.stream(seedsArray).forEach(seedAsString -> {
            if (!seedAsString.isBlank()) seedsList.add(Long.parseLong(seedAsString.strip()));
        });
        System.out.println("seedsList = " + seedsList);
        for (int i = 0; i < size; i++) {
            String currentLine = inputLines.get(i);
            if (currentLine.contains("map")) {
                String mapName = currentLine.substring(0, currentLine.indexOf(":"));
                foodProductionMapsNamesList.add(mapName);
                List<Conditions> conditionsList = new ArrayList<>();
                i++;
                currentLine = inputLines.get(i);
                while (!currentLine.isBlank()) {
                    System.out.println("currentLine = " + currentLine);
                    String[] lineAsArray = currentLine.split(" ");
                    long destination = Long.parseLong(lineAsArray[0].strip());
                    long source = Long.parseLong(lineAsArray[1].strip());
                    long length = Long.parseLong(lineAsArray[2].strip());
                    conditionsList.add(new Conditions(destination, source, length - 1));
                    i++;
                    if (i >= size) break;
                    currentLine = inputLines.get(i);
                }
                foodProductionMaps.put(mapName, conditionsList);
            }

        }
        System.out.println("foodProductionMapsNamesList = " + foodProductionMapsNamesList);
        for (Map.Entry<String, List<Conditions>> entry : foodProductionMaps.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }

    }

    private static long goThroughConditions(Conditions conditions, long inputValue) {
        if ((inputValue >= conditions.getRightValue()) && (inputValue <= (conditions.getRightValue() + conditions.getLength()))) {
            return conditions.getLeftValue() + inputValue - conditions.getRightValue();
        }
        return inputValue;
    }

    private static long goThroughMap(String mapName, long inputValue) {
        long[] result = new long[1];
        result[0] = inputValue;
        List<Conditions> conditionsList = foodProductionMaps.get(mapName);
        conditionsList.forEach(currentConditions -> {
            if (isInRange(currentConditions, inputValue)) {
                result[0] = goThroughConditions(currentConditions, inputValue);
            }

        });
        return result[0];
    }

    private static boolean isInRange(final Conditions currentConditions, final long inputValue) {
        return (inputValue >= currentConditions.getRightValue()) && (inputValue <= (currentConditions.getRightValue() + currentConditions.getLength()));
    }

    private static long partOne() {
        long result = 0;
        List<Long> lowestLocations = new ArrayList<>();
        seedsList.forEach(sedNumb -> {
            System.out.println("sedNumb = " + sedNumb);
            long currentResult = goThroughMap("seed-to-soil map", sedNumb);
            System.out.println("currentResult = " + currentResult);
            currentResult = goThroughMap("soil-to-fertilizer map", currentResult);
            System.out.println("currentResult = " + currentResult);
            currentResult = goThroughMap("fertilizer-to-water map", currentResult);
            System.out.println("currentResult = " + currentResult);
            currentResult = goThroughMap("water-to-light map", currentResult);
            System.out.println("currentResult = " + currentResult);
            currentResult = goThroughMap("light-to-temperature map", currentResult);
            System.out.println("currentResult = " + currentResult);
            currentResult = goThroughMap("temperature-to-humidity map", currentResult);
            System.out.println("currentResult = " + currentResult);
            currentResult = goThroughMap("humidity-to-location map", currentResult);
            System.out.println("currentResult = " + currentResult);
            lowestLocations.add(currentResult);
        });

        lowestLocations.sort(Collections.reverseOrder());
        return lowestLocations.get(lowestLocations.size()-1);
    }


    private static String partTwo() {
        return "";
    }

    public static void main(String[] args) throws IOException {
        String pathToInputFile = "src/main/resources/day05/input.txt";
        List<String> inputLines = MyUtilities.getInputLines(pathToInputFile);
//        inputLines.forEach(line -> {
//            System.out.println("line = " + line);
//        });
        generateFoodProductionMaps(inputLines);

        System.out.println("PART I = " + partOne());
        System.out.println("PART II = " + partTwo());

    }
}
