package com.vietair.service;

import com.vietair.exception.PersistenceException;
import com.vietair.exception.SeatAlreadyReservedException;
import com.vietair.exception.SeatInvalidException;
import com.vietair.exception.SeatNotReservedException;
import com.vietair.model.SeatLayout;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SeatBookServiceTest {

  private static SeatLayoutPersistenceService seatLayoutPersistenceService;

  private static SeatBookService instance;

  @BeforeAll
  @SneakyThrows
  static void setUp() {
    seatLayoutPersistenceService = mock(SeatLayoutPersistenceService.class);
  }

  @BeforeEach
  void reset() {
    File f = new File(SeatBookService.LAYOUT_FILENAME);
    f.delete();

    Mockito.reset(seatLayoutPersistenceService);
  }

  @Test
  @SneakyThrows
  public void testBookSeat() {
    try (MockedConstruction<SeatLayout> seatLayoutMockedConstruction = Mockito.mockConstruction(SeatLayout.class, (mock, context) -> {
      when(mock.getSeat(anyString())).thenReturn(false);
    })) {
      instance = new SeatBookService(seatLayoutPersistenceService);
      instance.bookSeats("A0", 1);

      assertEquals(1, seatLayoutMockedConstruction.constructed().size());
      var mockSeatLayout = seatLayoutMockedConstruction.constructed().getFirst();

      verify(mockSeatLayout, times(1)).getSeat(anyString());
      verify(mockSeatLayout, times(1)).setSeat(anyString(), eq(true));
    }
  }

  @Test
  @SneakyThrows
  public void testBookOccupiedSeat() {
    try (MockedConstruction<SeatLayout> seatLayoutMockedConstruction = Mockito.mockConstruction(SeatLayout.class, (mock, context) -> {
      when(mock.getSeat(anyString())).thenReturn(true);
    })) {
      instance = new SeatBookService(seatLayoutPersistenceService);
      assertThrows(SeatAlreadyReservedException.class, () -> instance.bookSeats("T0", 1));

      assertEquals(1, seatLayoutMockedConstruction.constructed().size());
      var mockSeatLayout = seatLayoutMockedConstruction.constructed().getFirst();

      verify(mockSeatLayout, times(1)).getSeat(anyString());
    }
  }

  @Test
  @SneakyThrows
  public void testCancelSeat() {
    try (MockedConstruction<SeatLayout> seatLayoutMockedConstruction = Mockito.mockConstruction(SeatLayout.class, (mock, context) -> {
      when(mock.getSeat(anyString())).thenReturn(true);
    })) {
      instance = new SeatBookService(seatLayoutPersistenceService);
      instance.cancelSeats("A0", 1);

      assertEquals(1, seatLayoutMockedConstruction.constructed().size());
      var mockSeatLayout = seatLayoutMockedConstruction.constructed().getFirst();

      verify(mockSeatLayout, times(1)).getSeat(anyString());
      verify(mockSeatLayout, times(1)).setSeat(anyString(), eq(false));
    }
  }

  @Test
  @SneakyThrows
  public void testCancelEmptySeat() {
    try (MockedConstruction<SeatLayout> seatLayoutMockedConstruction = Mockito.mockConstruction(SeatLayout.class, (mock, context) -> {
      when(mock.getSeat(anyString())).thenReturn(false);
    })) {
      instance = new SeatBookService(seatLayoutPersistenceService);
      assertThrows(SeatNotReservedException.class, () -> instance.cancelSeats("T0", 1));

      assertEquals(1, seatLayoutMockedConstruction.constructed().size());
      var mockSeatLayout = seatLayoutMockedConstruction.constructed().getFirst();

      verify(mockSeatLayout, times(1)).getSeat(anyString());
    }
  }

  @Test
  @SneakyThrows
  public void testProvidingInvalidSeats() {
    try (MockedStatic<SeatLayout> seatLayoutMockedStatic = mockStatic(SeatLayout.class)) {
      seatLayoutMockedStatic.when(() -> SeatLayout.generateSeats(anyString(), anyInt())).thenThrow(SeatInvalidException.class);
      instance = new SeatBookService(seatLayoutPersistenceService);
      assertThrows(SeatInvalidException.class, () -> instance.bookSeats("JPMorgan", 2));
    }
  }

  @Test
  @SneakyThrows
  public void testNoSeatLayout() {
    instance = new SeatBookService(seatLayoutPersistenceService);
    verify(seatLayoutPersistenceService, times(0)).deserializeSeatLayout();
  }

  @Test
  @SneakyThrows
  public void testExistSeatLayout() {
    File f = new File(SeatBookService.LAYOUT_FILENAME);
    f.createNewFile();

    instance = new SeatBookService(seatLayoutPersistenceService);
    verify(seatLayoutPersistenceService, times(1)).deserializeSeatLayout();
  }

  @Test
  @SneakyThrows
  public void testFailToLoadSeatLayout() {
    File f = new File(SeatBookService.LAYOUT_FILENAME);
    f.createNewFile();

    when(seatLayoutPersistenceService.deserializeSeatLayout()).thenThrow(new PersistenceException());
    assertThrows(PersistenceException.class, () -> instance = new SeatBookService(seatLayoutPersistenceService));
  }
}
