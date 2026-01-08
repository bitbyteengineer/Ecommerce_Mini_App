package com.shop.dao;

import java.sql.*;
import java.util.List;

import com.shop.model.CartItem;
import com.shop.util.DBUtil;

public class OrderDao {

    // returns generated order_id
    public int placeOrder(int userId, List<CartItem> cart) throws SQLException {
        String insertOrderSql = "INSERT INTO orders(user_id, total_amount) VALUES(?, ?)";
        String insertItemSql  = "INSERT INTO order_items(order_id, product_id, qty, price_at_purchase) VALUES(?, ?, ?, ?)";
        String updateStockSql = "UPDATE products SET quantity = quantity - ? WHERE product_id = ? AND quantity >= ?";

        double total = 0;
        for (CartItem ci : cart) total += ci.getLineTotal();

        try (Connection con = DBUtil.getConnection()) {
            con.setAutoCommit(false); // start transaction [web:352]

            try {
                // 1) insert into orders and get generated order_id
                int orderId;
                try (PreparedStatement ps = con.prepareStatement(insertOrderSql, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setInt(1, userId);
                    ps.setDouble(2, total);
                    ps.executeUpdate();

                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (!rs.next()) throw new SQLException("Order id not generated.");
                        orderId = rs.getInt(1);
                    }
                } // getGeneratedKeys is the standard way to get AUTO_INCREMENT id. [web:377]

                // 2) for each cart item: reduce stock + insert order_items
                for (CartItem ci : cart) {
                    // 2a) update stock (fails if not enough quantity)
                    try (PreparedStatement ps = con.prepareStatement(updateStockSql)) {
                        ps.setInt(1, ci.getQty());
                        ps.setInt(2, ci.getProductId());
                        ps.setInt(3, ci.getQty());
                        int updated = ps.executeUpdate();
                        if (updated != 1) throw new SQLException("Not enough stock for product id " + ci.getProductId());
                    }

                    // 2b) insert order item
                    try (PreparedStatement ps = con.prepareStatement(insertItemSql)) {
                        ps.setInt(1, orderId);
                        ps.setInt(2, ci.getProductId());
                        ps.setInt(3, ci.getQty());
                        ps.setDouble(4, ci.getPrice());
                        ps.executeUpdate();
                    }
                }

                con.commit();   // finish transaction [web:352]
                return orderId;

            } catch (SQLException e) {
                con.rollback(); // undo everything if any step fails [web:352]
                throw e;
            } finally {
                con.setAutoCommit(true);
            }
        }
    }
    
    public void printOrdersForUser(int userId) throws SQLException {
        String sql =
            "SELECT o.order_id, o.order_time, o.total_amount, " +
            "       oi.product_id, p.name AS product_name, oi.qty, oi.price_at_purchase " +
            "FROM orders o " +
            "JOIN order_items oi ON o.order_id = oi.order_id " +
            "JOIN products p ON oi.product_id = p.product_id " +
            "WHERE o.user_id = ? " +
            "ORDER BY o.order_id DESC";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                System.out.println("\nORDER_ID | TIME | TOTAL | PID | PRODUCT | QTY | PRICE");
                while (rs.next()) {
                    System.out.println(
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

    
}
