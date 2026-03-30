import java.time.LocalDateTime;
import java.util.*;

public class Main {

        public static void main(String[] args) {

                PricingStrategy pricing = new PricingStrategy()
                                .addRate(SlotType.SMALL, 50.0)
                                .addRate(SlotType.MEDIUM, 100.0)
                                .addRate(SlotType.LARGE, 200.0);

                BillingStrategy billing = new NormalBillingStrategy();

                ParkingSlot s1 = new ParkingSlot(SlotType.SMALL, 1);
                ParkingSlot s2 = new ParkingSlot(SlotType.SMALL, 1);
                ParkingSlot s3 = new ParkingSlot(SlotType.MEDIUM, 1);
                ParkingSlot s4 = new ParkingSlot(SlotType.MEDIUM, 1);
                ParkingSlot s5 = new ParkingSlot(SlotType.LARGE, 1);
                ParkingSlot s6 = new ParkingSlot(SlotType.SMALL, 2);
                ParkingSlot s7 = new ParkingSlot(SlotType.MEDIUM, 2);
                ParkingSlot s8 = new ParkingSlot(SlotType.LARGE, 2);

                Map<Integer, Map<Integer, ParkingSlot>> levelSlots = new HashMap<>();

                Map<Integer, ParkingSlot> floor1 = new HashMap<>();
                for (ParkingSlot s : Arrays.asList(s1, s2, s3, s4, s5)) {
                        floor1.put(s.getSlotId(), s);
                }
                levelSlots.put(1, floor1);

                Map<Integer, ParkingSlot> floor2 = new HashMap<>();
                for (ParkingSlot s : Arrays.asList(s6, s7, s8)) {
                        floor2.put(s.getSlotId(), s);
                }
                levelSlots.put(2, floor2);

                EntryGate gate1 = new EntryGate(1);
                EntryGate gate2 = new EntryGate(2);

                Map<Integer, EntryGate> entryGates = new HashMap<>();
                entryGates.put(gate1.getEntryGateId(), gate1);
                entryGates.put(gate2.getEntryGateId(), gate2);

                List<EntryGateSlotDistance> distances = Arrays.asList(

                                new EntryGateSlotDistance(gate1.getEntryGateId(), s1.getSlotId(), 10.0),
                                new EntryGateSlotDistance(gate1.getEntryGateId(), s2.getSlotId(), 20.0),
                                new EntryGateSlotDistance(gate1.getEntryGateId(), s3.getSlotId(), 15.0),
                                new EntryGateSlotDistance(gate1.getEntryGateId(), s4.getSlotId(), 25.0),
                                new EntryGateSlotDistance(gate1.getEntryGateId(), s5.getSlotId(), 30.0),
                                new EntryGateSlotDistance(gate2.getEntryGateId(), s6.getSlotId(), 5.0),
                                new EntryGateSlotDistance(gate2.getEntryGateId(), s7.getSlotId(), 12.0),
                                new EntryGateSlotDistance(gate2.getEntryGateId(), s8.getSlotId(), 18.0));

                StatusDisplayService statusDisplay = new StatusDisplayService(2, levelSlots);

                SlotAssignmentStrategy nearestStrategy = new NearestSlotAssignmentStrategy(
                                levelSlots, entryGates, distances);

                ParkingService service = new ParkingService(
                                levelSlots, entryGates, nearestStrategy, billing, pricing, statusDisplay);

                service.status();

                System.out.println("[ACTION] Parking car KA-01-AB-1234 via Gate 1...");
                Vehicle car1 = new Vehicle("KA-01-AB-1234", VehicleType.CAR);
                Ticket t1 = service.park(car1, LocalDateTime.of(2026, 3, 30, 10, 0), gate1.getEntryGateId());
                System.out.println("  → Ticket#" + t1.getTicketId() + " | Slot#" + t1.getParkingSlotId()
                                + " | Type=" + t1.getAssignedSlotType());

                System.out.println("\n[ACTION] Parking bike KA-02-CD-5678 via Gate 1...");
                Vehicle bike1 = new Vehicle("KA-02-CD-5678", VehicleType.TWO_WHEELER);
                Ticket t2 = service.park(bike1, LocalDateTime.of(2026, 3, 30, 10, 30), gate1.getEntryGateId());
                System.out.println("  → Ticket#" + t2.getTicketId() + " | Slot#" + t2.getParkingSlotId()
                                + " | Type=" + t2.getAssignedSlotType());

                System.out.println("\n[ACTION] Parking bike KA-03-EF-9012 via Gate 1...");
                Vehicle bike2 = new Vehicle("KA-03-EF-9012", VehicleType.TWO_WHEELER);
                Ticket t3 = service.park(bike2, LocalDateTime.of(2026, 3, 30, 10, 45), gate1.getEntryGateId());
                System.out.println("  → Ticket#" + t3.getTicketId() + " | Slot#" + t3.getParkingSlotId()
                                + " | Type=" + t3.getAssignedSlotType());

                System.out.println("\n[ACTION] Parking bike KA-04-GH-3456 via Gate 1 (expect upgrade)...");
                Vehicle bike3 = new Vehicle("KA-04-GH-3456", VehicleType.TWO_WHEELER);
                Ticket t4 = service.park(bike3, LocalDateTime.of(2026, 3, 30, 11, 0), gate1.getEntryGateId());
                System.out.println("  → Ticket#" + t4.getTicketId() + " | Slot#" + t4.getParkingSlotId()
                                + " | Type=" + t4.getAssignedSlotType());

                System.out.println("\n[ACTION] Parking bus KA-05-IJ-7890 via Gate 2...");
                Vehicle bus1 = new Vehicle("KA-05-IJ-7890", VehicleType.BUS);
                Ticket t5 = service.park(bus1, LocalDateTime.of(2026, 3, 30, 11, 15), gate2.getEntryGateId());
                System.out.println("  → Ticket#" + t5.getTicketId() + " | Slot#" + t5.getParkingSlotId()
                                + " | Type=" + t5.getAssignedSlotType());

                service.status();

                System.out.println("[ACTION] Car KA-01-AB-1234 exiting...");
                double bill1 = service.exit(t1, LocalDateTime.of(2026, 3, 30, 12, 30));
                System.out.println("  → Bill: ₹" + bill1 + " (MEDIUM slot, 3 hours)");

                System.out.println("\n[ACTION] Bike KA-02-CD-5678 exiting...");
                double bill2 = service.exit(t2, LocalDateTime.of(2026, 3, 30, 11, 15));
                System.out.println("  → Bill: ₹" + bill2 + " (SMALL slot, 1 hour)");

                System.out.println("\n[ACTION] Bus KA-05-IJ-7890 exiting...");
                double bill3 = service.exit(t5, LocalDateTime.of(2026, 3, 30, 14, 0));
                System.out.println("  → Bill: ₹" + bill3 + " (LARGE slot, 3 hours)");

                service.status();

        }
}
