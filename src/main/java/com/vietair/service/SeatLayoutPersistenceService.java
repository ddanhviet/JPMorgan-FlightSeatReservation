package com.vietair.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vietair.exception.PersistenceException;
import com.vietair.model.SeatLayout;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;

@Slf4j
public class SeatLayoutPersistenceService {

  private final String filename;
  private final ObjectMapper objectMapper;

  public SeatLayoutPersistenceService(String filename) {
    log.debug("file in use {}", filename);
    this.filename = filename;
    objectMapper = new ObjectMapper();
  }

  public void serializeSeatLayout(SeatLayout seatLayout) throws PersistenceException {
    serializeSeatLayout(seatLayout, filename);
  }

  public void serializeSeatLayout(SeatLayout seatLayout, String anyFilename) throws PersistenceException {
    //try (FileWriter fileWriter = new FileWriter(filename)) {
    try {
      //String jsonString = objectMapper.writeValueAsString(seatLayout);
      //log.debug("Seat Layout to disk {}", (Object) seatLayout.seats);
      //log.debug("Seat Layout JSON to disk {}", jsonString);
      //fileWriter.write(jsonString);

      File f = new File(anyFilename);
      objectMapper.writeValue(f, seatLayout);
    } catch (IOException e) {
      log.error("Unable to persist seat layout to file", e);
      throw new PersistenceException();
    }
  }

  public SeatLayout deserializeSeatLayout() throws PersistenceException {
    return deserializeSeatLayout(filename);
  }

  public SeatLayout deserializeSeatLayout(String anyFilename) throws PersistenceException {
    try {
      File f = new File(anyFilename);
      //log.debug("Seat Layout JSON {}", seatLayoutJson);
      return objectMapper.readValue(f, SeatLayout.class);
    } catch (IOException e) {
      throw new PersistenceException("Unable to obtain seat layout from file", e);
    }
  }
}
