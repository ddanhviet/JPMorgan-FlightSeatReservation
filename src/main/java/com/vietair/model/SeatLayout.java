package com.vietair.model;

import com.vietair.exception.SeatInvalidException;
import com.vietair.utility.CharacterUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 20 rows, | xx _ xxxx _ xx |
 */
@Slf4j
@Data
public class SeatLayout {

  private static final String SEAT_REGEX = "[A-T][0-7]";

  private final boolean[][] seats;

  public SeatLayout() {
    seats = new boolean[20][8];
  }

  public SeatLayout(boolean[][] seats) {
    this.seats = seats;
  }

  public boolean getSeat(String seat) throws SeatInvalidException {
    int[] seatComponents = parseSeat(seat);
    return seats[seatComponents[0]][seatComponents[1]];
  }

  public void setSeat(String seat, boolean reserveState) throws SeatInvalidException {
    var seatComponents = parseSeat(seat);
    seats[seatComponents[0]][seatComponents[1]] = reserveState;
  }

  public static String[] generateSeats(String seatId, int count) {
    String[] seats = new String[count];
    StringBuilder sb = new StringBuilder(seatId);
    for (int i = 0; i < count; i++) {
      sb.setCharAt(1, CharacterUtils.increaseCharDigit(sb.charAt(1)));
      seats[i] = sb.toString();
    }

    return seats;
  }

  static void validateSeat(String seat) throws SeatInvalidException {
    if (!seat.matches(SEAT_REGEX))
      throw new SeatInvalidException("Invalid seat " + seat + ". It must follow format " + SEAT_REGEX);
  }

  private static int[] parseSeat(String seat) throws SeatInvalidException {
    validateSeat(seat);
    var seatComponents = new int[]{
          convertRow(seat.charAt(0)),
          seat.charAt(1) - '0'
    };
    log.debug("Parsed seat {}", seatComponents);
    return seatComponents;
  }

  private static int convertRow(char c) {
    return Character.toUpperCase(c) - 'A';
  }
}
