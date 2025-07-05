package main.java.com.fawry.ecommerce;

import main.java.com.fawry.ecommerce.model.*;
import main.java.com.fawry.ecommerce.service.*;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        // Setup services
        CartService cartService = new CartService();
        InventoryService inventoryService = new InventoryService();
        PaymentService paymentService = new PaymentService();
        ShippingService shippingService = new ShippingService(cartService);
        CheckoutService checkoutService = new CheckoutService(inventoryService, paymentService, shippingService, cartService);

        // Create test products
        Product cheese = new Product("Cheese", 100.0, 5, LocalDate.now().plusDays(3), true, 0.4);  // Expires in 3 days
        Product tv = new Product("TV", 500.0, 2, null, true, 5.0);  // Only 2 in stock
        Product scratchCard = new Product("Scratch Card", 50.0, 100, null, false, 0.05);
        Product milk = new Product("Milk", 80.0, 10, LocalDate.now().plusDays(1), true, 1.0);  // Expires TOMORROW (delivery fails)

        // Create customers
        Customer Seif = new Customer("cust-1", "Seif", 1000.0);
        Customer Tamer = new Customer("cust-2", "Tamer", 200.0);
        Customer Heakal = new Customer("cust-3", "Heakal", 5000.0);

        // ===== TEST 1: Normal checkout =====
        System.out.println("\n===== TEST 1: Normal Checkout =====");
        Cart cart1 = new Cart();
        cart1.add(cheese, 2);
        cart1.add(scratchCard, 1);
        checkoutService.checkout(Seif, cart1);
        System.out.println("Remaining cheese: " + cheese.getStock());

        // ===== TEST 2: Expired item during delivery =====
        System.out.println("\n===== TEST 2: Expired Item =====");
        Cart cart2 = new Cart();
        cart2.add(milk, 2);
        try {
            checkoutService.checkout(Tamer, cart2);
        } catch (IllegalStateException e) {
            System.out.println("EXPECTED ERROR: " + e.getMessage());//delivery time is 2 days asumption
        }

        // ===== TEST 3: Out of stock =====
        System.out.println("\n===== TEST 3: Out of Stock =====");
        Cart cart3 = new Cart();
        cart3.add(tv, 3);
        try {
            checkoutService.checkout(Heakal, cart3);
        } catch (IllegalStateException e) {
            System.out.println("EXPECTED ERROR: " + e.getMessage());
        }

        // ===== TEST 4: Empty cart =====
        System.out.println("\n===== TEST 4: Empty Cart =====");
        Cart cart4 = new Cart();
        try {
            checkoutService.checkout(Seif, cart4);
        } catch (IllegalStateException e) {
            System.out.println("EXPECTED ERROR: " + e.getMessage());
        }

        // ===== TEST 5: Insufficient balance =====
        System.out.println("\n===== TEST 5: Insufficient Balance =====");
        Cart cart5 = new Cart();
        cart5.add(tv, 1);
        try {
            checkoutService.checkout(Tamer, cart5);
        } catch (IllegalStateException e) {
            System.out.println("EXPECTED ERROR: " + e.getMessage());
        }

        // ===== TEST 6: Cart modifications (add/remove) =====
        System.out.println("\n===== TEST 6: Cart Modifications =====");
        Cart cart6 = new Cart();
        cart6.add(cheese, 3);
        cart6.add(tv, 1);
        System.out.println("Cart before removal: " + cart6.getItems());
        cart6.removeItem(tv);
        System.out.println("Cart after removal: " + cart6.getItems());
        checkoutService.checkout(Heakal, cart6);

        // ===== TEST 7: Free shipping (order > 1000) =====
        System.out.println("\n===== TEST 7: Free Shipping =====");
        Cart cart7 = new Cart();
        cart7.add(tv, 2);
        checkoutService.checkout(Heakal, cart7);//assume order subtotal is above 1000, so shipping is free
    }
}