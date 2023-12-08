package day07;

import utils.MyUtilities;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class CamelCards {

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
            counter = 1;
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
        if (Objects.equals(sorted.get(0), sorted.get(1)) && Objects.equals(sorted.get(1), sorted.get(2)) && Objects.equals(sorted.get(3), sorted.get(4)))
            return true;
        if (Objects.equals(sorted.get(0), sorted.get(1)) && Objects.equals(sorted.get(2), sorted.get(3)) && Objects.equals(sorted.get(3), sorted.get(4)))
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

        if (Objects.equals(sorted.get(0), sorted.get(1)) && Objects.equals(sorted.get(1), sorted.get(2))) return true;
        if (Objects.equals(sorted.get(1), sorted.get(2)) && Objects.equals(sorted.get(2), sorted.get(3))) return true;
        if (Objects.equals(sorted.get(4), sorted.get(3)) && Objects.equals(sorted.get(3), sorted.get(2))) return true;
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
            handsMap.put(hand, bid);
            putToProperListType(hand);
        });

        Comparator<String> myComparator = Comparator.
                comparing((String str) -> cardsFromHighest.indexOf(str.charAt(0)))
                .thenComparing(str -> cardsFromHighest.indexOf(str.charAt(1)))
                .thenComparing(str -> cardsFromHighest.indexOf(str.charAt(2)))
                .thenComparing(str -> cardsFromHighest.indexOf(str.charAt(3)))
                .thenComparing(str -> cardsFromHighest.indexOf(str.charAt(4)));

        Collections.sort(fives, myComparator);
        Collections.sort(fours, myComparator);
        Collections.sort(fulls, myComparator);
        Collections.sort(threes, myComparator);
        Collections.sort(twoPairs, myComparator);
        Collections.sort(onePairs, myComparator);
        Collections.sort(restHands, myComparator);
    }

    private static int calculateValueForAList(List<String> handsList, int currentRank) {
        int result = 0;
        int numOfRestHands = handsList.size();
        for (int i = numOfRestHands - 1; i >= 0; i--) {
            String currentHand = handsList.get(i);
            int currentHandBid = handsMap.get(currentHand);
            int currentHandValue = currentRank * currentHandBid;
            result += currentHandValue;
            currentRank++;
        }
        return result;
    }

    private static int calculateJokers(String hand) {
        return (int) hand.chars().filter(c -> c == 'J').count();
    }

    private static void optimizeFours() {
        int numOfFours = fours.size();
        List<String> handsToRemoveFromList = new ArrayList<>();
        for (int i = 0; i < numOfFours; i++) {
            String currentHand = fours.get(i);
            if (currentHand.contains("J")) {
                handsToRemoveFromList.add(currentHand);
                fives.add(currentHand);
            }
        }
        for (String handToRemoveFromList : handsToRemoveFromList) {
            fours.remove(handToRemoveFromList);
        }
    }

    private static void optimizeFulls() {
        int numOfFours = fulls.size();
        List<String> handsToRemoveFromList = new ArrayList<>();
        for (int i = 0; i < numOfFours; i++) {
            String currentHand = fulls.get(i);
            if (currentHand.contains("J")) {
                handsToRemoveFromList.add(currentHand);
                fives.add(currentHand);
            }
        }
        for (String handToRemoveFromList : handsToRemoveFromList) {
            fulls.remove(handToRemoveFromList);
        }
    }

    private static void optimizeThrees() {
        int numOfFours = threes.size();
        List<String> handsToRemoveFromList = new ArrayList<>();
        for (int i = 0; i < numOfFours; i++) {
            String currentHand = threes.get(i);
            if (currentHand.contains("J")) {
                handsToRemoveFromList.add(currentHand);
                fours.add(currentHand);
            }
        }
        for (String handToRemoveFromList : handsToRemoveFromList) {
            threes.remove(handToRemoveFromList);
        }
    }

    private static void optimizeTwoPairs() {
        int numOfFours = twoPairs.size();
        List<String> handsToRemoveFromList = new ArrayList<>();
        for (int i = 0; i < numOfFours; i++) {
            String currentHand = twoPairs.get(i);
            if (currentHand.contains("J")) {
                handsToRemoveFromList.add(currentHand);
                if (calculateJokers(currentHand) == 2) fours.add(currentHand);
                else fulls.add(currentHand);
            }
        }
        for (String handToRemoveFromList : handsToRemoveFromList) {
            twoPairs.remove(handToRemoveFromList);
        }
    }

    private static void optimizeOnePairs() {
        int numOfFours = onePairs.size();
        List<String> handsToRemoveFromList = new ArrayList<>();
        for (int i = 0; i < numOfFours; i++) {
            String currentHand = onePairs.get(i);
            if (currentHand.contains("J")) {
                handsToRemoveFromList.add(currentHand);
                threes.add(currentHand);
            }
        }
        for (String handToRemoveFromList : handsToRemoveFromList) {
            onePairs.remove(handToRemoveFromList);
        }
    }

    private static void optimizeRestHands() {
        int numOfFours = restHands.size();
        List<String> handsToRemoveFromList = new ArrayList<>();
        for (int i = 0; i < numOfFours; i++) {
            String currentHand = restHands.get(i);
            if (currentHand.contains("J")) {
                handsToRemoveFromList.add(currentHand);
                onePairs.add(currentHand);
            }
        }
        for (String handToRemoveFromList : handsToRemoveFromList) {
            restHands.remove(handToRemoveFromList);
        }
    }

    private static long calculateResultForAllLists() {
        long result = 0;
        int currentHandRank = 1;
        long currentResult = calculateValueForAList(restHands, currentHandRank);
        result += currentResult;

        currentHandRank += restHands.size();
        currentResult = calculateValueForAList(onePairs, currentHandRank);
        result += currentResult;

        currentHandRank += onePairs.size();
        currentResult = calculateValueForAList(twoPairs, currentHandRank);
        result += currentResult;

        currentHandRank += twoPairs.size();
        currentResult = calculateValueForAList(threes, currentHandRank);
        result += currentResult;

        currentHandRank += threes.size();
        currentResult = calculateValueForAList(fulls, currentHandRank);
        result += currentResult;

        currentHandRank += fulls.size();
        currentResult = calculateValueForAList(fours, currentHandRank);
        result += currentResult;

        currentHandRank += fours.size();
        currentResult = calculateValueForAList(fives, currentHandRank);
        result += currentResult;

        return result;
    }

    private static void sortListsForPartOne() {
        Comparator<String> myComparator = Comparator.
                comparing((String str) -> cardsFromHighest.indexOf(str.charAt(0)))
                .thenComparing(str -> cardsFromHighest.indexOf(str.charAt(1)))
                .thenComparing(str -> cardsFromHighest.indexOf(str.charAt(2)))
                .thenComparing(str -> cardsFromHighest.indexOf(str.charAt(3)))
                .thenComparing(str -> cardsFromHighest.indexOf(str.charAt(4)));

        Collections.sort(fives, myComparator);
        Collections.sort(fours, myComparator);
        Collections.sort(fulls, myComparator);
        Collections.sort(threes, myComparator);
        Collections.sort(twoPairs, myComparator);
        Collections.sort(onePairs, myComparator);
        Collections.sort(restHands, myComparator);
    }

    private static void sortListForPartTwo() {
        List<Character> newCardsFromHighest = new ArrayList<>(cardsFromHighest);
        newCardsFromHighest.remove(new Character('J'));
        newCardsFromHighest.add('J');

        Comparator<String> myComparator = Comparator.
                comparing((String str) -> newCardsFromHighest.indexOf(str.charAt(0)))
                .thenComparing(str -> newCardsFromHighest.indexOf(str.charAt(1)))
                .thenComparing(str -> newCardsFromHighest.indexOf(str.charAt(2)))
                .thenComparing(str -> newCardsFromHighest.indexOf(str.charAt(3)))
                .thenComparing(str -> newCardsFromHighest.indexOf(str.charAt(4)));

        Collections.sort(fives, myComparator);
        Collections.sort(fours, myComparator);
        Collections.sort(fulls, myComparator);
        Collections.sort(threes, myComparator);
        Collections.sort(twoPairs, myComparator);
        Collections.sort(onePairs, myComparator);
        Collections.sort(restHands, myComparator);
    }

    private static long partTwo() {
        optimizeFours();
        optimizeFulls();
        optimizeThrees();
        optimizeTwoPairs();
        optimizeOnePairs();
        optimizeRestHands();
        sortListForPartTwo();
        return calculateResultForAllLists();
    }

    private static long partOne() {
        sortListsForPartOne();
        return calculateResultForAllLists();
    }

    public static void main(String[] args) throws IOException {
        String pathToInputFile = "src/main/resources/day07/input.txt";
        List<String> inputLines = MyUtilities.getInputLines(pathToInputFile);
        prepareHandsBeforePlay(inputLines);

        System.out.println("PART I = " + partOne());
        System.out.println("PART II = " + partTwo());
    }
}
