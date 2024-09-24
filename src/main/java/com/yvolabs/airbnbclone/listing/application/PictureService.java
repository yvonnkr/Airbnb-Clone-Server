package com.yvolabs.airbnbclone.listing.application;

import com.yvolabs.airbnbclone.listing.application.dto.sub.PictureDTO;
import com.yvolabs.airbnbclone.listing.domain.Listing;
import com.yvolabs.airbnbclone.listing.domain.ListingPicture;
import com.yvolabs.airbnbclone.listing.mapper.ListingPictureMapper;
import com.yvolabs.airbnbclone.listing.repository.ListingPictureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * @author Yvonne N
 * @version 1.0
 * @since 21/09/2024
 */

@Service
@RequiredArgsConstructor
public class PictureService {
    private final ListingPictureRepository listingPictureRepository;
    private final ListingPictureMapper listingPictureMapper;


    public List<PictureDTO> saveAll(List<PictureDTO> pictures, Listing listing) {
        Set<ListingPicture> listingPictures = listingPictureMapper.pictureDTOsToListingPictures(pictures);

        boolean isFirst = true;

        for (ListingPicture listingPicture : listingPictures) {
            listingPicture.setCover(isFirst);
            listingPicture.setListing(listing);
            isFirst = false;
        }

        listingPictureRepository.saveAll(listingPictures);
        return listingPictureMapper.listingPictureToPictureDTO(listingPictures.stream().toList());
    }
}
