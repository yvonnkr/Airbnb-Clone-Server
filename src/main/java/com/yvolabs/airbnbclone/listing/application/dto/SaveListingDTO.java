package com.yvolabs.airbnbclone.listing.application.dto;


import com.yvolabs.airbnbclone.listing.application.dto.sub.DescriptionDTO;
import com.yvolabs.airbnbclone.listing.application.dto.sub.ListingInfoDTO;
import com.yvolabs.airbnbclone.listing.application.dto.sub.PictureDTO;
import com.yvolabs.airbnbclone.listing.application.dto.vo.PriceVO;
import com.yvolabs.airbnbclone.listing.domain.BookingCategory;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Yvonne N
 * @version 1.0
 * @since 20/09/2024
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SaveListingDTO {

    @NotNull
    BookingCategory category;

    @NotNull
    String location;

    @NotNull
    @Valid
    ListingInfoDTO infos;

    @NotNull
    @Valid
    DescriptionDTO description;

    @NotNull
    @Valid
    PriceVO price;

    @NotNull
    List<PictureDTO> pictures;

}