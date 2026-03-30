import java.time.LocalDateTime;
import java.util.*;

public class ParkingService {

    private final Map<Integer, Map<Integer, ParkingSlot>> levelSlots;
    private final Map<Integer, EntryGate> entryGates;
    private final SlotAssignmentStrategy slotAssignmentStrategy;
    private final BillingStrategy billingStrategy;
    private final PricingStrategy pricingStrategy;
    private final StatusDisplayService statusDisplayService;

    public ParkingService(
            Map<Integer, Map<Integer, ParkingSlot>> levelSlots,
            Map<Integer, EntryGate> entryGates,
            SlotAssignmentStrategy slotAssignmentStrategy,
            BillingStrategy billingStrategy,
            PricingStrategy pricingStrategy,
            StatusDisplayService statusDisplayService) {

        this.levelSlots = levelSlots;
        this.entryGates = entryGates;
        this.slotAssignmentStrategy = slotAssignmentStrategy;
        this.billingStrategy = billingStrategy;
        this.pricingStrategy = pricingStrategy;
        this.statusDisplayService = statusDisplayService;
    }

    public Ticket park(Vehicle vehicle, LocalDateTime entryTime, int entryGateId) {
        EntryGate gate = entryGates.get(entryGateId);
        int level = gate.getFloorNumber();
        SlotType requiredSlotType = vehicle.getVehicleType().getRequiredSlotType();

        SlotType currentType = requiredSlotType;
        while (currentType != null) {
            ParkingSlot slot = slotAssignmentStrategy.findSlot(currentType, entryGateId, level);
            if (slot != null) {
                slot.parkVehicle(vehicle);
                Ticket ticket = new Ticket(slot.getSlotId(), currentType, vehicle, entryTime);

                if (!currentType.equals(requiredSlotType)) {
                    System.out.println("  Upgraded " + vehicle.getRegistrationNumber() + " from " + requiredSlotType
                            + " to " + currentType + " (requested slot type was full)");
                }
                return ticket;
            }
            currentType = currentType.nextBigger();
        }

        throw new RuntimeException(
                "No available slot for " + vehicle.getRegistrationNumber() + " on floor " + level
                        + " (tried " + requiredSlotType + " and above)");
    }

    public double exit(Ticket ticket, LocalDateTime exitTime) {
        ticket.setExitTime(exitTime);

        int slotId = ticket.getParkingSlotId();
        ParkingSlot slot = findSlotById(slotId);
        slot.freeSlot();

        return billingStrategy.bill(ticket, pricingStrategy);
    }

    public void status() {
        statusDisplayService.displayStatus();
    }

    private ParkingSlot findSlotById(int slotId) {
        for (Map<Integer, ParkingSlot> slotMap : levelSlots.values()) {
            if (slotMap.containsKey(slotId)) {
                return slotMap.get(slotId);
            }
        }
        throw new IllegalArgumentException("Slot#" + slotId + " not found.");
    }
}
