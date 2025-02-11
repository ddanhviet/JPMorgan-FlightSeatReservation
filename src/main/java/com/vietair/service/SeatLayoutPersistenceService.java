package com.vietair.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vietair.exception.PersistenceException;
import com.vietair.model.SeatLayout;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;

@Slf4j
public class SeatLayoutPersistenceService {

  private final ObjectMapper objectMapper;
  private String filename;

  public SeatLayoutPersistenceService(String filename) {
    log.debug("file in use {}", filename);
    this.filename = filename;
    objectMapper = new ObjectMapper();
  }

  public void serializeSeatLayout(SeatLayout seatLayout) throws PersistenceException {
    serializeSeatLayout(seatLayout, filename);
  }

  public void serializeSeatLayout(SeatLayout seatLayout, String anyFilename) throws PersistenceException {
    try {
      File f = new File(anyFilename);
      objectMapper
            .writerWithDefaultPrettyPrinter()
            .writeValue(f, seatLayout);
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
      return objectMapper.readValue(f, SeatLayout.class);
    } catch (IOException e) {
      throw new PersistenceException("Unable to obtain seat layout from file", e);
    }
  }

  public void updateFilename(String filename) {
    this.filename = filename;
  }
}
