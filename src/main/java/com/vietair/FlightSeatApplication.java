package com.vietair;

import com.vietair.exception.PersistenceException;
import com.vietair.exception.SeatInvalidException;
import com.vietair.exception.SeatOperationException;
import com.vietair.service.SeatBookService;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class FlightSeatApplication {

  private static Map<String, InputProcess<String, Integer>> generateActionMap(SeatBookService seatBookService) {
    return Map.of(
          "BOOK", seatBookService::bookSeats,
          "CANCEL", seatBookService::cancelSeats);
  }

  private final Map<String, InputProcess<String, Integer>> actionMap;

  public FlightSeatApplication(SeatBookService seatBookService) throws PersistenceException {
    actionMap = generateActionMap(seatBookService);
  }

  void handleInput(String[] commandArray) {
    boolean executionSuccess;
    log.info("Seat operation {} received", (Object) commandArray);
    try {
      executionSuccess = actionMap
            .get(commandArray[0])
            .processInput(commandArray[1], Integer.parseInt(commandArray[2]));
    } catch (SeatInvalidException | SeatOperationException exception) {
      log.error("Failed to execute SeatBookService operation with input {}", commandArray, exception);
      executionSuccess = false;
    } catch (Exception e) {
      log.warn("Unable to complete seat operations with input {}", commandArray, e); // TODO test wrong action, null seat layout
      executionSuccess = false;
    }

    if (executionSuccess)
      System.out.println("SUCCESS");
    else System.out.println("FAIL");
  }

  @FunctionalInterface
  interface InputProcess<String, Integer> {
    boolean processInput(String seatInput, Integer countInput) throws SeatInvalidException, SeatOperationException;
  }

  public static void main(String[] args) {
    try {
      SeatBookService sbs = new SeatBookService();
      FlightSeatApplication fsa = new FlightSeatApplication(sbs);

      fsa.handleInput(args);
    } catch (PersistenceException pe) {
      log.error("Cannot create flight seat application", pe);
      System.out.println("FAIL");
    }

  }
}