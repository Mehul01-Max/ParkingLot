public enum SlotType {
    SMALL(1),
    MEDIUM(2),
    LARGE(3);

    private final int size;

    private SlotType(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public SlotType nextBigger() {
        switch (this) {
            case SMALL:
                return MEDIUM;
            case MEDIUM:
                return LARGE;
            default:
                return null;
        }
    }
}
