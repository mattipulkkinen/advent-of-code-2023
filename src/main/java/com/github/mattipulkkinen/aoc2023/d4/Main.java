package com.github.mattipulkkinen.aoc2023.d4;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

  public static void main(String[] args) {
    List<Card> cards = new ArrayList<>(200);

    try (BufferedReader reader = Files.newBufferedReader(Path.of("input.txt"))) {
      reader.lines().map(Main::extractCardFromString).forEach(cards::add);
    } catch (IOException e) {
      System.err.println("Failed to read input, aborting...");
      System.exit(1);
    }

    int totalValueOfAllCards = cards.stream().mapToInt(Card::value).sum();

    System.out.println("The total value of all cards is " + totalValueOfAllCards);
  }

  private static Card extractCardFromString(String string) {
    String[] cardNumberAndRestSplit = string.split(": +");
    int cardNumber = Integer.parseInt(cardNumberAndRestSplit[0].split(" +")[1]);

    String[] winningNumbersAndNumbersYouHaveSplit = cardNumberAndRestSplit[1].split(" +\\| +");
    String winningNumbers = winningNumbersAndNumbersYouHaveSplit[0].trim();
    String numbersYouHave = winningNumbersAndNumbersYouHaveSplit[1].trim();

    List<Integer> winningNumbersList = Arrays.stream(winningNumbers.split(" +"))
        .map(Integer::parseInt).toList();
    List<Integer> numbersYouHaveList = Arrays.stream(numbersYouHave.split(" +"))
        .map(Integer::parseInt).toList();

    return new Card(cardNumber, winningNumbersList, numbersYouHaveList);
  }
}
