package com.yvolabs.airbnbclone.booking.application.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * @author Yvonne N
 * @version 1.0
 * @since 26/09/2024
 */

@Builder
public record NewBookingDTO(
        @NotNull OffsetDateTime startDate,
        @NotNull OffsetDateTime endDate,
        @NotNull UUID listingPublicId
) {
}
