import java.util.HashMap;
import java.util.Map;

public class PricingStrategy {

    private final Map<SlotType, Double> hourlyRates;

    public PricingStrategy() {
        this.hourlyRates = new HashMap<>();
    }

    public PricingStrategy addRate(SlotType slotType, double hourlyRate) {
        hourlyRates.put(slotType, hourlyRate);
        return this;
    }

    public double getHourlyRate(SlotType slotType) {
        if (!hourlyRates.containsKey(slotType)) {
            throw new IllegalArgumentException("No pricing configured for slot type: " + slotType);
        }
        return hourlyRates.get(slotType);
    }
}
