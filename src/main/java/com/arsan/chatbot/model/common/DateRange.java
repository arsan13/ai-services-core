package com.arsan.chatbot.model.common;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

public record DateRange(LocalDateTime start, LocalDateTime end) {

    public DateRange {
        if (start != null && end != null && start.isAfter(end)) {
            throw new IllegalArgumentException("Start date must be before end date");
        }
    }

    public static DateRange resolve(LocalDateTime startDate, LocalDateTime endDate) {
        return resolve(startDate, endDate, Clock.systemDefaultZone());
    }

    public static DateRange resolve(LocalDateTime startDate, LocalDateTime endDate, Clock clock) {
        LocalDateTime start = Optional.ofNullable(startDate).orElse(LocalDate.now(clock).atStartOfDay());
        LocalDateTime end = Optional.ofNullable(endDate).orElse(start.toLocalDate().atTime(LocalTime.MAX));

        return new DateRange(start, end);
    }
}
