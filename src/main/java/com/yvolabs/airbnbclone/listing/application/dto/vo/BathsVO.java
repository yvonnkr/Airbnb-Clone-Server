package com.yvolabs.airbnbclone.listing.application.dto.vo;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

/**
 * @author Yvonne N
 * @version 1.0
 * @since 20/09/2024
 */

@Builder
public record BathsVO(@NotNull(message = "Bath value must be present") int value) {
}