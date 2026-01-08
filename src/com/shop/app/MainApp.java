package com.shop.app;

import java.util.List;
import java.util.Scanner;

import com.shop.dao.AdminDao;
import com.shop.dao.ProductDao;
import com.shop.dao.UserDao;
import com.shop.model.Product;
import com.shop.model.User;
import com.shop.dao.OrderDao;

public class MainApp {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);

		ProductDao productDao = new ProductDao();
		UserDao userDao = new UserDao();
		AdminDao adminDao = new AdminDao();

		while (true) {
			System.out.println("\n=== ECOMMERCE CONSOLE APP ===");
			System.out.println("1. Guest");
			System.out.println("2. Login");
			System.out.println("3. Register");
			System.out.println("0. Exit");
			System.out.print("Enter choice: ");

			int choice = readInt(sc);

			switch (choice) {
			case 1:
				guestMenu(sc, productDao);
				break;

			case 2:
				loginAndRoute(sc, userDao, productDao, adminDao);
				break;

			case 3:
				doRegister(sc, userDao);
				break;

			case 0:
				sc.close();
				return;

			default:
				System.out.println("Invalid choice.");
			}
		}
	}

	private static void loginAndRoute(Scanner sc, UserDao userDao, ProductDao productDao, AdminDao adminDao) {
		sc.nextLine(); // consume newline after readInt
		try {
			System.out.print("Username: ");
			String username = sc.nextLine();

			System.out.print("Password: ");
			String password = sc.nextLine();

			User user = userDao.login(username, password);
			if (user == null) {
				System.out.println("Invalid login.");
				return;
			}

			if ("ADMIN".equalsIgnoreCase(user.getRole())) {
				System.out.println("Welcome Admin: " + user.getFirstName());
				adminMenu(sc, adminDao);
			} else {
				System.out.println("Welcome User: " + user.getFirstName());
				userAfterLoginMenu(sc, user, productDao);
			}
		} catch (Exception e) {
			System.out.println("Login error: " + e.getMessage());
		}
	}

	private static void userAfterLoginMenu(Scanner sc, User user, ProductDao productDao) {
		java.util.ArrayList<com.shop.model.CartItem> cart = new java.util.ArrayList<>();
		com.shop.dao.OrderDao orderDao = new com.shop.dao.OrderDao();

		while (true) {
			System.out.println("\n--- USER MENU (" + user.getUsername() + ") ---");
			System.out.println("1. View products");
			System.out.println("2. Add to cart");
			System.out.println("3. View cart");
			System.out.println("4. Purchase (bill)");
			System.out.println("5. View my orders");
			System.out.println("0. Logout");
			System.out.print("Enter choice: ");

			int choice = readInt(sc);
			if (choice == 0)
				return;

			switch (choice) {
			case 1:
				try {
					printProducts(productDao.getAllProductsSortedByPriceAsc());
				} catch (Exception e) {
					System.out.println("Error: " + e.getMessage());
				}
				break;

			case 2:
				addToCart(sc, cart, productDao);
				break;

			case 3:
				viewCart(cart);
				break;

			case 4:
				purchaseCart(user, cart, orderDao);
				break;

			case 5:
				try {
					orderDao.printOrdersForUser(user.getUserId());
				} catch (Exception e) {
					System.out.println("Error: " + e.getMessage());
				}
				break;

			default:
				System.out.println("Invalid choice.");
			}
		}
	}

	private static void guestMenu(Scanner sc, ProductDao productDao) {
		while (true) {
			System.out.println("\n--- GUEST MENU ---");
			System.out.println("1. View products (sort by price ASC)");
			System.out.println("0. Back");
			System.out.print("Enter choice: ");

			int choice = readInt(sc);
			if (choice == 0)
				return;

			switch (choice) {
			case 1:
				try {
					List<Product> products = productDao.getAllProductsSortedByPriceAsc();
					printProducts(products);
				} catch (Exception e) {
					System.out.println("Error: " + e.getMessage());
				}
				break;

			default:
				System.out.println("Invalid choice.");
			}
		}
	}

	private static void doRegister(Scanner sc, UserDao userDao) {
		sc.nextLine(); // consume newline after readInt
		try {
			User u = new User();

			System.out.print("First name: ");
			u.setFirstName(sc.nextLine());

			System.out.print("Last name: ");
			u.setLastName(sc.nextLine());

			System.out.print("Username: ");
			u.setUsername(sc.nextLine());

			System.out.print("Password: ");
			u.setPassword(sc.nextLine());

			System.out.print("City: ");
			u.setCity(sc.nextLine());

			System.out.print("Email: ");
			u.setEmail(sc.nextLine());

			System.out.print("Mobile: ");
			u.setMobile(sc.nextLine());

			boolean ok = userDao.register(u);
			System.out.println(ok ? "Registered successfully." : "Registration failed.");
		} catch (Exception e) {
			System.out.println("Registration error: " + e.getMessage());
		}
	}

	private static void adminMenu(Scanner sc, AdminDao adminDao) {
		while (true) {
			System.out.println("\n--- ADMIN MENU ---");
			System.out.println("1. Add product");
			System.out.println("2. Check quantity by product id");
			System.out.println("3. View user history by username");
			System.out.println("4. View registered users");
			System.out.println("0. Back");
			System.out.print("Enter choice: ");

			int choice = readInt(sc);
			if (choice == 0)
				return;

			switch (choice) {
			case 1:
				doAddProduct(sc, adminDao);
				break;

			case 2:
				doCheckQty(sc, adminDao);
				break;

			case 3:
				sc.nextLine(); // consume newline
				System.out.print("Enter username: ");
				String uname = sc.nextLine();
				try {
					adminDao.printUserHistoryByUsername(uname);
				} catch (Exception e) {
					System.out.println("Error: " + e.getMessage());
				}
				break;
			
			case 4:
			    try {
			        adminDao.printAllUsers();
			    } catch (Exception e) {
			        System.out.println("Error: " + e.getMessage());
			    }
			    break;

				
			default:
				System.out.println("Invalid choice.");
			}
		}
	}

	private static void doAddProduct(Scanner sc, AdminDao adminDao) {
		sc.nextLine(); // consume newline
		try {
			Product p = new Product();

			System.out.print("Product Id (number): ");
			p.setProductId(readInt(sc));
			sc.nextLine(); // consume newline

			System.out.print("Name: ");
			p.setName(sc.nextLine());

			System.out.print("Description: ");
			p.setDescription(sc.nextLine());

			System.out.print("Price: ");
			while (!sc.hasNextDouble()) {
				System.out.print("Enter valid price: ");
				sc.next();
			}
			p.setPrice(sc.nextDouble());

			System.out.print("Quantity: ");
			p.setQuantity(readInt(sc));

			boolean ok = adminDao.addProduct(p);
			System.out.println(ok ? "Product added." : "Add product failed.");
		} catch (Exception e) {
			System.out.println("Add product error: " + e.getMessage());
		}
	}

	private static void doCheckQty(Scanner sc, AdminDao adminDao) {
		try {
			System.out.print("Enter product id: ");
			int pid = readInt(sc);

			Integer qty = adminDao.checkQuantity(pid);
			if (qty == null)
				System.out.println("Product not found.");
			else
				System.out.println("Quantity is: " + qty);
		} catch (Exception e) {
			System.out.println("Check quantity error: " + e.getMessage());
		}
	}

	private static int readInt(Scanner sc) {
		while (!sc.hasNextInt()) {
			System.out.print("Please enter a number: ");
			sc.next();
		}
		return sc.nextInt();
	}

	private static void printProducts(List<Product> products) {
		System.out.printf("%-6s %-22s %-10s %-6s%n", "ID", "NAME", "PRICE", "QTY");
		System.out.printf("%-6s %-22s %-10s %-6s%n", "------", "----------------------", "----------", "------");
		for (Product p : products) {
			System.out.printf("%-6d %-22s %-10.2f %-6d%n", p.getProductId(), p.getName(), p.getPrice(),
					p.getQuantity());
		}
	}

	private static void addToCart(Scanner sc, java.util.List<com.shop.model.CartItem> cart, ProductDao productDao) {
		try {
			System.out.print("Enter product id: ");
			int pid = readInt(sc);

			System.out.print("Enter quantity: ");
			int qty = readInt(sc);

			if (qty <= 0) {
				System.out.println("Quantity must be > 0");
				return;
			}

			Product p = productDao.getById(pid);
			if (p == null) {
				System.out.println("Product not found.");
				return;
			}
			if (qty > p.getQuantity()) {
				System.out.println("Not enough stock. Available: " + p.getQuantity());
				return;
			}

			// if already in cart, just increase qty
			for (com.shop.model.CartItem ci : cart) {
				if (ci.getProductId() == pid) {
					ci.setQty(ci.getQty() + qty);
					System.out.println("Updated quantity in cart.");
					return;
				}
			}

			cart.add(new com.shop.model.CartItem(pid, p.getName(), p.getPrice(), qty));
			System.out.println("Added to cart.");
		} catch (Exception e) {
			System.out.println("Add to cart error: " + e.getMessage());
		}
	}

	private static void viewCart(java.util.List<com.shop.model.CartItem> cart) {
		if (cart.isEmpty()) {
			System.out.println("Cart is empty.");
			return;
		}
		double total = 0;
		System.out.printf("%-6s %-22s %-10s %-6s %-10s%n", "ID", "NAME", "PRICE", "QTY", "TOTAL");
		for (com.shop.model.CartItem ci : cart) {
			double line = ci.getLineTotal();
			total += line;
			System.out.printf("%-6d %-22s %-10.2f %-6d %-10.2f%n", ci.getProductId(), ci.getName(), ci.getPrice(),
					ci.getQty(), line);
		}
		System.out.println("Bill Amount: " + String.format("%.2f", total));
	}

	private static void purchaseCart(User user, java.util.List<com.shop.model.CartItem> cart,
			com.shop.dao.OrderDao orderDao) {

		if (cart.isEmpty()) {
			System.out.println("Cart is empty.");
			return;
		}

		try {
			int orderId = orderDao.placeOrder(user.getUserId(), cart);
			System.out.println("Purchase successful. Order ID: " + orderId);
			System.out.println("Bill:");
			viewCart(cart);
			cart.clear();
		} catch (Exception e) {
			System.out.println("Purchase failed: " + e.getMessage());
		}
	}
}