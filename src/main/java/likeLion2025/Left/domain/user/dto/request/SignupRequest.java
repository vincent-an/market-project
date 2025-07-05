package likeLion2025.Left.domain.user.dto.request;

import likeLion2025.Left.domain.user.entity.enums.Department;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignupRequest {
    private String email;
    private String password;
    private String nickname;
    private Department department;
}
