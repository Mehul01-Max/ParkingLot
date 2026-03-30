public enum VehicleType {
    TWO_WHEELER(SlotType.SMALL),
    CAR(SlotType.MEDIUM),
    BUS(SlotType.LARGE);

    private final SlotType requiredSlotType;

    private VehicleType(SlotType requiredSlotType) {
        this.requiredSlotType = requiredSlotType;
    }

    public SlotType getRequiredSlotType() {
        return requiredSlotType;
    }
}
