package com.yvolabs.airbnbclone.listing.application.dto;

import com.yvolabs.airbnbclone.listing.application.dto.sub.DescriptionDTO;
import com.yvolabs.airbnbclone.listing.application.dto.sub.LandlordListingDTO;
import com.yvolabs.airbnbclone.listing.application.dto.sub.ListingInfoDTO;
import com.yvolabs.airbnbclone.listing.application.dto.sub.PictureDTO;
import com.yvolabs.airbnbclone.listing.application.dto.vo.PriceVO;
import com.yvolabs.airbnbclone.listing.domain.BookingCategory;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author Yvonne N
 * @version 1.0
 * @since 25/09/2024
 */

@Getter
@Setter
@Builder
public class DisplayListingDTO {
    private DescriptionDTO description;
    private List<PictureDTO> pictures;
    private ListingInfoDTO infos;
    private PriceVO price;
    private BookingCategory category;
    private String location;
    private LandlordListingDTO landlord;
}
