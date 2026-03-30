import java.util.Map;

public class StatusDisplayService {

    private final int levels;
    private final Map<Integer, Map<Integer, ParkingSlot>> levelSlots;

    public StatusDisplayService(int levels, Map<Integer, Map<Integer, ParkingSlot>> levelSlots) {
        this.levels = levels;
        this.levelSlots = levelSlots;
    }

    public void displayStatus() {
        System.out.println("\n========== PARKING LOT STATUS ==========");
        for (int level = 1; level <= levels; level++) {
            Map<Integer, ParkingSlot> slots = levelSlots.get(level);
            if (slots == null)
                continue;

            int totalSmall = 0, totalMedium = 0, totalLarge = 0;
            int availSmall = 0, availMedium = 0, availLarge = 0;

            for (ParkingSlot slot : slots.values()) {
                switch (slot.getSlotType()) {
                    case SMALL:
                        totalSmall++;
                        if (!slot.isOccupied())
                            availSmall++;
                        break;
                    case MEDIUM:
                        totalMedium++;
                        if (!slot.isOccupied())
                            availMedium++;
                        break;
                    case LARGE:
                        totalLarge++;
                        if (!slot.isOccupied())
                            availLarge++;
                        break;
                }
            }

            System.out.println("\n--- Floor " + level + " ---");
            System.out.println("  SMALL  : " + availSmall + "/" + totalSmall + " available");
            System.out.println("  MEDIUM : " + availMedium + "/" + totalMedium + " available");
            System.out.println("  LARGE  : " + availLarge + "/" + totalLarge + " available");
        }
        System.out.println("\n========================================\n");
    }
}
