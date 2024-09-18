package com.yvolabs.airbnbclone.listing.repository;

import com.yvolabs.airbnbclone.listing.domain.ListingPicture;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Yvonne N
 * @version 1.0
 * @since 18/09/2024
 */
public interface ListingPictureRepository extends JpaRepository<ListingPicture, Long> {
}
