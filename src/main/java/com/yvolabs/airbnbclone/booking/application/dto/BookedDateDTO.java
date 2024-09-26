package com.yvolabs.airbnbclone.booking.application.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.OffsetDateTime;

/**
 * @author Yvonne N
 * @version 1.0
 * @since 26/09/2024
 */

@Builder
public record BookedDateDTO(
        @NotNull OffsetDateTime startDate,
        @NotNull OffsetDateTime endDate
) {
}
