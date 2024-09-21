package com.yvolabs.airbnbclone.listing.application.dto.sub;

import com.yvolabs.airbnbclone.listing.application.dto.vo.DescriptionVO;
import com.yvolabs.airbnbclone.listing.application.dto.vo.TitleVO;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

/**
 * @author Yvonne N
 * @version 1.0
 * @since 20/09/2024
 */
@Builder
public record DescriptionDTO(
        @NotNull TitleVO title,
        @NotNull DescriptionVO description
) {
}