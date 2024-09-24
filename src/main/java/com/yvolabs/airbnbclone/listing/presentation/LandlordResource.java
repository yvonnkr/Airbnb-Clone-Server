package com.yvolabs.airbnbclone.listing.presentation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yvolabs.airbnbclone.infrastructure.config.SecurityUtils;
import com.yvolabs.airbnbclone.listing.application.LandlordService;
import com.yvolabs.airbnbclone.listing.application.dto.CreatedListingDTO;
import com.yvolabs.airbnbclone.listing.application.dto.DisplayCardListingDTO;
import com.yvolabs.airbnbclone.listing.application.dto.SaveListingDTO;
import com.yvolabs.airbnbclone.listing.application.dto.sub.PictureDTO;
import com.yvolabs.airbnbclone.sharedkernel.service.State;
import com.yvolabs.airbnbclone.sharedkernel.service.StatusNotification;
import com.yvolabs.airbnbclone.user.application.UserException;
import com.yvolabs.airbnbclone.user.application.UserService;
import com.yvolabs.airbnbclone.user.application.dto.ReadUserDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Yvonne N
 * @version 1.0
 * @since 21/09/2024
 */

@RestController
@RequestMapping("/api/landlord-listing")
@RequiredArgsConstructor
public class LandlordResource {

    private final LandlordService landlordService;
    private final Validator validator;
    private final UserService userService;
    private ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CreatedListingDTO> create(
            MultipartHttpServletRequest request,
            @RequestPart(name = "dto") String saveListingDTOString
    ) throws IOException {
        List<PictureDTO> pictures = request.getFileMap()
                .values()
                .stream()
                .map(mapMultipartFileToPictureDTO())
                .toList();

        SaveListingDTO saveListingDTO = objectMapper.readValue(saveListingDTOString, SaveListingDTO.class);
        saveListingDTO.setPictures(pictures);

        Set<ConstraintViolation<SaveListingDTO>> violations = validator.validate(saveListingDTO);
        if (!violations.isEmpty()) {
            String violationsJoined = violations.stream()
                    .map(violation -> violation.getPropertyPath() + " " + violation.getMessage())
                    .collect(Collectors.joining());

            ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, violationsJoined);
            return ResponseEntity.of(problemDetail).build();
        } else {
            return ResponseEntity.ok(landlordService.create(saveListingDTO));
        }
    }

    @GetMapping(value = "/get-all")
    @PreAuthorize("hasAnyRole('" + SecurityUtils.ROLE_LANDLORD + "')")
    public ResponseEntity<List<DisplayCardListingDTO>> getAll() {
        ReadUserDTO connectedUser = userService.getAuthenticatedUserFromSecurityContext();
        List<DisplayCardListingDTO> allProperties = landlordService.getAllProperties(connectedUser);
        return ResponseEntity.ok(allProperties);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyRole('" + SecurityUtils.ROLE_LANDLORD + "')")
    public ResponseEntity<UUID> delete(@RequestParam UUID publicId) {
        ReadUserDTO connectedUser = userService.getAuthenticatedUserFromSecurityContext();
        State<UUID, String> deleteState = landlordService.delete(publicId, connectedUser);
        if (deleteState.getStatus().equals(StatusNotification.OK)) {
            return ResponseEntity.ok(deleteState.getValue());
        } else if (deleteState.getStatus().equals(StatusNotification.UNAUTHORIZED)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    private static Function<MultipartFile, PictureDTO> mapMultipartFileToPictureDTO() {
        return multipartFile -> {
            try {
                return new PictureDTO(multipartFile.getBytes(), multipartFile.getContentType(), false);
            } catch (IOException ioe) {
                throw new UserException(String.format("Cannot parse multipart file: %s", multipartFile.getOriginalFilename()));
            }
        };
    }
}
