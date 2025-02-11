package com.vietair.service;

import com.vietair.exception.PersistenceException;
import com.vietair.exception.SeatAlreadyReservedException;
import com.vietair.exception.SeatInvalidException;
import com.vietair.exception.SeatNotReservedException;
import com.vietair.exception.SeatOperationException;
import com.vietair.model.SeatLayout;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

@Slf4j
public class SeatBookService {

  static final String LAYOUT_FILENAME = "vietair.seat";

  SeatLayout layout;
  SeatOperation seatOperation;

  SeatLayoutPersistenceService seatLayoutPersistenceService;

  public SeatBookService() throws PersistenceException {
    this(new SeatLayoutPersistenceService(LAYOUT_FILENAME));
  }

  public SeatBookService(SeatLayoutPersistenceService seatLayoutPersistenceService) throws PersistenceException {
    this.seatLayoutPersistenceService = seatLayoutPersistenceService;

    File f = new File(LAYOUT_FILENAME);
    if (!f.exists())
      layout = new SeatLayout();
    else layout = seatLayoutPersistenceService.deserializeSeatLayout();

    seatOperation = (wantedSeats, supposedState, thrower) -> {
      for (var seatId : wantedSeats) {
        if (layout.getSeat(seatId) != supposedState) {
          thrower.throwSeatOperationException(seatId);
        }
        layout.setSeat(seatId, !supposedState);
        log.info("Set seat {} to {}", seatId, !supposedState);
      }

      try {
        seatLayoutPersistenceService.serializeSeatLayout(layout);
      } catch (PersistenceException pe) {
        log.error("Cannot save the new seat layout", pe);
        return false;
      }
      return true;
    };
  }

  public boolean bookSeats(String startSeatId, int count) throws SeatInvalidException, SeatOperationException {
    return seatOperation.apply(
          SeatLayout.generateSeats(startSeatId, count),
          false,
          seatId -> {
            throw new SeatAlreadyReservedException("Seat " + seatId + " is already reserved");
          }
    );
  }

  public boolean cancelSeats(String startSeatId, int count) throws SeatInvalidException, SeatOperationException {
    return seatOperation.apply(
          SeatLayout.generateSeats(startSeatId, count),
          true,
          seatId -> {
            throw new SeatNotReservedException("Seat " + seatId + " is not reserved");
          }
    );
  }

  @FunctionalInterface
  interface SeatOperation {
    boolean apply(String[] wantedSeats, boolean supposedState, SeatLayoutExceptionThrower thrower) throws SeatOperationException, SeatInvalidException;
  }

  @FunctionalInterface
  interface SeatLayoutExceptionThrower {
    void throwSeatOperationException(String seatId) throws SeatOperationException;
  }

}
