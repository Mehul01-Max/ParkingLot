public interface SlotAssignmentStrategy {

    ParkingSlot findSlot(SlotType slotType, int entryGateId, int level);
}
