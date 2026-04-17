package com.arsan.chatbot.model;

import com.arsan.chatbot.model.common.DateRange;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DateRangeTest {

    @Test
    void testConstructor_ValidDates() {
        LocalDateTime start = LocalDateTime.of(2026, 1, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2026, 1, 2, 0, 0);

        DateRange range = new DateRange(start, end);

        assertEquals(start, range.start());
        assertEquals(end, range.end());
    }

    @Test
    void testConstructor_StartAfterEnd_Throws() {
        LocalDateTime start = LocalDateTime.of(2026, 1, 2, 0, 0);
        LocalDateTime end = LocalDateTime.of(2026, 1, 1, 0, 0);

        assertThrows(IllegalArgumentException.class, () -> new DateRange(start, end));
    }

    @Test
    void testResolve_NullDates_FillsDefaults() {
        Clock fixedClock = Clock.fixed(
                java.time.Instant.parse("2026-01-01T10:15:30Z"),
                java.time.ZoneId.systemDefault()
        );

        DateRange range = DateRange.resolve(null, null, fixedClock);

        assertEquals(LocalDateTime.of(2026, 1, 1, 0, 0), range.start());
        assertEquals(LocalDateTime.of(2026, 1, 1, 23, 59, 59, 999_999_999), range.end().withNano(999_999_999));
    }
}