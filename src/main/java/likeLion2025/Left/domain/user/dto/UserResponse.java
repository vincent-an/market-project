package likeLion2025.Left.domain.user.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import likeLion2025.Left.domain.user.entity.User;
import likeLion2025.Left.domain.user.entity.enums.Department;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class) //user_id를 -> userId처럼 변경해주는 코드
public class UserResponse {
    private Long userId;
    private String email;
    private String nickname;
    private Department department;
    private String message;

    public static UserResponse from(User user, String message){
        return UserResponse.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .department(user.getDepartment())
                .message(message)
                .build();
    }
}
