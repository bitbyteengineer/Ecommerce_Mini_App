package com.shop.dao;

import java.sql.*;
import java.util.*;
import com.shop.model.Product;
import com.shop.util.DBUtil;

public class ProductDao {

    public List<Product> getAllProductsSortedByPriceAsc() throws SQLException {
        String sql = "SELECT product_id, name, description, price, quantity " +
                     "FROM products ORDER BY price ASC";

        List<Product> list = new ArrayList<>();
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Product p = new Product();
                p.setProductId(rs.getInt("product_id"));
                p.setName(rs.getString("name"));
                p.setDescription(rs.getString("description"));
                p.setPrice(rs.getDouble("price"));
                p.setQuantity(rs.getInt("quantity"));
                list.add(p);
            }
        }
        return list;
    }
    
    public Product getById(int productId) throws SQLException {
        String sql = "SELECT product_id, name, description, price, quantity FROM products WHERE product_id = ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                Product p = new Product();
                p.setProductId(rs.getInt("product_id"));
                p.setName(rs.getString("name"));
                p.setDescription(rs.getString("description"));
                p.setPrice(rs.getDouble("price"));
                p.setQuantity(rs.getInt("quantity"));
                return p;
            }
        }
    }

    public boolean reduceQuantity(int productId, int qty) throws SQLException {
        // reduces only if enough stock exists
        String sql = "UPDATE products SET quantity = quantity - ? WHERE product_id = ? AND quantity >= ?";
        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, qty);
            ps.setInt(2, productId);
            ps.setInt(3, qty);
            return ps.executeUpdate() == 1;
        }
    }
}
