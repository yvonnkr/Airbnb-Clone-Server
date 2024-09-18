package com.yvolabs.airbnbclone.listing.repository;

import com.yvolabs.airbnbclone.listing.domain.Listing;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Yvonne N
 * @version 1.0
 * @since 18/09/2024
 */
public interface ListingRepository extends JpaRepository<Listing, Long> {
}
