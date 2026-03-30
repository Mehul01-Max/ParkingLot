# Parking Lot System — Low-Level Design

A multilevel parking lot management system built in Java, designed with **SOLID principles** and the **Strategy pattern**.

---

## Features

- **Multi-level parking** with configurable floors and slots
- **Vehicle types**: Two-Wheeler, Car, Bus
- **Slot types**: Small, Medium, Large — with automatic **upgrade** if the requested slot is full
- **Pluggable slot assignment** via `SlotAssignmentStrategy` (nearest-to-gate, first-available, etc.)
- **Configurable billing** via `BillingStrategy` (e.g., `NormalBillingStrategy`)
- **Configurable pricing** via `PricingStrategy` with per-slot-type hourly rates
- **Live status display** via `StatusDisplayService`

---

## UML Diagram

<!-- Add your UML class diagram image here -->
<!-- Example: ![UML Diagram](./uml-diagram.png) -->

> **TODO**: Add UML class diagram here.

---

## Architecture

```
┌────────────────────────────────────────────────────────┐
│                     Main (Driver)                      │
└──────────┬─────────────────────────────────────────────┘
           │ creates & wires
           ▼
┌────────────────────┐    ┌──────────────────────────────┐
│   ParkingService   │───▶│   SlotAssignmentStrategy     │
│  park() / exit()   │    │  «interface»                 │
│  status()          │    └──────────┬───────────────────┘
└──┬────┬────────────┘               │
   │    │         ┌──────────────────┴──────────────────┐
   │    │         │                                     │
   │    │         ▼                                     ▼
   │    │  ┌─────────────────────────┐  ┌─────────────────────────────┐
   │    │  │ NearestSlotAssignment   │  │ FirstAvailableSlotStrategy  │
   │    │  │ Strategy                │  └─────────────────────────────┘
   │    │  └─────────────────────────┘
   │    │
   │    ├──▶ ┌──────────────────┐
   │    │    │ BillingStrategy  │ «interface»
   │    │    └──────┬───────────┘
   │    │           ▼
   │    │    ┌──────────────────────┐
   │    │    │ NormalBillingStrategy │
   │    │    └──────────────────────┘
   │    │
   │    └──▶ ┌──────────────────────┐
   │         │ StatusDisplayService │
   │         └──────────────────────┘
   ▼
┌──────────────────┐
│ PricingStrategy  │
└──────────────────┘
```

---

## SOLID Principles Applied

| Principle | How It's Applied |
|---|---|
| **Single Responsibility (SRP)** | `ParkingService` only orchestrates. Slot-finding is in `SlotAssignmentStrategy` implementations. Display is in `StatusDisplayService`. Billing is in `BillingStrategy` implementations. |
| **Open/Closed (OCP)** | New slot allocation algorithms (e.g., random, priority-based) are added by implementing `SlotAssignmentStrategy` — **zero changes** to `ParkingService`. Same for new billing models via `BillingStrategy`. |
| **Liskov Substitution (LSP)** | Any `SlotAssignmentStrategy` implementation can replace another without breaking `ParkingService`. `NearestSlotAssignmentStrategy` and `FirstAvailableSlotStrategy` are fully interchangeable. |
| **Interface Segregation (ISP)** | `SlotAssignmentStrategy` has a single focused method `findSlot()`. `BillingStrategy` has a single focused method `bill()`. No client is forced to depend on methods it doesn't use. |
| **Dependency Inversion (DIP)** | `ParkingService` depends on abstractions (`SlotAssignmentStrategy`, `BillingStrategy`), not on concrete implementations. Strategies are injected via the constructor. |

---

## Class Overview

| Class | Responsibility |
|---|---|
| `ParkingService` | Core orchestrator — delegates to strategies for parking, exit, and status |
| `SlotAssignmentStrategy` | Interface for finding an available parking slot |
| `NearestSlotAssignmentStrategy` | Allocates the nearest available slot to the entry gate (distance-based) |
| `FirstAvailableSlotStrategy` | Allocates the first available slot of the required type (ignores distance) |
| `BillingStrategy` | Interface for calculating the bill |
| `NormalBillingStrategy` | Hourly billing: `ceil(hours) × hourly_rate` |
| `PricingStrategy` | Maps slot types to hourly rates |
| `StatusDisplayService` | Prints parking lot availability per floor |
| `ParkingSlot` | Represents a single parking slot (id, type, floor, occupancy) |
| `EntryGate` | Represents an entry gate on a floor |
| `EntryGateSlotDistance` | Distance between a gate and a slot |
| `Vehicle` | A vehicle with registration number and type |
| `VehicleType` | Enum: `TWO_WHEELER`, `CAR`, `BUS` |
| `SlotType` | Enum: `SMALL`, `MEDIUM`, `LARGE` with upgrade chain |
| `Ticket` | Issued on park, used on exit for billing |

---

## Design Patterns Used

| Pattern | Where | Purpose |
|---|---|---|
| **Strategy** | `SlotAssignmentStrategy` | Swap slot-finding algorithms without modifying `ParkingService` |
| **Strategy** | `BillingStrategy` | Swap billing logic (normal, surge, subscription) without modifying `ParkingService` |
| **Builder** | `PricingStrategy.addRate()` | Fluent configuration of per-slot-type pricing |

---

## Slot Type Mapping & Upgrade

| Vehicle Type | Required Slot | Upgrade Path |
|---|---|---|
| TWO_WHEELER | SMALL | SMALL → MEDIUM → LARGE |
| CAR | MEDIUM | MEDIUM → LARGE |
| BUS | LARGE | — (no upgrade possible) |

If the required slot type is full, the system automatically upgrades to the next bigger slot.

---

## Pricing

| Slot Type | Hourly Rate |
|---|---|
| SMALL | ₹50 |
| MEDIUM | ₹100 |
| LARGE | ₹200 |

**Billing formula**: `ceil(duration_in_hours) × hourly_rate`

---

## How to Run

```bash
# Compile all Java files
javac *.java

# Run the demo
java Main
```

### Sample Output

```
╔══════════════════════════════════════════════════╗
║   DEMO 1: Nearest Slot Assignment Strategy      ║
╚══════════════════════════════════════════════════╝

========== PARKING LOT STATUS ==========
--- Floor 1 ---
  SMALL  : 2/2 available
  MEDIUM : 2/2 available
  LARGE  : 1/1 available
--- Floor 2 ---
  SMALL  : 1/1 available
  MEDIUM : 1/1 available
  LARGE  : 1/1 available
========================================

[ACTION] Parking car KA-01-AB-1234 via Gate 1...
  → Ticket#1 | Slot#3 | Type=MEDIUM

[ACTION] Parking bike KA-04-GH-3456 via Gate 1 (expect upgrade)...
  Upgraded KA-04-GH-3456 from SMALL to MEDIUM (requested slot type was full)
  → Ticket#4 | Slot#4 | Type=MEDIUM

[ACTION] Car KA-01-AB-1234 exiting...
  → Bill: ₹300.0 (MEDIUM slot, 3 hours)

╔══════════════════════════════════════════════════╗
║   DEMO 2: First Available Slot Strategy         ║
╚══════════════════════════════════════════════════╝

[ACTION] Parking car KA-06-KL-1111 via Gate 1 (FirstAvailable)...
  → Ticket#6 | Slot#3 | Type=MEDIUM

════════════════════════════════════════════════════
  Demo complete. Strategy pattern allows swapping
  slot algorithms with ZERO changes to ParkingService.
════════════════════════════════════════════════════
```

---

## Extending the System

### Adding a New Slot Assignment Algorithm

1. Create a new class implementing `SlotAssignmentStrategy`:

```java
public class RandomSlotStrategy implements SlotAssignmentStrategy {
    @Override
    public ParkingSlot findSlot(SlotType slotType, int entryGateId, int level) {
        // Your custom logic here
    }
}
```

2. Inject it into `ParkingService` — **no other code changes needed**.

### Adding a New Billing Model

1. Create a new class implementing `BillingStrategy`:

```java
public class SurgeBillingStrategy implements BillingStrategy {
    @Override
    public double bill(Ticket ticket, PricingStrategy pricingStrategy) {
        // Your custom billing logic here
    }
}
```

2. Inject it into `ParkingService` — **no other code changes needed**.
# ParkingLot
