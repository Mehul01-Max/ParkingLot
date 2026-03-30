public class EntryGateSlotDistance {

    private final int entryGateId;
    private final int slotId;
    private final double distance;

    public EntryGateSlotDistance(int entryGateId, int slotId, double distance) {
        this.entryGateId = entryGateId;
        this.slotId = slotId;
        this.distance = distance;
    }

    public int getEntryGateId() {
        return entryGateId;
    }

    public int getSlotId() {
        return slotId;
    }

    public double getDistance() {
        return distance;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        EntryGateSlotDistance o = (EntryGateSlotDistance) obj;
        return this.entryGateId == o.entryGateId && this.slotId == o.slotId;
    }

    @Override
    public int hashCode() {
        int result = Integer.hashCode(entryGateId);
        result = 31 * result + Integer.hashCode(slotId);
        return result;
    }

}
