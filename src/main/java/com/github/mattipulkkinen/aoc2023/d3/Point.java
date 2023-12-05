package com.github.mattipulkkinen.aoc2023.d3;

import java.util.ArrayList;
import java.util.List;

public record Point(int x, int y) {

  public List<Point> surroundingPoints() {
    List<Point> surroundingPoints = new ArrayList<>(8);

    for (int column = x - 1; column <= x + 1; column++) {
      for (int row = y - 1; row <= y + 1; row++) {
        surroundingPoints.add(new Point(column, row));
      }
    }

    // the center point is not a "surrounding" point, so remove it from the result
    surroundingPoints.remove(new Point(x, y));

    return surroundingPoints;
  }
}
