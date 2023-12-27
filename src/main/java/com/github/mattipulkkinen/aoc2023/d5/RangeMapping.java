package com.github.mattipulkkinen.aoc2023.d5;

public record RangeMapping(long dstRangeStart, long srcRangeStart, long rangeLength) {

  public long map(long src) {
    if (src < srcRangeStart || srcRangeStart + rangeLength < src) {
      throw new IllegalArgumentException("given src number outside of range");
    }

    long difference = src - srcRangeStart;

    return dstRangeStart + difference;
  }

  public boolean isInRange(long n) {
    return srcRangeStart <= n && n <= srcRangeStart + rangeLength;
  }
}
