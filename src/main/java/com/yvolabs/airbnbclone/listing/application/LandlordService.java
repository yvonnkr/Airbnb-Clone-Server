package com.yvolabs.airbnbclone.listing.application;


import com.yvolabs.airbnbclone.listing.application.dto.CreatedListingDTO;
import com.yvolabs.airbnbclone.listing.application.dto.SaveListingDTO;
import com.yvolabs.airbnbclone.listing.application.dto.sub.PictureDTO;
import com.yvolabs.airbnbclone.listing.domain.Listing;
import com.yvolabs.airbnbclone.listing.repository.ListingRepository;
import com.yvolabs.airbnbclone.user.application.Auth0Service;
import com.yvolabs.airbnbclone.user.application.UserService;
import com.yvolabs.airbnbclone.user.application.dto.ReadUserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.yvolabs.airbnbclone.listing.mapper.ListingMapper.INSTANCE;

/**
 * @author Yvonne N
 * @version 1.0
 * @since 21/09/2024
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class LandlordService {
    private final ListingRepository listingRepository;
    private final UserService userService;
    private final Auth0Service auth0Service;
    private final PictureService pictureService;

    public CreatedListingDTO create(SaveListingDTO saveListingDTO) {
        Listing newListing = INSTANCE.saveListingDTOToListing(saveListingDTO);

        ReadUserDTO userConnected = userService.getAuthenticatedUserFromSecurityContext();
        newListing.setLandlordPublicId(userConnected.publicId());

        Listing savedListing = listingRepository.saveAndFlush(newListing);

        List<PictureDTO> pictureDTOS = pictureService.saveAll(saveListingDTO.getPictures(), savedListing);
        log.info("pictureService.saveAll() success: pictureDTOS. size: {}", pictureDTOS.size());

        auth0Service.addLandlordRoleToUser(userConnected);

        return INSTANCE.listingToCreatedListingDTO(savedListing);
    }

}
