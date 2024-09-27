package com.yvolabs.airbnbclone.listing.application;


import com.yvolabs.airbnbclone.listing.application.dto.CreatedListingDTO;
import com.yvolabs.airbnbclone.listing.application.dto.DisplayCardListingDTO;
import com.yvolabs.airbnbclone.listing.application.dto.ListingCreateBookingDTO;
import com.yvolabs.airbnbclone.listing.application.dto.SaveListingDTO;
import com.yvolabs.airbnbclone.listing.application.dto.sub.PictureDTO;
import com.yvolabs.airbnbclone.listing.domain.Listing;
import com.yvolabs.airbnbclone.listing.mapper.ListingMapper;
import com.yvolabs.airbnbclone.listing.repository.ListingRepository;
import com.yvolabs.airbnbclone.sharedkernel.service.State;
import com.yvolabs.airbnbclone.user.application.Auth0Service;
import com.yvolabs.airbnbclone.user.application.UserService;
import com.yvolabs.airbnbclone.user.application.dto.ReadUserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
    private final ListingMapper listingMapper;

    public CreatedListingDTO create(SaveListingDTO saveListingDTO) {
        Listing newListing = listingMapper.saveListingDTOToListing(saveListingDTO);

        ReadUserDTO userConnected = userService.getAuthenticatedUserFromSecurityContext();
        newListing.setLandlordPublicId(userConnected.publicId());

        Listing savedListing = listingRepository.saveAndFlush(newListing);

        List<PictureDTO> pictureDTOS = pictureService.saveAll(saveListingDTO.getPictures(), savedListing);
        log.info("pictureService.saveAll() success: pictureDTOS. size: {}", pictureDTOS.size());

        auth0Service.addLandlordRoleToUser(userConnected);

        return listingMapper.listingToCreatedListingDTO(savedListing);
    }

    @Transactional(readOnly = true)
    public List<DisplayCardListingDTO> getAllProperties(ReadUserDTO landlord) {
        List<Listing> properties = listingRepository.findAllByLandlordPublicIdFetchCoverPicture(landlord.publicId());
        return listingMapper.listingToDisplayCardListingDTOs(properties);
    }

    @Transactional
    public State<UUID, String> delete(UUID publicId, ReadUserDTO landlord) {
        long deletedSuccessfully = listingRepository.deleteByPublicIdAndLandlordPublicId(publicId, landlord.publicId());
        if (deletedSuccessfully > 0) {
            return State.<UUID, String>builder().forSuccess(publicId);
        } else {
            return State.<UUID, String>builder().forUnauthorized("User not authorized to delete this listing");
        }
    }


    public Optional<ListingCreateBookingDTO> getByListingPublicId(UUID publicId) {
        return listingRepository.findByPublicId(publicId).map(listingMapper::mapListingToListingCreateBookingDTO);
    }

    public List<DisplayCardListingDTO> getCardDisplayByListingPublicId(List<UUID> allListingPublicIDs) {
        return listingRepository.findAllByPublicIdIn(allListingPublicIDs)
                .stream()
                .map(listingMapper::listingToDisplayCardListingDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<DisplayCardListingDTO> getByPublicIdAndLandlordPublicId(UUID listingPublicId, UUID landlordPublicId) {
        return listingRepository.findOneByPublicIdAndLandlordPublicId(listingPublicId, landlordPublicId)
                .map(listingMapper::listingToDisplayCardListingDTO);
    }
}
