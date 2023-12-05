package com.github.mattipulkkinen.aoc2023.d3;

import java.util.ArrayList;
import java.util.List;

public record PartNumber(Point startingPoint, int value) {

  public List<Point> occupiedPoints() {
    List<Point> result = new ArrayList<>();

    int length = Integer.toString(value).length();
    for (int i = 0; i < length; i++) {
      result.add(new Point(startingPoint().x() + i, startingPoint.y()));
    }
    return result;
  }
}
