# Fawry E-Commerce System

A robust Java implementation of an e-commerce platform with comprehensive inventory management, shopping cart operations, and checkout processing.

## Key Features

- **Product Management**:
  - Support for expirable (e.g., food) and non-expirable products (e.g., electronics)
  - Shippable and non-shippable items with weight tracking
  - Real-time stock validation

- **Cart Operations**:
  - Add/remove/update items
  - Quantity validation
  - Automatic stock reservation

- **Checkout System**:
  - Multi-step validation (stock, expiry, balance)
  - Detailed receipt generation
  - Shipping cost calculation
  - Payment processing

- **Error Handling**:
  - Comprehensive validation for all operations
  - Clear error messages for business rule violations

## Business Rules & Specifications

### Delivery and Product Expiry
- **Delivery Period**: 2 days from order date
- **Expiry Validation**: Products must remain valid through delivery date
  - Example: Product expiring tomorrow cannot be delivered (arrives in 2 days)

### Shipping Calculations
| Component | Value |
|-----------|-------|
| Base Cost | 10.0 L.E |
| Per Kilogram | 5.0 L.E |
| Free Shipping Threshold | ≥ 1000 L.E order value |
| Maximum Weight | 50 kg |

### Financial Rules
- **Transaction Limit**: 100,000 L.E maximum
- **Balance Management**:
  - Negative balances prohibited
  - Refunds processed as balance recharge

### Product Requirements
- All products require weight specification
  - Helps with warehouse inventory management
- Weight display:
  - <1 kg: grams (e.g., 400g)
  - ≥1 kg: kilograms (e.g., 1.20kg)

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
Customer customer = new Customer("cust-1", "Seif", 1000.0);

// Add to cart
Cart cart = new Cart();
cart.addItem(cheese, 2);
cart.addItem(tv, 1);

// Checkout
CheckoutService checkout = new CheckoutService(...);
checkout.checkout(customer, cart);

=== Fawry E-Commerce Receipt ===
Customer: Seif
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
