package com.github.mattipulkkinen;

import java.util.ArrayList;
import java.util.List;

public record GameRound(int id, List<SetOfCubes> cubeSets) {

  public int powerOfMinimumSet() {
    List<Integer> reds = new ArrayList<>(cubeSets.size());
    List<Integer> greens = new ArrayList<>(cubeSets.size());
    List<Integer> blues = new ArrayList<>(cubeSets.size());

    cubeSets.forEach(set -> {
      reds.add(set.reds());
      greens.add(set.greens());
      blues.add(set.blues());
    });

    return
        reds.stream().mapToInt(i -> i).max().orElse(0) *
            greens.stream().mapToInt(i -> i).max().orElse(0) *
            blues.stream().mapToInt(i -> i).max().orElse(0);
  }
}