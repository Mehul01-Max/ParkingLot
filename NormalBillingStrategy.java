import java.time.Duration;

public class NormalBillingStrategy implements BillingStrategy {

    @Override
    public double bill(Ticket ticket, PricingStrategy pricingStrategy) {
        long minutes = Duration.between(ticket.getEntryTime(), ticket.getExitTime()).toMinutes();
        long hoursCeiled = (long) Math.ceil(minutes / 60.0);
        double hourlyRate = pricingStrategy.getHourlyRate(ticket.getAssignedSlotType());
        return hourlyRate * hoursCeiled;
    }
}
