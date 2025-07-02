package likeLion2025.Left.domain.post.entity.enums;

public enum PostType {
    BUY("구매글"), SELL("판매글");

    private final String value;

    PostType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
