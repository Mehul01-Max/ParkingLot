import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

public class Ticket {

    private static final AtomicInteger ID_GENERATOR = new AtomicInteger(1);

    private final int ticketId;
    private final int parkingSlotId;
    private final SlotType assignedSlotType;
    private final Vehicle vehicle;
    private final LocalDateTime entryTime;
    private LocalDateTime exitTime;

    public Ticket(int parkingSlotId, SlotType assignedSlotType, Vehicle vehicle, LocalDateTime entryTime) {
        this.ticketId = ID_GENERATOR.getAndIncrement();
        this.parkingSlotId = parkingSlotId;
        this.assignedSlotType = assignedSlotType;
        this.vehicle = vehicle;
        this.entryTime = entryTime;
        this.exitTime = null;
    }

    public int getTicketId() {
        return ticketId;
    }

    public int getParkingSlotId() {
        return parkingSlotId;
    }

    public SlotType getAssignedSlotType() {
        return assignedSlotType;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public LocalDateTime getExitTime() {
        return exitTime;
    }

    public void setExitTime(LocalDateTime exitTime) {
        this.exitTime = exitTime;
    }
}
