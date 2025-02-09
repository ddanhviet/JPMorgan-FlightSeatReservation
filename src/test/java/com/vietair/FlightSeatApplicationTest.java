package com.vietair;

import com.vietair.service.SeatBookService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FlightSeatApplicationTest {

  private static ByteArrayOutputStream outContent;

  private static SeatBookService seatBookService;
  private static FlightSeatApplication instance;

  @BeforeAll
  @SneakyThrows
  static void setUp() {
    seatBookService = mock(SeatBookService.class);
    instance = new FlightSeatApplication(seatBookService);
  }

  @BeforeEach
  void resetOutStream() {
    outContent = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outContent));
  }

  @Test
  public void testInvalidAction() {
    instance.handleInput(new String[] {"QUERY", "A0", "1"});
    assertEquals("FAIL\n", outContent.toString());
  }

  @Test
  public void testInvalidSeat() {
    instance.handleInput(new String[] {"BOOK", "U1", "1"});
    assertEquals("FAIL\n", outContent.toString());
  }

  @Test
  @SneakyThrows
  public void testSameBook() {
    when(seatBookService.bookSeats("A1", 1))
          .thenReturn(true)
          .thenReturn(false);

    instance.handleInput(new String[] {"BOOK", "A0", "1"});
    instance.handleInput(new String[] {"BOOK", "A0", "1"});
    assertEquals("SUCCESS\nFAIL\n", outContent.toString());
  }

  @Test
  @SneakyThrows
  public void testBookAndCancel() {
    when(seatBookService.bookSeats("A1", 1)).thenReturn(true);
    when(seatBookService.cancelSeats("A1", 1)).thenReturn(true);

    instance.handleInput(new String[] {"BOOK", "A1", "1"});
    instance.handleInput(new String[] {"CANCEL", "A1", "1"});
    assertEquals("SUCCESS\nSUCCESS\n", outContent.toString());
  }

  @Test
  @SneakyThrows
  public void testCorruptSeatLayout() {
    when(seatBookService.bookSeats("U6", 2)).thenThrow(new NullPointerException());
    instance.handleInput(new String[] {"BOOK", "U6", "2"});
    assertEquals("FAIL\n", outContent.toString());
  }
}
