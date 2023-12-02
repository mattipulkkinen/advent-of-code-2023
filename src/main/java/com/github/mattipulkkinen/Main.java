package com.github.mattipulkkinen;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class Main {

  public static void main(String[] args) throws IOException {
    List<GameRound> rounds;
    try (BufferedReader reader = Files.newBufferedReader(Path.of("input.txt"))) {
      rounds = reader.lines().map(Main::readGameFromInputLine).toList();
    }

    int sumOfIds = rounds.stream().filter(Main::gameFitsGivenCriteria).mapToInt(GameRound::id)
        .sum();

    System.out.println("The sum is " + sumOfIds);
  }

  private static boolean gameFitsGivenCriteria(GameRound round) {
    final int MAXIMUM_REDS = 12;
    final int MAXIMUM_GREENS = 13;
    final int MAXIMUM_BLUES = 14;

    List<SetOfCubes> cubes = round.cubeSets();
    return cubes.stream().allMatch(
        setOfCubes -> setOfCubes.reds() <= MAXIMUM_REDS && setOfCubes.greens() <= MAXIMUM_GREENS
            && setOfCubes.blues() <= MAXIMUM_BLUES);
  }

  private static GameRound readGameFromInputLine(String line) {
    // the only : on the line should be at the start, separating the game ID from the cube sets
    // including a space in the splitting pattern avoids whitespace at the start of the set list
    String[] idAndSetsSplit = line.split(": ");

    // the game ID always comes after the word "Game" so it is the second element of this split
    int gameId = Integer.parseInt(idAndSetsSplit[0].split(" ")[1]);

    // the cube sets themselves are always separated by semicolons
    String[] setsSplit = idAndSetsSplit[1].split(";");
    Arrays.parallelSetAll(setsSplit, (i) -> setsSplit[i].trim());

    List<SetOfCubes> cubeSets = Arrays.stream(setsSplit).map(Main::readCubeSetFromString).toList();

    return new GameRound(gameId, cubeSets);
  }

  private static SetOfCubes readCubeSetFromString(String input) {
    int reds = 0;
    int greens = 0;
    int blues = 0;

    String[] cubes = input.split(", ");
    for (String cubeCount : cubes) {
      String[] countAndColor = cubeCount.split(" ");
      int count = Integer.parseInt(countAndColor[0]);
      String color = countAndColor[1];

      switch (color) {
        case "red" -> reds = count;
        case "green" -> greens = count;
        case "blue" -> blues = count;
        default -> throw new IllegalStateException("unknown color identifier: " + color);
      }
    }

    return new SetOfCubes(reds, greens, blues);
  }
}
