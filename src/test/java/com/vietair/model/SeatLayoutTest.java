package com.vietair.model;

import com.vietair.exception.SeatInvalidException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SeatLayoutTest {

  private static SeatLayout instance;

  @BeforeEach
  void resetSeatLayout() {
    instance = new SeatLayout();
  }

  @Test
  @SneakyThrows
  public void testGetSeat() {
    boolean[][] internalSeats = new boolean[20][8];
    internalSeats[0][0] = true;
    instance = new SeatLayout(internalSeats);

    assertTrue(instance.getSeat("A0"));
  }

  @Test
  @SneakyThrows
  public void testSetSeat() {
    instance.setSeat("A0", true);
    assertTrue(instance.getSeat("A0"));
  }

  @ParameterizedTest
  @ValueSource(strings = {"A0", "A1", "A2", "A3", "A4", "A5", "A6", "A7", "T0"})
  public void testValidateGoodSeat(String seat) {
    assertDoesNotThrow(() -> SeatLayout.validateSeat(seat));
  }

  @ParameterizedTest
  @ValueSource(strings = {"a0", "U1", "A9", "3", "A", "?", "", "AA7", "U50"})
  @SneakyThrows
  public void testValidateInvalidSeat(String seat) {
    assertThrows(SeatInvalidException.class, () -> SeatLayout.validateSeat(seat));
  }

  @ParameterizedTest
  @MethodSource("generateSeatsSource")
  @SneakyThrows
  public void testGenerateSeats(String startSeatId, int count, String[] result) {
    assertArrayEquals(SeatLayout.generateSeats(startSeatId, count), result);
  }

  private static Stream<Arguments> generateSeatsSource() {
    return Stream.of(
          Arguments.of("B0", 1, new String[] {"B0"}),
          Arguments.of("B0", 2, new String[] {"B0", "B1"}),
          Arguments.of("B0", 3, new String[] {"B0", "B1", "B2"}),
          Arguments.of("B0", 4, new String[] {"B0", "B1", "B2", "B3"}),
          Arguments.of("B0", 5, new String[] {"B0", "B1", "B2", "B3", "B4"}),
          Arguments.of("B0", 6, new String[] {"B0", "B1", "B2", "B3", "B4", "B5"}),
          Arguments.of("B0", 7, new String[] {"B0", "B1", "B2", "B3", "B4", "B5", "B6"}),
          Arguments.of("B0", 8, new String[] {"B0", "B1", "B2", "B3", "B4", "B5", "B6", "B7"})
    );
  }

  @ParameterizedTest
  @ValueSource(ints = {-1, 0})
  @SneakyThrows
  public void testGenerateSeatsBadCountInput(int count) {
    assertArrayEquals(SeatLayout.generateSeats("C0", count), new String[] {});
  }

  @Test
  public void testGenerateSeatsBadSeatInput() {
    assertThrows(SeatInvalidException.class, () -> SeatLayout.generateSeats("=0", 1));
  }
}
