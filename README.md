# 🛒 Ecommerce Console Application (Java 8 + JDBC + MySQL)

A pure console-based Ecommerce application built using **Core Java (Java 8)**, **JDBC**, and **MySQL**. Supports role-based access (Guest/User/Admin), product browsing, shopping cart, purchases, and order history.

> 🎯 **Perfect for:** Learning JDBC | Database Design | Java Projects

---

## 📌 Quick Navigation

| Section | Description |
|---------|-------------|
| ✨ Features | What the app does |
| 🚀 Quick Start | How to run it |
| 🗄️ Database | SQL setup scripts |
| 📁 Structure | Project organization |
| 🤝 Contribute | How to contribute |

---

## 📋 Requirement

Build an ecommerce system for user **Ajay** with role-based features for guests, registered users, and administrators.

---

## ✨ Key Features

### 📦 Product Management
- Store 10 products in database
- Display products sorted by price (ascending order)

### 👤 User Features
- User registration & login
- Shopping cart (add/remove items)
- Purchase multiple products
- Auto bill calculation
- View order history

### 👨‍💼 Admin Features
- Add new products
- Check inventory by product ID
- View all registered users
- View user's purchase history

### 👥 Guest Features
- Browse products (read-only)
- Cannot purchase

---

## 🛠️ Technology Stack

| Component | Technology |
|-----------|-----------|
| Language | Java 8 |
| Database | MySQL 8.x |
| Database Access | JDBC |
| IDE | Eclipse |
| Database Client | DBeaver |

---

## 🚀 How to Run (5 MINUTES)

### Prerequisites
- [ ] Java JDK 8+ → [Download](https://www.oracle.com/java/technologies/downloads/)
- [ ] MySQL 8.x → [Download](https://dev.mysql.com/downloads/mysql/)
- [ ] Eclipse IDE → [Download](https://www.eclipse.org/downloads/)
- [ ] DBeaver (optional) → [Download](https://dbeaver.io/)

---

### STEP 1: Download MySQL Driver

1. Download MySQL Connector/J from https://dev.mysql.com/downloads/connector/j/
2. Save the `.jar` file (e.g., `mysql-connector-j-8.0.33.jar`)
3. Create Eclipse project: File → New → Java Project → `EcommerceConsoleApp`

---

### STEP 2: Configure MySQL Driver

1. Right-click project → Build Path → Configure Build Path
2. Click "Libraries" tab → "Add External JARs"
3. Select MySQL `.jar` file → Apply and Close

---

### STEP 3: Create Package Structure

In Eclipse, right-click `src` and create these packages:

```
src/
 ├── com.shop.app
 ├── com.shop.dao
 ├── com.shop.model
 └── com.shop.util
```

---

### STEP 4: Set Up MySQL Database

Open DBeaver and execute this SQL script:

```sql
CREATE DATABASE EcommerceDB;
USE EcommerceDB;

CREATE TABLE users (
  user_id INT AUTO_INCREMENT PRIMARY KEY,
  role ENUM('ADMIN','USER') NOT NULL DEFAULT 'USER',
  first_name VARCHAR(50) NOT NULL,
  last_name VARCHAR(50) NOT NULL,
  username VARCHAR(50) NOT NULL UNIQUE,
  password VARCHAR(100) NOT NULL,
  city VARCHAR(50),
  email VARCHAR(100),
  mobile VARCHAR(15)
) ENGINE=InnoDB;

CREATE TABLE products (
  product_id INT PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  description VARCHAR(255),
  price DECIMAL(10,2) NOT NULL,
  quantity INT NOT NULL
) ENGINE=InnoDB;

CREATE TABLE orders (
  order_id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT NOT NULL,
  order_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  total_amount DECIMAL(10,2) NOT NULL DEFAULT 0,
  CONSTRAINT fk_orders_user FOREIGN KEY (user_id) REFERENCES users(user_id)
) ENGINE=InnoDB;

CREATE TABLE order_items (
  item_id INT AUTO_INCREMENT PRIMARY KEY,
  order_id INT NOT NULL,
  product_id INT NOT NULL,
  qty INT NOT NULL,
  price_at_purchase DECIMAL(10,2) NOT NULL,
  CONSTRAINT fk_items_order FOREIGN KEY (order_id) REFERENCES orders(order_id),
  CONSTRAINT fk_items_product FOREIGN KEY (product_id) REFERENCES products(product_id)
) ENGINE=InnoDB;

INSERT INTO products(product_id, name, description, price, quantity) VALUES
(101,'Apple MacBook 2020','8 GB RAM, 256 SSD',85000.00,5),
(102,'OnePlus Mobile','16 GB RAM, 128 GB Storage',37500.00,3),
(103,'Bluetooth Headphones','Wireless over-ear',2999.00,10),
(104,'Keyboard','Mechanical keyboard',1999.00,12),
(105,'Mouse','Wireless mouse',799.00,20),
(106,'Pendrive 64GB','USB 3.0 storage',499.00,25),
(107,'Smart Watch','Fitness tracking watch',2499.00,8),
(108,'Backpack','Laptop backpack',1299.00,15),
(109,'Water Bottle','Steel bottle 1L',399.00,30),
(110,'Notebook','200 pages ruled',60.00,100);

INSERT INTO users(role, first_name, last_name, username, password, city, email, mobile)
VALUES ('ADMIN', 'Admin', 'User', 'admin', 'admin', 'NA', 'admin@gmail.com', '0000000000');
```

---

### STEP 5: Create Java Files

Create these Java files in Eclipse with appropriate package structure:

**Files to create:**
1. `com.shop.util.DBUtil.java` - Database connection utility
2. `com.shop.model.Product.java` - Product model with getters/setters
3. `com.shop.model.User.java` - User model with getters/setters
4. `com.shop.model.CartItem.java` - Cart item model
5. `com.shop.dao.ProductDao.java` - Product database operations
6. `com.shop.dao.UserDao.java` - User registration and login
7. `com.shop.dao.AdminDao.java` - Admin operations (add product, view users)
8. `com.shop.dao.OrderDao.java` - Order and checkout operations
9. `com.shop.app.MainApp.java` - Main application entry point

**Note:** Download complete Java source code from the GitHub repository or refer to the original project template for full implementations.

---

### STEP 6: Run the Application

1. Right-click `src/com/shop/app/MainApp.java`
2. Select `Run As → Java Application`
3. ✅ Application starts!

**Test Accounts:**
- Admin: `username: admin` | `password: admin`
- Create new user via registration option

---

## 📁 Project Structure

```
EcommerceConsoleApp/
├── src/com/shop/
│   ├── app/           → MainApp.java
│   ├── model/         → Product, User, CartItem classes
│   ├── dao/           → ProductDao, UserDao, AdminDao, OrderDao
│   └── util/          → DBUtil.java
├── lib/               → mysql-connector-j-8.0.33.jar
└── README.md          → Documentation
```

---

## 👥 User Roles

| Role | Features |
|------|----------|
| **Guest** | Browse products (read-only) |
| **User** | Register, Login, Add to cart, Purchase, View orders |
| **Admin** | Add products, Check inventory, View users, View user history |

---

## 🔐 Security Features

✅ **PreparedStatement** - Prevents SQL injection  
✅ **Input Validation** - Checks all user inputs  
✅ **Role-Based Access** - Different menus for each role  
✅ **ACID Transactions** - Atomic operations (all-or-nothing)  
✅ **Foreign Keys** - Referential integrity  

---

## 🆘 Troubleshooting

| Issue | Solution |
|-------|----------|
| "No suitable driver" | Add MySQL JAR to Build Path |
| "Access denied" | Check MySQL password in DBUtil.java |
| "Table doesn't exist" | Run all SQL scripts in DBeaver |
| Registration fails | Check username is unique |
| No order history | Complete a purchase first |

---

## 🤝 How to Contribute

### Fork & Clone Repository
```bash
git clone https://github.com/your-username/EcommerceConsoleApp.git
cd EcommerceConsoleApp
```

### Create Feature Branch
```bash
git checkout -b feature/your-feature-name
```

**Branch naming conventions:**
- `feature/wishlist` - New feature
- `bugfix/login-error` - Bug fix
- `docs/update-readme` - Documentation

### Commit & Push Changes
```bash
git add .
git commit -m "Add wishlist feature"
git push origin feature/your-feature-name
```

**Commit message guidelines:**
- Start with action verb: "Add", "Fix", "Update"
- Be concise and descriptive
- Use present tense

### Create Pull Request
1. Go to GitHub repository
2. Click "New Pull Request"
3. Write clear description including:
   - **What:** Changes made
   - **Why:** Reason for changes
   - **How:** Implementation approach
   - **Testing:** How you tested it

### Contribution Guidelines
✅ Follow Java naming conventions (camelCase, PascalCase)  
✅ Keep methods focused and under 30 lines  
✅ Add meaningful comments for complex logic  
✅ Handle exceptions properly  
✅ Test thoroughly before submitting PR  
✅ Don't break existing functionality  

---

## 🚀 Feature Ideas for Contributors

- 🔐 Password hashing (BCrypt)
- 🔍 Product search & filter
- ❤️ Wishlist feature
- ⭐ Product reviews & ratings
- 📊 Admin analytics
- 📧 Email notifications
- 📄 PDF invoices
- 🔄 Refund system
- 🎟️ Coupon codes
- 📍 Multiple addresses

---

## ⚠️ Important Notes

- Change MySQL password in `DBUtil.java` for production
- Stock reduces automatically after purchase
- Users see only their own orders
- All purchases are atomic (succeed or fail together)

---

## 📄 Project Information

| Detail | Information |
|--------|-------------|
| **Project** | Ecommerce Console Application |
| **Author** | Pranay Kadu |
| **Created** | January 2026 |
| **Purpose** | Java Learning Project |
| **Status** | ✅ Fully Functional |

---

## 💡 Learning Outcomes

✅ Core Java (OOP, Collections, Generics)  
✅ JDBC (Connection, Statement, ResultSet)  
✅ Database Design (Tables, Keys, Relationships)  
✅ SQL (SELECT, INSERT, UPDATE, DELETE, JOIN)  
✅ Transaction Management  
✅ Exception Handling  
✅ DAO Design Pattern  
✅ Role-Based Access Control  

---

**Happy Coding! 🚀**

For questions or issues, check the Troubleshooting section or open a GitHub Issue.
