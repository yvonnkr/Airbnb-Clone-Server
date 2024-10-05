package com.yvolabs.airbnbclone.booking.application;

import com.yvolabs.airbnbclone.booking.application.dto.BookedDateDTO;
import com.yvolabs.airbnbclone.booking.application.dto.BookedListingDTO;
import com.yvolabs.airbnbclone.booking.application.dto.NewBookingDTO;
import com.yvolabs.airbnbclone.booking.domain.Booking;
import com.yvolabs.airbnbclone.booking.mapper.BookingMapper;
import com.yvolabs.airbnbclone.booking.repository.BookingRepository;
import com.yvolabs.airbnbclone.infrastructure.config.SecurityUtils;
import com.yvolabs.airbnbclone.listing.application.LandlordService;
import com.yvolabs.airbnbclone.listing.application.dto.DisplayCardListingDTO;
import com.yvolabs.airbnbclone.listing.application.dto.ListingCreateBookingDTO;
import com.yvolabs.airbnbclone.listing.application.dto.vo.PriceVO;
import com.yvolabs.airbnbclone.sharedkernel.service.State;
import com.yvolabs.airbnbclone.user.application.UserService;
import com.yvolabs.airbnbclone.user.application.dto.ReadUserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Yvonne N
 * @version 1.0
 * @since 26/09/2024
 */

@Service
@RequiredArgsConstructor
public class BookingService {
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final UserService userService;
    private final LandlordService landlordService;

    @Transactional
    public State<Void, String> create(NewBookingDTO newBookingDTO) {
        Booking booking = bookingMapper.newBookingToBooking(newBookingDTO);

        Optional<ListingCreateBookingDTO> listingOpt = landlordService.getByListingPublicId(newBookingDTO.listingPublicId());

        if (listingOpt.isEmpty()) {
            return State.<Void, String>builder().forError("Landlord public id not found");
        }

        boolean alreadyBooked = bookingRepository.bookingExistsAtInterval(newBookingDTO.startDate(), newBookingDTO.endDate(), newBookingDTO.listingPublicId());

        if (alreadyBooked) {
            return State.<Void, String>builder().forError("One booking already exists");
        }

        ListingCreateBookingDTO listingCreateBookingDTO = listingOpt.get();

        booking.setFkListing(listingCreateBookingDTO.listingPublicId());

        ReadUserDTO connectedUser = userService.getAuthenticatedUserFromSecurityContext();
        booking.setFkTenant(connectedUser.publicId());
        booking.setNumberOfTravelers(1);

        long numberOfNights = ChronoUnit.DAYS.between(booking.getStartDate(), booking.getEndDate());
        booking.setTotalPrice((int) (numberOfNights * listingCreateBookingDTO.price().value()));

        bookingRepository.save(booking);

        return State.<Void, String>builder().forSuccess();
    }

    @Transactional(readOnly = true)
    public List<BookedDateDTO> checkAvailability(UUID publicId) {
        return bookingRepository.findAllByFkListing(publicId)
                .stream().map(bookingMapper::bookingToCheckAvailability).toList();
    }

    @Transactional(readOnly = true)
    public List<BookedListingDTO> getBookedListing() {
        ReadUserDTO connectedUser = userService.getAuthenticatedUserFromSecurityContext();
        List<Booking> allBookings = bookingRepository.findAllByFkTenant(connectedUser.publicId());
        List<UUID> allListingPublicIDs = allBookings.stream().map(Booking::getFkListing).toList();
        List<DisplayCardListingDTO> allListings = landlordService.getCardDisplayByListingPublicId(allListingPublicIDs);
        return mapBookingToBookedListing(allBookings, allListings);
    }

    @Transactional
    public State<UUID, String> cancel(UUID bookingPublicId, UUID listingPublicId, boolean byLandlord) {
        ReadUserDTO connectedUser = userService.getAuthenticatedUserFromSecurityContext();
        int deleteSuccess = 0;

        if (SecurityUtils.hasCurrentUserAnyOfAuthorities(SecurityUtils.ROLE_LANDLORD)
                && byLandlord) {
            deleteSuccess = handleDeletionForLandlord(bookingPublicId, listingPublicId, connectedUser, deleteSuccess);
        } else {
            deleteSuccess = bookingRepository.deleteBookingByFkTenantAndPublicId(connectedUser.publicId(), bookingPublicId);
        }

        if (deleteSuccess >= 1) {
            return State.<UUID, String>builder().forSuccess(bookingPublicId);
        } else {
            return State.<UUID, String>builder().forError("Booking not found");
        }
    }

    @Transactional(readOnly = true)
    public List<BookedListingDTO> getBookedListingForLandlord() {
        ReadUserDTO connectedUser = userService.getAuthenticatedUserFromSecurityContext();
        List<DisplayCardListingDTO> allProperties = landlordService.getAllProperties(connectedUser);
        List<UUID> allPropertyPublicIds = allProperties.stream().map(DisplayCardListingDTO::publicId).toList();
        List<Booking> allBookings = bookingRepository.findAllByFkListingIn(allPropertyPublicIds);
        return mapBookingToBookedListing(allBookings, allProperties);
    }

    private List<BookedListingDTO> mapBookingToBookedListing(List<Booking> allBookings, List<DisplayCardListingDTO> allListings) {
        return allBookings.stream().map(booking -> {
            DisplayCardListingDTO displayCardListingDTO = allListings
                    .stream()
                    .filter(listing -> listing.publicId().equals(booking.getFkListing()))
                    .findFirst()
                    .orElseThrow();
            BookedDateDTO dates = bookingMapper.bookingToCheckAvailability(booking);
            return new BookedListingDTO(displayCardListingDTO.cover(),
                    displayCardListingDTO.location(),
                    dates, new PriceVO(booking.getTotalPrice()),
                    booking.getPublicId(), displayCardListingDTO.publicId());
        }).toList();
    }

    private int handleDeletionForLandlord(UUID bookingPublicId, UUID listingPublicId, ReadUserDTO connectedUser, int deleteSuccess) {
        Optional<DisplayCardListingDTO> listingVerificationOpt = landlordService.getByPublicIdAndLandlordPublicId(listingPublicId, connectedUser.publicId());
        if (listingVerificationOpt.isPresent()) {
            deleteSuccess = bookingRepository.deleteBookingByPublicIdAndFkListing(bookingPublicId, listingVerificationOpt.get().publicId());
        }
        return deleteSuccess;
    }
}
