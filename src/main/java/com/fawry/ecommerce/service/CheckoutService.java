package main.java.com.fawry.ecommerce.service;

import main.java.com.fawry.ecommerce.model.Cart;
import main.java.com.fawry.ecommerce.model.Customer;
import main.java.com.fawry.ecommerce.model.Product;

import java.time.LocalDate;

public class CheckoutService {
    private final InventoryService inventoryService;
    private final PaymentService paymentService;
    private final ShippingService shippingService;
    private final CartService cartService;
    private static final int DELIVERY_DAYS = 2;

    public CheckoutService(InventoryService inventoryService, PaymentService paymentService, ShippingService shippingService, CartService cartService) {
        this.inventoryService = inventoryService;
        this.paymentService = paymentService;
        this.shippingService = shippingService;
        this.cartService = cartService;
    }

    public void checkout(Customer customer, Cart cart) {

        cartService.validateCartNotEmpty(cart);
        cartService.validateShippingWeight(cart);


        validateInventory(cart);


        double subtotal = cartService.calculateSubtotal(cart);
        double shippingCost = shippingService.calculateShippingCost(cart);
        double total = subtotal + shippingCost;


        paymentService.processPayment(customer, total);


        updateInventory(cart);


        printReceipt(customer, cart, subtotal, shippingCost, total);
    }

    private void validateInventory(Cart cart) {
        cart.getItems().forEach((product, quantity) -> {
            if (product.getStock() < quantity) {
                throw new IllegalStateException(String.format("Insufficient stock for %s (Available: %d, Requested: %d)", product.getName(), product.getStock(), quantity));
            }
            if (product.getExpiryDate() != null) {
                if (product.isShippable()) {
                    //  Item must still be good when it arrives (2 days from now)
                    LocalDate deliveryDate = LocalDate.now().plusDays(DELIVERY_DAYS);
                    if (product.getExpiryDate().isBefore(deliveryDate)) {
                        throw new IllegalStateException(String.format("%s will expire BEFORE delivery! (Expires: %s, Arrives: %s)", product.getName(), product.getExpiryDate(), deliveryDate));
                    }
                } else {
                    if (product.getExpiryDate().isBefore(LocalDate.now())) {
                        throw new IllegalStateException(String.format("%s is already EXPIRED! (Expired on: %s)", product.getName(), product.getExpiryDate()));
                    }
                }
            }
        });
    }

    private void updateInventory(Cart cart) {
        cart.getItems().forEach((product, quantity) -> {
            inventoryService.reduceStock(product, quantity);
        });
    }

    private void printReceipt(Customer customer, Cart cart, double subtotal, double shipping, double total) {
        System.out.println("\n=== Fawry E-Commerce Receipt ===");
        System.out.printf("Customer: %s\n", customer.getName());
        System.out.println("-------------------------------");

        cart.getItems().forEach((product, qty) -> {

            System.out.printf("%2dx %-20s L.E%,.2f", qty, product.getName(), product.getPrice() * qty);

            if (product.isShippable()) {
                String weightStr = product.getWeight() < 1.0 ? String.format("%.0fg", product.getWeight() * 1000) : // Show grams
                        String.format("%.2fkg", product.getWeight());      // Show kg
                System.out.printf(" (%s)", weightStr);
            }
            System.out.println();
        });

        // Total weight footer (only if shippable items exist)
        if (cart.getItems().keySet().stream().anyMatch(Product::isShippable)) {
            double totalWeightKg = cartService.calculateShippingWeight(cart);
            String totalWeightStr = totalWeightKg < 1.0 ? String.format("%.0fg", totalWeightKg * 1000) : String.format("%.2fkg", totalWeightKg);
            System.out.printf("Total Ship Weight:  %s\n", totalWeightStr);
        }

        System.out.println("-------------------------------");
        System.out.printf("Subtotal:          L.E%,.2f\n", subtotal);
        System.out.printf("Shipping:          L.E%,.2f\n", shipping);
        System.out.printf("Total:             L.E%,.2f\n", total);
        System.out.printf("New Balance:       L.E%,.2f\n", customer.getBalance());
        System.out.println("===============================");
    }

}
