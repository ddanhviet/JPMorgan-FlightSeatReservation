package com.vietair.utility;

public class CharacterUtils {

  public static char increaseCharDigit(char c) {
    if (!Character.isDigit(c))
      return c;
    return (char) ((c - '0' + 1) % 10 + '0');
  }
}
