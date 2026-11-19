public class BillingService {
    // Дефект 1 (Common Coupling): спільний глобальний змінний стан
    public static double GLOBAL_TAX_RATE = 0.20;

    public static class CustomerProfile {
        public int userId;
        public String email;
        public String passwordHash;
        public String city;
        public int loyaltyPoints;

        public CustomerProfile(int userId, String email, String passwordHash, String city, int loyaltyPoints) {
            this.userId = userId;
            this.email = email;
            this.passwordHash = passwordHash;
            this.city = city;
            this.loyaltyPoints = loyaltyPoints;
        }
    }

    // Дефект 2 (Stamp Coupling): приймає повну сутність CustomerProfile заради одного поля city
    public static double calculateDeliveryCost(CustomerProfile customer, double weightKg) {
        double baseRate = "Ivano-Frankivsk".equals(customer.city) ? 50.0 : 110.0;
        return baseRate + (weightKg * 12.0);
    }

    // Дефект 3 (Control Coupling): прапорець modeFlag керує ходом виконання методу
    public static String processInvoice(double amount, CustomerProfile customer, double weightKg, int modeFlag) {
        if (modeFlag == 1) {
            double delivery = calculateDeliveryCost(customer, weightKg);
            double total = (amount * (1 + GLOBAL_TAX_RATE)) + delivery;
            return "PROCESSED: " + Math.round(total * 100.0) / 100.0;
        } else if (modeFlag == 2) {
            GLOBAL_TAX_RATE = 0.0; // Псує глобальний стан для інших потоків!
            double total = amount + calculateDeliveryCost(customer, weightKg);
            return "TAX_EXEMPT: " + Math.round(total * 100.0) / 100.0;
        }
        return "CANCELLED: 0.0";
    }
}
