package com.yvolabs.airbnbclone.listing.mapper;

import com.yvolabs.airbnbclone.listing.application.dto.CreatedListingDTO;
import com.yvolabs.airbnbclone.listing.application.dto.DisplayCardListingDTO;
import com.yvolabs.airbnbclone.listing.application.dto.SaveListingDTO;
import com.yvolabs.airbnbclone.listing.application.dto.vo.PriceVO;
import com.yvolabs.airbnbclone.listing.domain.Listing;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author Yvonne N
 * @version 1.0
 * @since 18/09/2024
 */

@Mapper(componentModel = "spring", uses = {ListingPictureMapper.class})
public interface ListingMapper {

    ListingMapper INSTANCE = Mappers.getMapper(ListingMapper.class);

    @Mapping(target = "landlordPublicId", ignore = true)
    @Mapping(target = "publicId", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "pictures", ignore = true)
    @Mapping(target = "title", source = "description.title.value")
    @Mapping(target = "description", source = "description.description.value")
    @Mapping(target = "bedrooms", source = "infos.bedrooms.value")
    @Mapping(target = "guests", source = "infos.guests.value")
    @Mapping(target = "bookingCategory", source = "category")
    @Mapping(target = "beds", source = "infos.beds.value")
    @Mapping(target = "bathrooms", source = "infos.baths.value")
    @Mapping(target = "price", source = "price.value")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Listing saveListingDTOToListing(SaveListingDTO saveListingDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    CreatedListingDTO listingToCreatedListingDTO(Listing listing);

    @Mapping(target = "cover", source = "pictures")
    List<DisplayCardListingDTO> listingToDisplayCardListingDTOs(List<Listing> listings);

    @Mapping(target = "cover", source = "pictures", qualifiedByName = "extract-cover")
    DisplayCardListingDTO listingToDisplayCardListingDTO(Listing listing);

    default PriceVO mapPriceToPriceVO(int price) {
        return new PriceVO(price);
    }


}
