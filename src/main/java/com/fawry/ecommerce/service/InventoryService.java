package main.java.com.fawry.ecommerce.service;

import main.java.com.fawry.ecommerce.model.Product;

public class InventoryService {
    private void validate(Product product, int quantity) {
        if (product == null) throw new IllegalArgumentException("Invalid product");
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be positive");
    }

    public void reduceStock(Product product, int quantity) {
        validate(product, quantity);
        if (product.getStock() < quantity)
            throw new IllegalStateException(String.format("Only %d units available for %s (requested %d)", product.getStock(), product.getName(), quantity));
        product.setStock(product.getStock() - quantity);
    }

    public void restock(Product product, int quantity) {
        validate(product, quantity);
        product.setStock(product.getStock() + quantity);
    }
}
