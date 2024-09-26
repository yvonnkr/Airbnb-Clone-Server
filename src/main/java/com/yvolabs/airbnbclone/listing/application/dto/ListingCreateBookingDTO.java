package com.yvolabs.airbnbclone.listing.application.dto;

import com.yvolabs.airbnbclone.listing.application.dto.vo.PriceVO;
import lombok.Builder;

import java.util.UUID;

/**
 * @author Yvonne N
 * @version 1.0
 * @since 26/09/2024
 */
@Builder
public record ListingCreateBookingDTO(
        UUID listingPublicId, PriceVO price
) {
}
