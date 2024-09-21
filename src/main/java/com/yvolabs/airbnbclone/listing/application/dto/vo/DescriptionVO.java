package com.yvolabs.airbnbclone.listing.application.dto.vo;


import jakarta.validation.constraints.NotNull;
import lombok.Builder;

/**
 * @author Yvonne N
 * @version 1.0
 * @since 20/09/2024
 */

@Builder
public record DescriptionVO(@NotNull(message = "Description value must be present") String value) {
}