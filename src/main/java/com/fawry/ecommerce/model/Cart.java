package main.java.com.fawry.ecommerce.model;

import java.util.LinkedHashMap;
import java.util.Collections;
import java.util.Map;

public class Cart {
    private final Map<Product, Integer> items = new LinkedHashMap<>(); // Product -> Quantity

    public void addItem(Product product, int quantity) {
        validateProductNotNull(product);
        validateQuantityPositive(quantity);
        items.merge(product, quantity, Integer::sum);
    }

    public void removeItem(Product product) {
        validateProductNotNull(product);
        validateProductInCart(product);
        items.remove(product);
    }

    public void updateQuantity(Product product, int newQuantity) {
        validateProductNotNull(product);
        validateProductInCart(product);

        if (newQuantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative (" + newQuantity + ")");
        }

        if (newQuantity > 0) {
            validateStock(product, newQuantity);
            items.put(product, newQuantity);
        } else {
            removeItem(product);
        }
    }

    public Map<Product, Integer> getItems() {
        return Collections.unmodifiableMap(new LinkedHashMap<>(items));
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    private void validateProductNotNull(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
    }

    private void validateQuantityPositive(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
    }

    private void validateProductInCart(Product product) {
        if (!items.containsKey(product)) {
            throw new IllegalArgumentException("Product not in cart: " + product.getName());
        }
    }

    private void validateStock(Product product, int quantity) {
        if (product.getStock() < quantity) {
            throw new IllegalArgumentException("Insufficient stock for " + product.getName() + " (Available: " + product.getStock() + ", Requested: " + quantity + ")");
        }
    }
}
