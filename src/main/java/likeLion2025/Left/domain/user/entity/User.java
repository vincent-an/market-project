package likeLion2025.Left.domain.user.entity;

import jakarta.persistence.*;
import likeLion2025.Left.domain.user.entity.enums.Department;
import likeLion2025.Left.domain.user.entity.enums.Role;
import lombok.*;

@Entity
@Builder
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String nickname;

    @Enumerated(EnumType.STRING) @Column(nullable = false)
    private Department department;

    @Enumerated(EnumType.STRING)
    private Role role;


}


