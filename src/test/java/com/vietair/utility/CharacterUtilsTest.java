package com.vietair.utility;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CharacterUtilsTest {

  @ParameterizedTest
  @MethodSource("digitTestSource")
  void testIncreaseCharDigit(Character startDigit, Character endDigit) {
    assertEquals(CharacterUtils.increaseCharDigit(startDigit), endDigit);
  }

  private static Stream<Arguments> digitTestSource() {
    return Stream.of(
          Arguments.of('0','1'),
          Arguments.of('1','2'),
          Arguments.of('2','3'),
          Arguments.of('3','4'),
          Arguments.of('4','5'),
          Arguments.of('5','6'),
          Arguments.of('6','7'),
          Arguments.of('7','8'),
          Arguments.of('8','9'),
          Arguments.of('9','0')
    );
  }

  @ParameterizedTest
  @ValueSource(chars = { 'a', '/', ',' })
  void testInvalidCharacter(char c) {
    assertEquals(CharacterUtils.increaseCharDigit(c), c);
  }
}
