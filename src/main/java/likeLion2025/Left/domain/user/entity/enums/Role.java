package likeLion2025.Left.domain.user.entity.enums;

public enum Role {
    ADMIN("운영자"),
    USER("사용자");

    private final String value;

    Role(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

