package likeLion2025.Left.domain.post.entity.enums;

public enum Category {
    MAJOR("전공"),
    GENERAL("교양"),
    MISC("잡화");

    private final String value;

    Category(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
}
