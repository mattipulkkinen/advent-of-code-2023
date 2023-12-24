package com.github.mattipulkkinen.aoc2023.d4;

import java.util.List;

public record Card(int cardNumber, List<Integer> winningNumbers, List<Integer> numbersYouHave) {

  public int value() {
    return (int) numbersYouHave.stream().distinct().filter(winningNumbers::contains).count();
  }

}
