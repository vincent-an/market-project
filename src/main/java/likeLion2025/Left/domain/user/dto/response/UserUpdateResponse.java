package likeLion2025.Left.domain.user.dto.response;

import likeLion2025.Left.domain.user.entity.User;
import likeLion2025.Left.domain.user.entity.enums.Department;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserUpdateResponse {
    private String nickname;
    private Department department;
    private String email;
    private String message;

    public static UserUpdateResponse from(User user, String message) {
        return UserUpdateResponse.builder()
                .nickname(user.getNickname())
                .department(user.getDepartment())
                .email(user.getEmail())
                .message(message)
                .build();
    }
}
