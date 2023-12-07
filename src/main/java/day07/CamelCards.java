package day07;

import utils.MyUtilities;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class CamelCards {

    // handStrengths: Five of a kind,Four of a kind, Full house (3+2), Three of a kind,Two pair, One pain, high
    private static List<Character> cardsFromHighest = List.of('A', 'K', 'Q', 'J', 'T', '9', '8', '7', '6', '5', '4', '3', '2');
    private static Map<String, Integer> handsMap = new HashMap<>();
    private static List<String> fives = new ArrayList<>();
    private static List<String> fours = new ArrayList<>();
    private static List<String> fulls = new ArrayList<>();
    private static List<String> threes = new ArrayList<>();
    private static List<String> twoPairs = new ArrayList<>();
    private static List<String> onePairs = new ArrayList<>();
    private static List<String> restHands = new ArrayList<>();

    private static boolean isFives(final String hand) {
        char firstChar = hand.charAt(0);
        int length = hand.length();
        int counter = 1;
        for (int i = 1; i < length; i++) {
            if (firstChar == hand.charAt(i)) counter++;
        }
        return 5 == counter;
    }

    private static boolean isFours(final String hand) {
        char firstChar = hand.charAt(0);
        int length = hand.length();
        int counter = 1;
        for (int i = 1; i < length; i++) {
            if (firstChar == hand.charAt(i)) counter++;
        }
        if (counter < 4) {
            counter = 0;
            char lastChar = hand.charAt(length - 1);
            for (int i = length - 2; i >= 0; i--) {
                if (lastChar == hand.charAt(i)) counter++;
            }
        }
        return counter == 4;
    }

    private static boolean isFull(final String hand) {
        char[] handAsCharArray = hand.toCharArray();
        List<Character> list = new ArrayList<>();
        for (char c : handAsCharArray) {
            list.add(c);
        }
        List<Character> sorted = list.stream().sorted().collect(Collectors.toList());
        //System.out.println("sorted = " + sorted);
        if (sorted.get(0) == sorted.get(1) && sorted.get(1) == sorted.get(2) && sorted.get(3) == sorted.get(4))
            return true;
        if (sorted.get(0) == sorted.get(1) && sorted.get(2) == sorted.get(3) && sorted.get(3) == sorted.get(4))
            return true;
        return false;
    }

    private static boolean isThree(final String hand) {
        char[] handAsCharArray = hand.toCharArray();
        List<Character> list = new ArrayList<>();
        for (char c : handAsCharArray) {
            list.add(c);
        }
        List<Character> sorted = list.stream().sorted().collect(Collectors.toList());
        if (sorted.get(0) == sorted.get(1) && sorted.get(1) == sorted.get(2)) return true;
        if (sorted.get(4) == sorted.get(3) && sorted.get(3) == sorted.get(2)) return true;
        return false;
    }

    private static boolean isTwoPairs(final String hand) {
        char[] handAsCharArray = hand.toCharArray();
        List<Character> list = new ArrayList<>();
        for (char c : handAsCharArray) {
            list.add(c);
        }
        List<Character> sorted = list.stream().sorted().collect(Collectors.toList());
        Set<Character> set = new HashSet<>(sorted);
        return set.size() == 3;
    }

    private static boolean isOnePair(final String hand) {
        char[] handAsCharArray = hand.toCharArray();
        List<Character> list = new ArrayList<>();
        for (char c : handAsCharArray) {
            list.add(c);
        }
        List<Character> sorted = list.stream().sorted().collect(Collectors.toList());
        Set<Character> set = new HashSet<>(sorted);
        return set.size() == 4;
    }

    private static void putToProperListType(String hand) {
        if (isFives(hand)) fives.add(hand);
        else if (isFours(hand)) fours.add(hand);
        else if (isFull(hand)) fulls.add(hand);
        else if (isThree(hand)) threes.add(hand);
        else if (isTwoPairs(hand)) twoPairs.add(hand);
        else if (isOnePair(hand)) onePairs.add(hand);
        else restHands.add(hand);
    }

    private static void prepareHandsBeforePlay(final List<String> inputLines) {
        inputLines.forEach(line -> {
            String[] lineAsArray = line.split(" ");
            String hand = lineAsArray[0];
            int bid = Integer.parseInt(lineAsArray[1]);
            //System.out.println("hand = " + hand + " -> " + bid);
            handsMap.put(hand, bid);
            putToProperListType(hand);
        });
//
//        System.out.println("fives = " + fives);
//        System.out.println("fours = " + fours);
//        System.out.println("fulls = " + fulls);
//        System.out.println("threes = " + threes);
//        System.out.println("twoPairs = " + twoPairs);
//        System.out.println("onePairs = " + onePairs);
//        System.out.println("restHands = " + restHands);
    }

    private static long partOne() {
        long result = 0;
        int numOfHands = handsMap.size();
        Comparator<String> handCompareFirst = Comparator.comparing(hand -> cardsFromHighest.indexOf(hand.charAt(0)));
        Comparator<String> handCompareSecond = Comparator.comparing(hand -> cardsFromHighest.indexOf(hand.charAt(1)));
        Collections.sort(restHands, handCompareFirst);
        Collections.sort(restHands, handCompareFirst);

        System.out.println("\nSORTED:\n");
//        System.out.println("fives = " + fives);
//        System.out.println("fours = " + fours);
//        System.out.println("fulls = " + fulls);
//        System.out.println("threes = " + threes);
//        System.out.println("twoPairs = " + twoPairs);
//        System.out.println("onePairs = " + onePairs);
        System.out.println("restHands = " + restHands);

        return result;
    }

    private static long partTwo() {
        long counter = 0;

        return counter;
    }


    public static void main(String[] args) throws IOException {
        String pathToInputFile = "src/main/resources/day07/input.txt";
        List<String> inputLines = MyUtilities.getInputLines(pathToInputFile);
        inputLines.forEach(System.out::println);
        prepareHandsBeforePlay(inputLines);

        System.out.println("PART I = " + partOne());
        System.out.println("\nPART II = " + partTwo());


    }
}
