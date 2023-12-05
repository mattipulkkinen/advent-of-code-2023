package com.github.mattipulkkinen.aoc2023.d3;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Main {

  public static void main(String[] args) {
    List<String> inputLines = new ArrayList<>();
    try (BufferedReader reader = Files.newBufferedReader(Path.of("input.txt"))) {
      reader.lines().forEach(inputLines::add);
    } catch (IOException e) {
      System.err.println("Failed to read input, aborting...");
      System.exit(1);
    }

    List<PartNumber> partNumbers = new ArrayList<>();
    List<Point> pointsSurroundingSymbols = new ArrayList<>();

    for (int row = 0; row < inputLines.size(); row++) {
      String currentLine = inputLines.get(row);

      for (int column = 0; column < currentLine.length(); column++) {
        char currentCharacter = currentLine.charAt(column);

        if (Character.isDigit(currentCharacter)) {
          StringBuilder fullNumber = new StringBuilder("" + currentCharacter);
          Point startingPoint = new Point(column, row);
          while (column + 1 < currentLine.length() && Character.isDigit(
              currentLine.charAt(column + 1))) {
            fullNumber.append(currentLine.charAt(column + 1));
            column++;
          }

          partNumbers.add(new PartNumber(startingPoint, Integer.parseInt(fullNumber.toString())));
        } else if (currentCharacter != '.') {
          pointsSurroundingSymbols.addAll(new Point(column, row).surroundingPoints());
        }
      }
    }

    int sumOfPartNumbersSurroundingSymbols = partNumbers.stream()
        .filter(n -> n.occupiedPoints().stream().anyMatch(pointsSurroundingSymbols::contains))
        .mapToInt(PartNumber::value).sum();

    System.out.println("The sum is " + sumOfPartNumbersSurroundingSymbols);
  }

}
