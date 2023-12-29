package com.github.mattipulkkinen.aoc2023.d5;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {

  public static void main(String[] args) {
    List<Long> seedNumbers = null;
    List<List<RangeMapping>> mappings = null;

    try (BufferedReader reader = Files.newBufferedReader(Path.of("input.txt"))) {
      seedNumbers = readSeedNumbersFromFirstInputLine(reader.readLine());
      mappings = readMappingsFromInputFile(reader);
    } catch (IOException e) {
      System.err.println("Failed to read input, aborting...");
      System.exit(1);
    }

    long lowestLocationNumber = findLowestLocationNumber(seedNumbers, mappings);
    System.out.println("The lowest location number is " + lowestLocationNumber);
  }

  private static long findLowestLocationNumber(List<Long> seedNumbers,
      List<List<RangeMapping>> mappings) {
    try (ExecutorService executor = Executors.newFixedThreadPool(
        Runtime.getRuntime().availableProcessors())) {
      List<Future<Long>> tasks = new ArrayList<>();

      for (int i = 0; i < seedNumbers.size(); i += 2) {
        long seedRangeStart = seedNumbers.get(i);
        long seedRangeEnd = seedRangeStart + seedNumbers.get(i + 1);

        // for every seed range...
        tasks.add(executor.submit(() -> {
          long lowestLocationNumber = Long.MAX_VALUE;

          for (long seedNumber = seedRangeStart; seedNumber <= seedRangeEnd; seedNumber++) {
            long mappingResult = applyMappingsToSeedNumber(mappings, seedNumber);

            if (mappingResult < lowestLocationNumber) {
              lowestLocationNumber = mappingResult;
            }
          }

          return lowestLocationNumber;
        }));
      }

      return tasks.stream().mapToLong(longFuture -> {
        try {
          return longFuture.get();
        } catch (InterruptedException | ExecutionException e) {
          throw new RuntimeException(e);
        }
      }).min().orElseThrow();
    }
  }

  private static long applyMappingsToSeedNumber(List<List<RangeMapping>> mappings,
      long seedNumber) {
    long result = seedNumber;
    for (List<RangeMapping> mapping : mappings) {
      long finalResult = result;
      result = mapping.stream().filter(m -> m.isInRange(finalResult)).findFirst()
          .orElse(new RangeMapping(finalResult, finalResult, 1)).map(finalResult);
    }
    return result;
  }

  private static List<List<RangeMapping>> readMappingsFromInputFile(BufferedReader reader)
      throws IOException {
    List<List<RangeMapping>> mappings = new ArrayList<>();

    while (true) {
      String line = reader.readLine();

      if (line == null) {
        // We are finished
        break;
      }

      if (line.isEmpty()) {
        continue;
      }

      // if a line contains : then it is a section header
      if (line.contains(":")) {
        mappings.add(new ArrayList<>());
        continue;
      }

      String[] numbers = line.split(" +");
      long dstRangeStart = Long.parseLong(numbers[0]);
      long srcRangeStart = Long.parseLong(numbers[1]);
      long rangeLength = Long.parseLong(numbers[2]);
      mappings.getLast().add(new RangeMapping(dstRangeStart, srcRangeStart, rangeLength));
    }

    return mappings;
  }

  private static List<Long> readSeedNumbersFromFirstInputLine(String line) {
    return Arrays.stream(line.split(" +")).skip(1).mapToLong(Long::parseLong)
        .collect(ArrayList::new, List::add, List::addAll);
  }
}
