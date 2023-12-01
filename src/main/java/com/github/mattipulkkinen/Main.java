package com.github.mattipulkkinen;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Main {

  public static void main(String[] args) {
    List<Integer> calibrationValues = new ArrayList<>();
    try (BufferedReader reader = Files.newBufferedReader(Path.of("input.txt"))) {
      reader.lines().forEach(line -> {
        char firstDigit = ' ';
        char lastDigit = ' ';

        // find the first digit
        for (int i = 0; i < line.length(); i++) {
          char current = line.charAt(i);
          if (Character.isDigit(current)) {
            firstDigit = current;
            break;
          }
        }
        // find the last digit
        for (int i = line.length() - 1; i >= 0; i--) {
          char current = line.charAt(i);
          if (Character.isDigit(current)) {
            lastDigit = current;
            break;
          }
        }

        if (firstDigit == ' ' || lastDigit == ' ') {
          throw new IllegalStateException("failed to find both digits for line \"" + line + "\"");
        }

        int calibrationValue = Integer.parseInt(String.valueOf(firstDigit) + lastDigit);
        calibrationValues.add(calibrationValue);
      });
    } catch (IOException e) {
      System.err.println("Failed to open file for reading: " + e);
      return;
    }

    int sum = calibrationValues.stream().reduce(0, Integer::sum);
    System.out.println(sum);
  }
}
