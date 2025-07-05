package main.java.com.fawry.ecommerce.model;

import java.time.LocalDate;

public class Product {
    private final String name;
    private final double price;
    private final boolean isShippable;
    private final double weight;

    private int stock;
    private final LocalDate expiryDate;

    public Product(String name, double price, int stock, LocalDate expiryDate, boolean isShippable, double weight) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Product name cannot be empty");
        }
        if (price <= 0) {
            throw new IllegalArgumentException("Price must be positive");
        }
        if (stock < 0) {
            throw new IllegalArgumentException("Stock cannot be negative");
        }
        if (weight <= 0) {
            throw new IllegalArgumentException("Must have positive weight");
        }
        if (expiryDate != null && expiryDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Expiry date cannot be in the past");
        }

        this.name = name;
        this.price = price;
        this.stock = stock;
        this.expiryDate = expiryDate;
        this.isShippable = isShippable;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public boolean isShippable() {
        return isShippable;
    }

    public double getWeight() {
        return weight;
    }

    public boolean isExpiryable() {
        return expiryDate != null;
    }
    public void setStock(int stock) {
        if (stock < 0) {
            throw new IllegalArgumentException("Stock cannot be negative");
        }
        this.stock = stock;
    }
    @Override
    public String toString() {
        return String.format("%s (Stock: %d)", name, stock);
    }
}
