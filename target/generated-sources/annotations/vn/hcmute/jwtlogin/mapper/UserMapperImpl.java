package vn.hcmute.jwtlogin.mapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import vn.hcmute.jwtlogin.dto.RegisterRequest;
import vn.hcmute.jwtlogin.dto.UserResponse;
import vn.hcmute.jwtlogin.entity.Role;
import vn.hcmute.jwtlogin.entity.User;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-09-29T09:46:26+0700",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.46.0.v20260528-0407, environment: Java 25.0.3 (Eclipse Adoptium)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserResponse toResponse(User user) {
        if ( user == null ) {
            return null;
        }

        Long id = null;
        String fullName = null;
        String email = null;
        String images = null;
        Role role = null;
        LocalDateTime createdAt = null;

        id = user.getId();
        fullName = user.getFullName();
        email = user.getEmail();
        images = user.getImages();
        role = user.getRole();
        createdAt = user.getCreatedAt();

        UserResponse userResponse = new UserResponse( id, fullName, email, images, role, createdAt );

        return userResponse;
    }

    @Override
    public List<UserResponse> toResponseList(List<User> users) {
        if ( users == null ) {
            return null;
        }

        List<UserResponse> list = new ArrayList<UserResponse>( users.size() );
        for ( User user : users ) {
            list.add( toResponse( user ) );
        }

        return list;
    }

    @Override
    public User toEntity(RegisterRequest request) {
        if ( request == null ) {
            return null;
        }

        User user = new User();

        user.setEmail( request.email() );
        user.setFullName( request.fullName() );

        return user;
    }
}
