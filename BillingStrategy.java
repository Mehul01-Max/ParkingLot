public interface BillingStrategy {

    double bill(Ticket ticket, PricingStrategy pricingStrategy);
}
