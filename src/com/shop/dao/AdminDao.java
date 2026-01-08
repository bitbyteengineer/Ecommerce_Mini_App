package com.shop.dao;

import java.sql.*;
import com.shop.model.Product;
import com.shop.util.DBUtil;

public class AdminDao {

    public boolean addProduct(Product p) throws SQLException {
        String sql = "INSERT INTO products(product_id, name, description, price, quantity) "
                   + "VALUES(?, ?, ?, ?, ?)";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, p.getProductId());
            ps.setString(2, p.getName());
            ps.setString(3, p.getDescription());
            ps.setDouble(4, p.getPrice());
            ps.setInt(5, p.getQuantity());

            return ps.executeUpdate() == 1;
        }
    }
    
    public void printUserHistoryByUsername(String username) throws SQLException {
        String sql =
            "SELECT u.username, o.order_id, o.order_time, o.total_amount, " +
            "       oi.product_id, p.name AS product_name, oi.qty, oi.price_at_purchase " +
            "FROM users u " +
            "JOIN orders o ON u.user_id = o.user_id " +
            "JOIN order_items oi ON o.order_id = oi.order_id " +
            "JOIN products p ON oi.product_id = p.product_id " +
            "WHERE u.username = ? " +
            "ORDER BY o.order_id DESC";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                System.out.println("\nUSERNAME | ORDER_ID | TIME | TOTAL | PID | PRODUCT | QTY | PRICE");
                while (rs.next()) {
                    System.out.println(
                        rs.getString("username") + " | " +
                        rs.getInt("order_id") + " | " +
                        rs.getTimestamp("order_time") + " | " +
                        rs.getDouble("total_amount") + " | " +
                        rs.getInt("product_id") + " | " +
                        rs.getString("product_name") + " | " +
                        rs.getInt("qty") + " | " +
                        rs.getDouble("price_at_purchase")
                    );
                }
            }
        }
    }
    
    public void printAllUsers() throws SQLException {
        String sql = "SELECT user_id, role, first_name, last_name, username, email, mobile, city FROM users ORDER BY user_id";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            System.out.println("\nID | ROLE | NAME | USERNAME | EMAIL | MOBILE | CITY");
            boolean found = false;

            while (rs.next()) {
                found = true;
                System.out.println(
                    rs.getInt("user_id") + " | " +
                    rs.getString("role") + " | " +
                    rs.getString("first_name") + " " + rs.getString("last_name") + " | " +
                    rs.getString("username") + " | " +
                    rs.getString("email") + " | " +
                    rs.getString("mobile") + " | " +
                    rs.getString("city")
                );
            }

            if (!found) System.out.println("No users found.");
        }
    }
    
    public Integer checkQuantity(int productId) throws SQLException {
        String sql = "SELECT quantity FROM products WHERE product_id = ?";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, productId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt("quantity");
                return null;
            }
        }
    }
}
