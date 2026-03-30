import java.util.concurrent.atomic.AtomicInteger;

public class EntryGate {

    private static final AtomicInteger ID_GENERATOR = new AtomicInteger(1);

    private final int entryGateId;
    private final int floorNumber;

    public EntryGate(int floorNumber) {
        this.entryGateId = ID_GENERATOR.getAndIncrement();
        this.floorNumber = floorNumber;
    }

    public int getEntryGateId() {
        return entryGateId;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    @Override
    public int hashCode() {
        return entryGateId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        EntryGate other = (EntryGate) obj;
        return this.entryGateId == other.entryGateId;
    }

    @Override
    public String toString() {
        return "EntryGate#" + entryGateId + " (Floor " + floorNumber + ")";
    }
}
