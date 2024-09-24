package com.yvolabs.airbnbclone.listing.application.dto;

import com.yvolabs.airbnbclone.listing.application.dto.sub.PictureDTO;
import com.yvolabs.airbnbclone.listing.application.dto.vo.PriceVO;
import com.yvolabs.airbnbclone.listing.domain.BookingCategory;
import lombok.Builder;

import java.util.UUID;

/**
 * @author Yvonne N
 * @version 1.0
 * @since 24/09/2024
 */

@Builder
public record DisplayCardListingDTO(PriceVO price,
                                    String location,
                                    PictureDTO cover,
                                    BookingCategory bookingCategory,
                                    UUID publicId) {
}