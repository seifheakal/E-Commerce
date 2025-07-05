package main.java.com.fawry.ecommerce.service;

import main.java.com.fawry.ecommerce.model.Cart;

public class ShippingService {
    private static final double BASE_COST = 10.0;
    private static final double COST_PER_KILOGRAM = 5.0;
    private static final double FREE_SHIPPING_MIN = 1000.0;

    private final CartService cartService;

    public ShippingService(CartService cartService) {
        this.cartService = cartService;
    }

    public double calculateShippingCost(Cart cart) {
        if (cart == null) {
            throw new IllegalArgumentException("Cart cannot be null");
        }
        if (cart.isEmpty()) {
            return 0.0;
        }

        // Use CartService's subtotal calculation
        double subtotal = cartService.calculateSubtotal(cart);

        if (subtotal >= FREE_SHIPPING_MIN) {
            return 0.0;
        }

        double totalWeightKg = cartService.calculateShippingWeight(cart);

        return BASE_COST + (totalWeightKg * COST_PER_KILOGRAM);
    }
}
