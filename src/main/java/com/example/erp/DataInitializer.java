package com.example.erp;

import com.example.erp.entity.*;
import com.example.erp.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements ApplicationRunner {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final SupplierRepository supplierRepository;
    private final EmployeeRepository employeeRepository;
    private final SaleRepository saleRepository;
    private final PurchaseRepository purchaseRepository;
    private final ExpenseRepository expenseRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) {
        seedUsers();
        seedCategories();
        seedProducts();
        seedCustomers();
        seedSuppliers();
        seedEmployees();
        seedSales();
        seedPurchases();
        seedExpenses();
    }

    // ── Users ─────────────────────────────────────────────────────────────────
    private void createUser(String name, String email, String pwd, Role role) {
        User user = User.builder()
                .name(name)
                .email(email)
                .role(role)
                .status(ItemStatus.ACTIVE)
                .build();
        UserCredential credential = UserCredential.builder()
                .password(passwordEncoder.encode(pwd))
                .user(user)
                .build();
        user.setCredential(credential);
        userRepository.save(user);
    }

    private void seedUsers() {
        if (userRepository.existsByEmail("admin@store.com")) return;

        createUser("Admin User", "admin@store.com", "admin123", Role.ADMIN);
        createUser("Store Manager", "manager@store.com", "manager123", Role.MANAGER);
        createUser("Sales Staff", "staff@store.com", "staff123", Role.STAFF);

        log.info("==> Users seeded: admin@store.com / admin123");
    }

    // ── Categories ────────────────────────────────────────────────────────────
    private void seedCategories() {
        if (categoryRepository.count() > 0) return;

        categoryRepository.save(Category.builder().name("Tops")
                .description("T-shirts, shirts and blouses").status(ItemStatus.ACTIVE).build());
        categoryRepository.save(Category.builder().name("Bottoms")
                .description("Jeans, trousers and shorts").status(ItemStatus.ACTIVE).build());
        categoryRepository.save(Category.builder().name("Outerwear")
                .description("Jackets and coats").status(ItemStatus.ACTIVE).build());
        categoryRepository.save(Category.builder().name("Footwear")
                .description("Shoes and sneakers").status(ItemStatus.ACTIVE).build());
        categoryRepository.save(Category.builder().name("Accessories")
                .description("Belts, hats and bags").status(ItemStatus.ACTIVE).build());

        log.info("==> Categories seeded");
    }

    // ── Products ──────────────────────────────────────────────────────────────
    private void seedProducts() {
        if (productRepository.count() > 0) return;

        Category tops = categoryRepository.findAll().stream()
                .filter(c -> c.getName().equals("Tops")).findFirst().orElse(null);
        Category bottoms = categoryRepository.findAll().stream()
                .filter(c -> c.getName().equals("Bottoms")).findFirst().orElse(null);
        Category outerwear = categoryRepository.findAll().stream()
                .filter(c -> c.getName().equals("Outerwear")).findFirst().orElse(null);
        Category footwear = categoryRepository.findAll().stream()
                .filter(c -> c.getName().equals("Footwear")).findFirst().orElse(null);
        Category accessories = categoryRepository.findAll().stream()
                .filter(c -> c.getName().equals("Accessories")).findFirst().orElse(null);

        productRepository.save(Product.builder()
                .name("Classic Denim Jacket").sku("OUT-CDJ-M").category(outerwear)
                .unit("pcs").purchasePrice(new BigDecimal("1200")).sellingPrice(new BigDecimal("2499"))
                .taxRate(new BigDecimal("18")).stock(45).minStock(10)
                .size("M").color("Blue").brand("Levi's").gender(Gender.UNISEX)
                .status(ItemStatus.ACTIVE).build());

        productRepository.save(Product.builder()
                .name("Cotton V-Neck T-Shirt").sku("TOP-CVT-L").category(tops)
                .unit("pcs").purchasePrice(new BigDecimal("250")).sellingPrice(new BigDecimal("499"))
                .taxRate(new BigDecimal("5")).stock(8).minStock(15)
                .size("L").color("White").brand("BasicWear").gender(Gender.MEN)
                .status(ItemStatus.ACTIVE).build());

        productRepository.save(Product.builder()
                .name("Women High-Waist Jeans").sku("BOT-WHJ-28").category(bottoms)
                .unit("pcs").purchasePrice(new BigDecimal("800")).sellingPrice(new BigDecimal("1699"))
                .taxRate(new BigDecimal("12")).stock(120).minStock(20)
                .size("28").color("Black").brand("DenimCo").gender(Gender.WOMEN)
                .status(ItemStatus.ACTIVE).build());

        productRepository.save(Product.builder()
                .name("Running Sneakers").sku("FTW-RS-9").category(footwear)
                .unit("pairs").purchasePrice(new BigDecimal("2500")).sellingPrice(new BigDecimal("4200"))
                .taxRate(new BigDecimal("18")).stock(5).minStock(10)
                .size("9").color("Grey").brand("Puma").gender(Gender.MEN)
                .status(ItemStatus.ACTIVE).build());

        productRepository.save(Product.builder()
                .name("Kids Winter Beanie").sku("ACC-KWB-S").category(accessories)
                .unit("pcs").purchasePrice(new BigDecimal("150")).sellingPrice(new BigDecimal("350"))
                .taxRate(new BigDecimal("5")).stock(0).minStock(10)
                .size("S").color("Red").brand("CozyKids").gender(Gender.KIDS)
                .status(ItemStatus.ACTIVE).build());

        productRepository.save(Product.builder()
                .name("Floral Summer Dress").sku("TOP-FSD-M").category(tops)
                .unit("pcs").purchasePrice(new BigDecimal("600")).sellingPrice(new BigDecimal("1299"))
                .taxRate(new BigDecimal("5")).stock(30).minStock(8)
                .size("M").color("Pink").brand("StyleHer").gender(Gender.WOMEN)
                .status(ItemStatus.ACTIVE).build());

        log.info("==> Products seeded");
    }

    // ── Customers ─────────────────────────────────────────────────────────────
    private void seedCustomers() {
        if (customerRepository.count() > 0) return;

        customerRepository.save(Customer.builder()
                .name("Rahul Sharma").phone("9876543210").email("rahul@example.com")
                .totalOrders(12).totalSpent(new BigDecimal("45000")).outstanding(BigDecimal.ZERO)
                .status(ItemStatus.ACTIVE).build());

        customerRepository.save(Customer.builder()
                .name("Priya Patel").phone("8765432109").email("priya@example.com")
                .totalOrders(5).totalSpent(new BigDecimal("12500")).outstanding(new BigDecimal("2500"))
                .status(ItemStatus.ACTIVE).build());

        customerRepository.save(Customer.builder()
                .name("Amit Kumar").phone("7654321098")
                .totalOrders(2).totalSpent(new BigDecimal("3000")).outstanding(BigDecimal.ZERO)
                .status(ItemStatus.ACTIVE).build());

        customerRepository.save(Customer.builder()
                .name("Sneha Reddy").phone("9012345678").email("sneha@example.com")
                .totalOrders(8).totalSpent(new BigDecimal("18500")).outstanding(new BigDecimal("1000"))
                .status(ItemStatus.ACTIVE).build());

        customerRepository.save(Customer.builder()
                .name("Vikram Singh").phone("8123456789").email("vikram@example.com")
                .totalOrders(4).totalSpent(new BigDecimal("7200")).outstanding(BigDecimal.ZERO)
                .status(ItemStatus.ACTIVE).build());

        log.info("==> Customers seeded");
    }

    // ── Suppliers ─────────────────────────────────────────────────────────────
    private void seedSuppliers() {
        if (supplierRepository.count() > 0) return;

        supplierRepository.save(Supplier.builder()
                .name("TechZone Distributors").contactPerson("Sanjay Verma")
                .phone("9988776655").email("sales@techzone.in")
                .totalPurchases(new BigDecimal("150000")).outstanding(new BigDecimal("25000"))
                .status(ItemStatus.ACTIVE).build());

        supplierRepository.save(Supplier.builder()
                .name("FashionHub Wholesale").contactPerson("Neha Singh")
                .phone("8877665544").email("orders@fashionhub.in")
                .totalPurchases(new BigDecimal("45000")).outstanding(BigDecimal.ZERO)
                .status(ItemStatus.ACTIVE).build());

        supplierRepository.save(Supplier.builder()
                .name("DenimCo Exports").contactPerson("Rajesh Gupta")
                .phone("9966554433").email("supply@denimco.com")
                .totalPurchases(new BigDecimal("85000")).outstanding(new BigDecimal("10000"))
                .status(ItemStatus.ACTIVE).build());

        supplierRepository.save(Supplier.builder()
                .name("Global Footwear").contactPerson("Ravi Teja")
                .phone("9000111222").email("contact@globalfootwear.com")
                .totalPurchases(new BigDecimal("120000")).outstanding(new BigDecimal("5000"))
                .status(ItemStatus.ACTIVE).build());

        supplierRepository.save(Supplier.builder()
                .name("Kids Wear Factory").contactPerson("Pooja Sharma")
                .phone("9111222333").email("sales@kidswear.in")
                .totalPurchases(new BigDecimal("40000")).outstanding(BigDecimal.ZERO)
                .status(ItemStatus.ACTIVE).build());

        log.info("==> Suppliers seeded");
    }

    // ── Employees ─────────────────────────────────────────────────────────────
    private void seedEmployees() {
        if (employeeRepository.count() > 0) return;

        employeeRepository.save(Employee.builder()
                .name("Suresh Kumar").employeeId("EMP-001").department("Sales")
                .phone("9876543210").role(Role.MANAGER).status(ItemStatus.ACTIVE).build());

        employeeRepository.save(Employee.builder()
                .name("Anjali Verma").employeeId("EMP-002").department("Operations")
                .phone("8765432109").role(Role.STAFF).status(ItemStatus.ACTIVE).build());

        employeeRepository.save(Employee.builder()
                .name("Mohammed Khan").employeeId("EMP-003").department("Inventory")
                .phone("7654321098").role(Role.STAFF).status(ItemStatus.ACTIVE).build());

        employeeRepository.save(Employee.builder()
                .name("Sunita Rao").employeeId("EMP-004").department("Sales")
                .phone("8000111222").role(Role.STAFF).status(ItemStatus.ACTIVE).build());

        employeeRepository.save(Employee.builder()
                .name("David John").employeeId("EMP-005").department("Support")
                .phone("7111222333").role(Role.STAFF).status(ItemStatus.ACTIVE).build());

        log.info("==> Employees seeded");
    }

    // ── Sales, Purchases, Expenses ────────────────────────────────────────────

    private void seedSales() {
        if (saleRepository.count() > 0) return;
        Customer c1 = customerRepository.findAll().get(0);
        
        for (int i = 1; i <= 5; i++) {
            Sale sale = Sale.builder()
                    .invoiceNumber("INV-00" + i)
                    .customer(c1)
                    .saleDate(LocalDate.now().minusDays(i))
                    .subtotal(new BigDecimal(1000 * i))
                    .discountTotal(BigDecimal.ZERO)
                    .taxTotal(new BigDecimal(180 * i))
                    .total(new BigDecimal(1180 * i))
                    .paymentMethod(PaymentMethod.UPI)
                    .paymentStatus(PaymentStatus.PAID)
                    .status(SaleStatus.COMPLETED)
                    .build();
            saleRepository.save(sale);
        }
        log.info("==> Sales seeded");
    }

    private void seedPurchases() {
        if (purchaseRepository.count() > 0) return;
        Supplier s1 = supplierRepository.findAll().get(0);
        
        for (int i = 1; i <= 5; i++) {
            Purchase purchase = Purchase.builder()
                    .referenceNumber("PUR-00" + i)
                    .supplier(s1)
                    .purchaseDate(LocalDate.now().minusDays(i + 5))
                    .subtotal(new BigDecimal(5000 * i))
                    .discountTotal(BigDecimal.ZERO)
                    .taxTotal(new BigDecimal(900 * i))
                    .total(new BigDecimal(5900 * i))
                    .paymentMethod(PaymentMethod.BANK_TRANSFER)
                    .paymentStatus(PaymentStatus.PAID)
                    .status(PurchaseStatus.RECEIVED)
                    .build();
            purchaseRepository.save(purchase);
        }
        log.info("==> Purchases seeded");
    }

    private void seedExpenses() {
        if (expenseRepository.count() > 0) return;
        
        for (int i = 1; i <= 5; i++) {
            expenseRepository.save(Expense.builder()
                    .category("Utility")
                    .description("Monthly Utility Bill " + i)
                    .amount(new BigDecimal(1500 + i * 100))
                    .date(LocalDate.now().minusDays(i))
                    .paymentMethod("Bank Transfer")
                    .status(ExpenseStatus.PAID)
                    .build());
        }
        log.info("==> Expenses seeded");
    }
}
