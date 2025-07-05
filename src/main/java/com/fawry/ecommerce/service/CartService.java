package main.java.com.fawry.ecommerce.service;

import main.java.com.fawry.ecommerce.model.Cart;

public class CartService {
    private static final double MAX_SHIPPING_WEIGHT = 50.0;

    public double calculateSubtotal(Cart cart) {
        return cart.getItems().entrySet().stream().mapToDouble(e -> e.getKey().getPrice() * e.getValue()).sum();
    }

    public int calculateTotalItems(Cart cart) {
        return cart.getItems().values().stream().mapToInt(Integer::intValue).sum();
    }

    public void validateCartNotEmpty(Cart cart) {
        if (cart.isEmpty()) {
            throw new IllegalStateException("Cannot process empty cart");
        }
    }

    public void validateShippingWeight(Cart cart) {
        if (cart == null) {
            throw new IllegalArgumentException("Cart cannot be null");
        }
        double totalWeightKg = calculateShippingWeight(cart);
        if (totalWeightKg > MAX_SHIPPING_WEIGHT) {
            throw new IllegalStateException(String.format("Maximum shipping weight exceeded (%.1fkg > %.1fkg)", totalWeightKg, MAX_SHIPPING_WEIGHT));
        }
    }

    public double calculateShippingWeight(Cart cart) {
        return cart.getItems().entrySet().stream().filter(e -> e.getKey().isShippable()).mapToDouble(e -> e.getKey().getWeight() * e.getValue()).sum();
    }
}
