package likeLion2025.Left.domain.category.entity.enums;

public enum CategoryName {
    MAJOR("전공"),
    GENERAL("교양"),
    MISC("잡화");

    private final String value;

    CategoryName(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
}
