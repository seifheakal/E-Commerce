package main.java.com.fawry.ecommerce.model;

public class Customer {
    private final String id;
    private final String name;
    private double balance;

    public Customer(String id, String name, double initialBalance) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("Customer ID cannot be empty");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Customer name cannot be empty");
        if (initialBalance < 0) throw new IllegalArgumentException("Initial balance cannot be negative");

        this.id = id;
        this.name = name;
        this.balance = initialBalance;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getBalance() {
        return balance;
    }

    public void deductBalance(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Deduction amount cannot be negative");
        }
        if (balance < amount) {
            throw new IllegalStateException(String.format("Insufficient balance (%.2f < %.2f)", balance, amount));
        }
        balance -= amount;
    }

    public void addBalance(double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Added amount cannot be negative");
        }
        balance += amount;
    }
}
