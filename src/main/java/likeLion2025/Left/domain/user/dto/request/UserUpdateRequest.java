package likeLion2025.Left.domain.user.dto.request;

import likeLion2025.Left.domain.user.entity.enums.Department;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class UserUpdateRequest {
    private String nickname;
    private Department department;
}
