package com.yvolabs.airbnbclone.listing.application.dto.sub;


import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.Objects;

/**
 * @author Yvonne N
 * @version 1.0
 * @since 20/09/2024
 */

@Builder
public record PictureDTO(
        @NotNull byte[] file,
        @NotNull String fileContentType,
        @NotNull boolean isCover
) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PictureDTO that = (PictureDTO) o;
        return isCover == that.isCover && Objects.equals(fileContentType, that.fileContentType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fileContentType, isCover);
    }

    @Override
    public String toString() {
        return "PictureDTO{" +
                "fileContentType='" + fileContentType + '\'' +
                ", isCover=" + isCover +
                '}';
    }
}