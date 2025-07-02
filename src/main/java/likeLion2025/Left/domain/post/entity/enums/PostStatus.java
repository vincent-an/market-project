package likeLion2025.Left.domain.post.entity.enums;

public enum PostStatus {
    SELLING("판매중"),
    SOLD("판매완료"),
    BUYING("구매중"),
    BOUGHT("구매완료");

    private final String value;

    PostStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
