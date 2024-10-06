package com.yvolabs.airbnbclone.listing.application;

import com.yvolabs.airbnbclone.booking.application.BookingService;
import com.yvolabs.airbnbclone.listing.application.dto.DisplayCardListingDTO;
import com.yvolabs.airbnbclone.listing.application.dto.DisplayListingDTO;
import com.yvolabs.airbnbclone.listing.application.dto.SearchDTO;
import com.yvolabs.airbnbclone.listing.application.dto.sub.LandlordListingDTO;
import com.yvolabs.airbnbclone.listing.domain.BookingCategory;
import com.yvolabs.airbnbclone.listing.domain.Listing;
import com.yvolabs.airbnbclone.listing.mapper.ListingMapper;
import com.yvolabs.airbnbclone.listing.repository.ListingRepository;
import com.yvolabs.airbnbclone.sharedkernel.service.State;
import com.yvolabs.airbnbclone.user.application.UserService;
import com.yvolabs.airbnbclone.user.application.dto.ReadUserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Yvonne N
 * @version 1.0
 * @since 24/09/2024
 */

@Service
@RequiredArgsConstructor
public class TenantService {

    private final ListingRepository listingRepository;
    private final ListingMapper listingMapper;
    private final UserService userService;
    private final BookingService bookingService;

    @Transactional(readOnly = true)
    public Page<DisplayCardListingDTO> getAllByCategory(Pageable pageable, BookingCategory category) {
        Page<Listing> allOrBookingCategory;
        if (category == BookingCategory.ALL) {
            allOrBookingCategory = listingRepository.findAllWithCoverOnly(pageable);
        } else {
            allOrBookingCategory = listingRepository.findAllByBookingCategoryWithCoverOnly(pageable, category);
        }

        return allOrBookingCategory.map(listingMapper::listingToDisplayCardListingDTO);
    }

    @Transactional(readOnly = true)
    public State<DisplayListingDTO, String> getOne(UUID publicId) {
        Optional<Listing> listingByPublicIdOpt = listingRepository.findByPublicId(publicId);

        if (listingByPublicIdOpt.isEmpty()) {
            return State.<DisplayListingDTO, String>builder()
                    .forError(String.format("Listing doesn't exist for publicId: %s", publicId));
        }

        DisplayListingDTO displayListingDTO = listingMapper.listingToDisplayListingDTO(listingByPublicIdOpt.get());

        ReadUserDTO readUserDTO = userService.getByPublicId(listingByPublicIdOpt.get().getLandlordPublicId()).orElseThrow();
        LandlordListingDTO landlordListingDTO = new LandlordListingDTO(readUserDTO.firstName(), readUserDTO.imageUrl());
        displayListingDTO.setLandlord(landlordListingDTO);

        return State.<DisplayListingDTO, String>builder().forSuccess(displayListingDTO);
    }

    @Transactional(readOnly = true)
    public Page<DisplayCardListingDTO> search(Pageable pageable, SearchDTO newSearch) {

        Page<Listing> allMatchedListings = listingRepository.findAllByLocationAndBathroomsAndBedroomsAndGuestsAndBeds(pageable, newSearch.location(),
                newSearch.infos().baths().value(),
                newSearch.infos().bedrooms().value(),
                newSearch.infos().guests().value(),
                newSearch.infos().beds().value());

        List<UUID> listingUUIDs = allMatchedListings.stream().map(Listing::getPublicId).toList();

        List<UUID> bookingUUIDs = bookingService.getBookingMatchByListingIdsAndBookedDate(listingUUIDs, newSearch.dates());

        List<DisplayCardListingDTO> listingsNotBooked = allMatchedListings.stream()
                .filter(listing -> !bookingUUIDs.contains(listing.getPublicId()))
                .map(listingMapper::listingToDisplayCardListingDTO)
                .toList();

        return new PageImpl<>(listingsNotBooked, pageable, listingsNotBooked.size());
    }

}
