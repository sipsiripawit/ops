package com.yipintsoi.authservice.domain.mapper;

import com.yipintsoi.authservice.domain.dto.UserDTO;
import com.yipintsoi.authservice.domain.entity.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-04-17T22:15:06+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDTO userToUserDTO(User user) {
        if ( user == null ) {
            return null;
        }

        UserDTO.UserDTOBuilder userDTO = UserDTO.builder();

        userDTO.id( user.getId() );
        userDTO.username( user.getUsername() );
        userDTO.email( user.getEmail() );
        userDTO.status( user.getStatus() );

        userDTO.roles( user.getRoles().stream().map(role -> role.getName()).collect(java.util.stream.Collectors.toSet()) );

        return userDTO.build();
    }
}
