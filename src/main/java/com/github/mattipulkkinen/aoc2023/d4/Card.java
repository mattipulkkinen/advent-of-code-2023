package com.github.mattipulkkinen.aoc2023.d4;

import java.util.List;

public record Card(int cardNumber, List<Integer> winningNumbers, List<Integer> numbersYouHave) {

  public int value() {
    return numbersYouHave.stream().distinct().filter(winningNumbers::contains)
        .reduce(1, (subproduct, elem) -> subproduct * 2) / 2;
  }
}
