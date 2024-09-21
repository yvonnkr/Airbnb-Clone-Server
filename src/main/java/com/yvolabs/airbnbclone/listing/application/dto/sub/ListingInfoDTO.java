package com.yvolabs.airbnbclone.listing.application.dto.sub;

import com.yvolabs.airbnbclone.listing.application.dto.vo.BathsVO;
import com.yvolabs.airbnbclone.listing.application.dto.vo.BedroomsVO;
import com.yvolabs.airbnbclone.listing.application.dto.vo.BedsVO;
import com.yvolabs.airbnbclone.listing.application.dto.vo.GuestsVO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

/**
 * @author Yvonne N
 * @version 1.0
 * @since 20/09/2024
 */

@Builder
public record ListingInfoDTO(
        @NotNull @Valid GuestsVO guests,
        @NotNull @Valid BedroomsVO bedrooms,
        @NotNull @Valid BedsVO beds,
        @NotNull @Valid BathsVO baths) {
}