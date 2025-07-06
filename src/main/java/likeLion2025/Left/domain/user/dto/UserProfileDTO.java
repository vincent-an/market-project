package likeLion2025.Left.domain.user.dto;

import likeLion2025.Left.domain.user.entity.enums.Department;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDTO {
    private String email;
    private String nickname;
    private Department department;
}
