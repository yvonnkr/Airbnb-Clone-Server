package com.yvolabs.airbnbclone.user.application.dto;

import java.util.Set;
import java.util.UUID;

/**
 * @author Yvonne N
 * @version 1.0
 * @since 18/09/2024
 */

public record ReadUserDTO(UUID publicId,
                          String firstName,
                          String lastName,
                          String email,
                          String imageUrl,
                          Set<String> authorities) {
}