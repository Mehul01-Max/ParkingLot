import java.util.concurrent.atomic.AtomicInteger;

public class ParkingSlot {

    private static final AtomicInteger ID_GENERATOR = new AtomicInteger(1);

    private final int slotId;
    private final SlotType slotType;
    private final int floorNumber;
    private boolean occupied;
    private Vehicle parkedVehicle;

    public ParkingSlot(SlotType slotType, int floorNumber) {
        this.slotId = ID_GENERATOR.getAndIncrement();
        this.slotType = slotType;
        this.floorNumber = floorNumber;
        this.occupied = false;
        this.parkedVehicle = null;
    }

    public int getSlotId() {
        return slotId;
    }

    public SlotType getSlotType() {
        return slotType;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public Vehicle getParkedVehicle() {
        return parkedVehicle;
    }

    public void parkVehicle(Vehicle vehicle) {
        this.occupied = true;
        this.parkedVehicle = vehicle;
    }

    public void freeSlot() {
        this.occupied = false;
        this.parkedVehicle = null;
    }

}
