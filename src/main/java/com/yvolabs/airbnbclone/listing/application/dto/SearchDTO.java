package com.yvolabs.airbnbclone.listing.application.dto;

import com.yvolabs.airbnbclone.booking.application.dto.BookedDateDTO;
import com.yvolabs.airbnbclone.listing.application.dto.sub.ListingInfoDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

/**
 * @author Yvonne N
 * @version 1.0
 * @since 06/10/2024
 */

@Builder
public record SearchDTO(@Valid BookedDateDTO dates,
                        @Valid ListingInfoDTO infos,
                        @NotEmpty String location
) {
}
