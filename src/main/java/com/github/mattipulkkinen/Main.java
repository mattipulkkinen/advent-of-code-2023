package com.github.mattipulkkinen;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

  private static final Map<String, Character> wordsToDigits = new HashMap<>();

  public static void main(String[] args) {
    wordsToDigits.put("zero", '0');
    wordsToDigits.put("one", '1');
    wordsToDigits.put("two", '2');
    wordsToDigits.put("three", '3');
    wordsToDigits.put("four", '4');
    wordsToDigits.put("five", '5');
    wordsToDigits.put("six", '6');
    wordsToDigits.put("seven", '7');
    wordsToDigits.put("eight", '8');
    wordsToDigits.put("nine", '9');

    List<Integer> calibrationValues;
    try {
      calibrationValues = readCalibrationValuesFromFile(Path.of("input.txt"));
    } catch (IOException e) {
      System.err.println("Failed to read input file");
      throw new RuntimeException();
    }
    int sum = calibrationValues.stream().reduce(0, Integer::sum);
    System.out.println(sum);
  }

  private static List<Integer> readCalibrationValuesFromFile(Path inputFile) throws IOException {
    List<Integer> calibrationValues = new ArrayList<>();

    try (BufferedReader reader = Files.newBufferedReader(inputFile)) {
      reader.lines().forEach(line -> {
        char firstDigit;
        char lastDigit;

        firstDigit = findFirstDigit(line);
        lastDigit = findLastDigit(line);

        if (firstDigit == ' ' || lastDigit == ' ') {
          throw new IllegalStateException("failed to find both digits for line \"" + line + "\"");
        }

        int calibrationValue = Integer.parseInt(String.valueOf(firstDigit) + lastDigit);
        calibrationValues.add(calibrationValue);
      });
    }

    return calibrationValues;
  }

  private static char findFirstDigit(String line) {
    for (int i = 0; i < line.length(); i++) {
      char current = line.charAt(i);

      if (Character.isDigit(current)) {
        return current;
      }

      // search all the text between current and the next digit character / EOL and check if a
      // digit in word form is found
      String rest = line.substring(i);
      int nextDigitIdx = indexOfFirstDigitCharacterOrEOL(rest);

      for (int end = 0; end < nextDigitIdx; end++) {
        String maybeDigitInWordForm = rest.substring(0, end + 1);
        if (wordsToDigits.containsKey(maybeDigitInWordForm)) {
          return wordsToDigits.get(maybeDigitInWordForm);
        }
      }
    }

    throw new IllegalStateException("failed to find any kind of digit in input");
  }

  private static int indexOfFirstDigitCharacterOrEOL(String string) {
    int result = string.length();

    for (int i = 0; i < string.length(); i++) {
      if (Character.isDigit(string.charAt(i))) {
        result = i;
        break;
      }
    }

    return result;
  }

  private static char findLastDigit(String line) {
    for (int i = line.length() - 1; i >= 0; i--) {
      char current = line.charAt(i);

      if (Character.isDigit(current)) {
        return current;
      }

      String rest = line.substring(0, i + 1);
      int lastDigitIdx = indexOfLastDigitCharacterOrEOL(rest);

      for (int end = rest.length(); end >= lastDigitIdx; end--) {
        String maybeDigitInWordForm = rest.substring(end);
        if (wordsToDigits.containsKey(maybeDigitInWordForm)) {
          return wordsToDigits.get(maybeDigitInWordForm);
        }
      }
    }

    throw new IllegalStateException("failed to find any kind of digit in input");
  }

  private static int indexOfLastDigitCharacterOrEOL(String string) {
    int result = 0;

    for (int i = string.length() - 1; i >= 0; i--) {
      if (Character.isDigit(string.charAt(i))) {
        result = i;
        break;
      }
    }

    return result;
  }

}
