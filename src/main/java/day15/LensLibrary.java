package day15;

import utils.MyUtilities;

import java.util.*;

public class LensLibrary {

    private static List<Lens>[] BOXES;

    // PART I:
    // Determine the ASCII code for the current character of the string.
    // Increase the current value by the ASCII code you just determined.
    // Set the current value to itself multiplied by 17.
    // Set the current value to the remainder of dividing itself by 256.

    // PART II
    //The book goes on to describe a series of 256 boxes numbered 0 through 255.

    // The label will be immediately followed by a character that indicates
    // the operation to perform: either an equals sign (=) or a dash (-).

//    If the operation character is a dash (-), go to the relevant box and REMOVE the lens with the GIVEN LABEL
//    if it is present in the box. Then, move any remaining lenses as far forward in the box
//    as they can go without changing their order, filling any space made by removing the indicated lens.
//    (If no lens in that box has the given label, nothing happens.)

//    If the operation character is an equals sign (=), it will be followed by a number indicating
//    the focal length of the lens that needs to go into the relevant box; be sure to use the label maker
//    to mark the lens with the label given in the beginning of the step so you can find it later.

//    There are two possible situations:
//      - If there is already a lens in the box with the same label, replace the old lens with the new lens:
//        remove the old lens and put the new lens in its place, not moving any other lenses in the box.
//      - If there is not already a lens in the box with the same label, add the lens to the box immediately
//        behind any lenses already in the box. Don't move any of the other lenses when you do this.
//        If there aren't any lenses in the box, the new lens goes all the way to the front of the box.

    static class Lens {
        private String name;
        private int value;

        Lens(final String name, final int value) {
            this.name = name;
            this.value = value;
        }

        String getName() {
            return name;
        }

        void setName(final String name) {
            this.name = name;
        }

        int getValue() {
            return value;
        }

        void setValue(final int value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return name + " " + value;
        }
    }

    private static void printBoxes() {
        for (int i = 0; i < 256; i++) {
            if (BOXES[i].size() > 0) System.out.println("Box " + i + " -> " + BOXES[i]);
        }
    }

    private static void generateBoxes() {
        BOXES = new List[256];
        for (int i = 0; i < 256; i++) {
            BOXES[i] = new ArrayList<>();
        }
    }

    private static int runHashAlgorithm(String step) {
        int value = 0;
        int length = step.length();
        for (int i = 0; i < length; i++) {
            char currentChar = step.charAt(i);
            value += (int) currentChar;
            value = value * 17;
            value = value % 256;
        }

        return value;
    }


    private static int partTwo(String commandsLine) {
        generateBoxes();
        String[] stepsArray = commandsLine.split(",");
        int numOfSteps = stepsArray.length;
        for (int i = 0; i < numOfSteps; i++) {
            int lensValue = 0;
            String lensName = stepsArray[i].replaceAll("[^a-zA-Z]", "").strip();
//            System.out.println("\nlensName = " + lensName);
            int numOfBox = runHashAlgorithm(lensName);
//            System.out.println("numOfBox = " + numOfBox);
            String operator = stepsArray[i].replaceAll("[a-zA-Z0-9]", "").strip();
//            System.out.println("operator = " + operator);
            if ("=".equals(operator)) {
                lensValue = Integer.parseInt(stepsArray[i].replaceAll("[^0-9]", "").strip());
//                System.out.println("lensValue = " + lensValue);
            }
            String lensFullName = lensName + " " + lensValue;
//            System.out.println("lensFullName = " + lensFullName);

            if ("-".equals(operator)) {
                List<Lens> currentList = BOXES[numOfBox];
                int size = currentList.size();
                for (int j = 0; j < size; j++) {
                    Lens currentLens = currentList.get(j);
                    if (currentLens.getName().equals(lensName)) {
                        currentList.remove(j);
                        break;
                    }
                }

            }
            if ("=".equals(operator)) {
                List<Lens> currentList = BOXES[numOfBox];
                int size = currentList.size();
                boolean flag = false;
                for (int j = 0; j < size; j++) {
                    Lens currentLens = currentList.get(j);
                    if (currentLens.getName().equals(lensName)) {
                        currentLens.setValue(lensValue);
                        flag = true;
                    }
                }
                if (!flag) {
                    currentList.add(new Lens(lensName, lensValue));
                }

            }
        }
        printBoxes();

        int result = 0;
        for (int i = 0; i < 256; i++) {
            List<Lens> currentList = BOXES[i];
            int currentListSize = currentList.size();
            for (int j = 0; j < currentListSize; j++) {
                Lens currentLens = currentList.get(j);
                result += (i + 1) * (j + 1) * currentLens.getValue();
            }
        }

        return result;
    }

    private static String partOne(String commandsLine) {
        String[] stepsArray = commandsLine.split(",");
        int numOfSteps = stepsArray.length;
        int result = 0;
        for (int i = 0; i < numOfSteps; i++) {
            result += runHashAlgorithm(stepsArray[i]);

        }

        return String.valueOf(result);
    }

    public static void main(String[] args) throws Exception {
        String pathToInputFile = "src/main/resources/day15/input.txt";
        List<String> inputLines = MyUtilities.getInputLines(pathToInputFile);

        System.out.println("\nPART I = " + partOne(inputLines.get(0)));
        System.out.println("PART II = " + partTwo(inputLines.get(0)));
    }
}
