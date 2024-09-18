package com.yvolabs.airbnbclone.user.mapper;

import com.yvolabs.airbnbclone.user.application.dto.ReadUserDTO;
import com.yvolabs.airbnbclone.user.domain.Authority;
import com.yvolabs.airbnbclone.user.domain.User;
import org.mapstruct.Mapper;

/**
 * @author Yvonne N
 * @version 1.0
 * @since 17/09/2024
 */

@Mapper(componentModel = "spring")
public interface UserMapper {

    ReadUserDTO readUserDTOToUser(User user);

    default String mapAuthoritiesToString(Authority authority) {
        return authority.getName();
    }
}
