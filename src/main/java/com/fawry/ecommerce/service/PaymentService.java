package main.java.com.fawry.ecommerce.service;


import main.java.com.fawry.ecommerce.model.Customer;

public class PaymentService {
    private static final double MAX_TRANSACTION = 100_000;

    public void processPayment(Customer customer, double amount) {
        validateAmount(amount);
        customer.deductBalance(amount);
    }

    public void rechargeBalance(Customer customer, double amount) {
        validateAmount(amount);
        customer.addBalance(amount);
    }

    private void validateAmount(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        if (amount > MAX_TRANSACTION) {
            throw new IllegalStateException(String.format("Transaction limit exceeded (Max: %,.2f)", MAX_TRANSACTION));
        }
    }

}
