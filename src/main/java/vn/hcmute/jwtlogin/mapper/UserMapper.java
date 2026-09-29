package vn.hcmute.jwtlogin.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import vn.hcmute.jwtlogin.dto.RegisterRequest;
import vn.hcmute.jwtlogin.dto.UserResponse;
import vn.hcmute.jwtlogin.entity.User;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserResponse toResponse(User user);

    List<UserResponse> toResponseList(List<User> users);

    /** Password được mã hóa ở service, không copy trực tiếp từ request. */
    @Mapping(target = "password", ignore = true)
    User toEntity(RegisterRequest request);
}
