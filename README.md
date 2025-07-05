# Fawry E-Commerce System

Welcome to the Fawry E-Commerce System — a Java-based simulation of an e-commerce platform that handles inventory, shopping cart operations, shipping, and payment workflows.

This project was built to demonstrate core concepts like object-oriented design, service layering, and real-world business logic.

## Key Features

- Product management (expirable/non-expirable, shippable/non-shippable)
- Cart operations (add/remove/update items)
- Checkout with receipt generation
- Inventory validation
- Shipping cost calculation
- Payment processing

## Business Rules & Assumptions

### Delivery and Expiry
- Delivery takes **2 days** from order date
- Expirable products must be valid **through the delivery date**
  - Example: If a product expires tomorrow, it won’t be eligible for checkout since it won’t survive until delivery.
    
### Shipping Costs
- **Base cost**: 10.0 L.E
- **Per kilogram**: 5.0 L.E
- **Free shipping**: Orders ≥ 1000 L.E get free shipping
- **Maximum shipping weight**: 50 kg (system rejects heavier orders)

### Transactions
- **Maximum transaction amount**: 100,000 L.E
- **Refunds**: Treated as balance recharge (no dedicated refund function)

### Products
- All products have weight (even non-shippable ones)
  - Helps with warehouse inventory management
- Weight display:
  - <1 kg: shown in grams (e.g., 400g)
  - ≥1 kg: shown in kilograms (e.g., 1.20kg)

## Class Structure
com.fawry.ecommerce
├── model
│ ├── Cart.java # Cart operations
│ ├── Customer.java # Customer data and balance
│ └── Product.java # Product definitions
└── service
├── CartService.java # Cart calculations
├── CheckoutService.java # Checkout workflow
├── InventoryService.java # Stock management
├── PaymentService.java # Payment processing
└── ShippingService.java # Shipping calculations


## Example Usage

```java
// Create products
Product cheese = new Product("Cheese", 100.0, 20, LocalDate.now().plusDays(3), true, 0.4);
Product tv = new Product("TV", 500.0, 10, null, true, 5.0);

// Create customer
Customer customer = new Customer("cust-1", "John", 1000.0);

// Add to cart
Cart cart = new Cart();
cart.addItem(cheese, 2);
cart.addItem(tv, 1);

// Checkout
CheckoutService checkout = new CheckoutService(...);
checkout.checkout(customer, cart);

=== Fawry E-Commerce Receipt ===
Customer: John
-------------------------------
 2x Cheese               L.E200.00 (400g)
 1x TV                   L.E500.00 (5.00kg)
Total Ship Weight:  5.80kg
-------------------------------
Subtotal:          L.E700.00
Shipping:          L.E39.00
Total:             L.E739.00
New Balance:       L.E261.00
===============================
