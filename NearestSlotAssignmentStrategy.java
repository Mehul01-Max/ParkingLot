import java.util.*;

public class NearestSlotAssignmentStrategy implements SlotAssignmentStrategy {

    private final Map<Integer, Map<Integer, ParkingSlot>> levelSlots;
    private final Map<Integer, Map<SlotType, Map<Integer, TreeSet<EntryGateSlotDistance>>>> gateSlotMapping;

    public NearestSlotAssignmentStrategy(
            Map<Integer, Map<Integer, ParkingSlot>> levelSlots,
            Map<Integer, EntryGate> entryGates,
            List<EntryGateSlotDistance> gateSlotDistances) {

        this.levelSlots = levelSlots;
        this.gateSlotMapping = new HashMap<>();

        for (EntryGateSlotDistance egsd : gateSlotDistances) {
            int gateId = egsd.getEntryGateId();
            int slotId = egsd.getSlotId();
            EntryGate gate = entryGates.get(gateId);
            int level = gate.getFloorNumber();
            SlotType slotType = levelSlots.get(level).get(slotId).getSlotType();

            gateSlotMapping
                    .computeIfAbsent(level, k -> new HashMap<>())
                    .computeIfAbsent(slotType, k -> new HashMap<>())
                    .computeIfAbsent(gateId, k -> new TreeSet<>(
                            (a, b) -> {
                                int cmp = Double.compare(a.getDistance(), b.getDistance());
                                if (cmp != 0)
                                    return cmp;
                                return Integer.compare(a.getSlotId(), b.getSlotId());
                            }))
                    .add(egsd);
        }
    }

    @Override
    public ParkingSlot findSlot(SlotType slotType, int entryGateId, int level) {
        Map<SlotType, Map<Integer, TreeSet<EntryGateSlotDistance>>> slotTypeMap = gateSlotMapping.get(level);
        if (slotTypeMap == null)
            return null;

        Map<Integer, TreeSet<EntryGateSlotDistance>> gateMap = slotTypeMap.get(slotType);
        if (gateMap == null)
            return null;

        TreeSet<EntryGateSlotDistance> distances = gateMap.get(entryGateId);
        if (distances == null || distances.isEmpty())
            return null;

        for (EntryGateSlotDistance egsd : distances) {
            ParkingSlot slot = levelSlots.get(level).get(egsd.getSlotId());
            if (!slot.isOccupied()) {
                return slot;
            }
        }
        return null;
    }
}
