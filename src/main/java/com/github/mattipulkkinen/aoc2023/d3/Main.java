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
    List<Point> symbols = new ArrayList<>();

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
          symbols.add(new Point(column, row));
        }
      }
    }

    List<Point> gears = symbols.stream().filter(symbol -> {
      List<Point> surroundingPoints = symbol.surroundingPoints();

      return partNumbers.stream().filter(partNumber -> {
        for (Point occupiedPoint : partNumber.occupiedPoints()) {
          if (surroundingPoints.contains(occupiedPoint)) {
            return true;
          }
        }
        return false;
      }).count() == 2;
    }).toList();

    int sumOfAllGearRatios = gears.stream().map(gear -> partNumbers.stream().filter(partNumber -> {
          for (Point occupiedPoint : partNumber.occupiedPoints()) {
            if (gear.surroundingPoints().contains(occupiedPoint)) {
              return true;
            }
          }
          return false;
        }).mapToInt(PartNumber::value).reduce(1, (subproduct, elem) -> subproduct * elem))
        .mapToInt(i -> i).sum();

    System.out.println("The sum of all gear ratios in the schematic is " + sumOfAllGearRatios);
  }

}
