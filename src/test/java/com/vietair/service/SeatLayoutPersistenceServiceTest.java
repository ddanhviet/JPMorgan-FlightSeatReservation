package com.vietair.service;

import com.vietair.exception.PersistenceException;
import com.vietair.model.SeatLayout;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


class SeatLayoutPersistenceServiceTest {
    private static final String PERSISTENCE_FILE_NAME = "vietair.seat.local";

    private static SeatLayoutPersistenceService instance;

    @BeforeAll
    static void setUp() {
        instance = new SeatLayoutPersistenceService(PERSISTENCE_FILE_NAME);
    }

    @BeforeEach
    @SneakyThrows
    void cleanUp() {
      File file = new File(PERSISTENCE_FILE_NAME);
      Files.deleteIfExists(file.toPath());
    }

    @Test
    @SneakyThrows
    public void testSerialization() {
        SeatLayout seatLayout = new SeatLayout();
        seatLayout.setSeat("A0", true);

        instance.serializeSeatLayout(seatLayout);
        var layoutFromFile = instance.deserializeSeatLayout();

        assertTrue(layoutFromFile.getSeat("A0"));
    }

    @Test
    @SneakyThrows
    public void testDeserializationNoPermission() {
        SeatLayout seatLayout = new SeatLayout();
        instance.serializeSeatLayout(seatLayout);

        File file = new File(PERSISTENCE_FILE_NAME);
        Files.setPosixFilePermissions(file.toPath(), Set.of(PosixFilePermission.OTHERS_READ));

        assertThrows(PersistenceException.class, () -> instance.deserializeSeatLayout());
    }

    @Test
    public void testDeserializationNoFile() {
      assertThrows(PersistenceException.class, () -> instance.deserializeSeatLayout());
    }
}
