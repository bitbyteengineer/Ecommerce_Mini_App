package com.shop.dao;

import java.sql.*;
import com.shop.model.User;
import com.shop.util.DBUtil;

public class UserDao {

	public boolean register(User u) throws SQLException {
		String sql = "INSERT INTO users(role, first_name, last_name, username, password, city, email, mobile) "
		           + "VALUES('USER', ?, ?, ?, ?, ?, ?, ?)";

	    try (Connection con = DBUtil.getConnection();
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setString(1, u.getFirstName());
	        ps.setString(2, u.getLastName());
	        ps.setString(3, u.getUsername());
	        ps.setString(4, u.getPassword());
	        ps.setString(5, u.getCity());
	        ps.setString(6, u.getEmail());
	        ps.setString(7, u.getMobile());

	        return ps.executeUpdate() == 1;
	    }
	}

    public User login(String username, String password) throws SQLException {
        String sql = "SELECT user_id, role, first_name, last_name, username "
                   + "FROM users WHERE username = ? AND password = ?";

        try (Connection con = DBUtil.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User u = new User();
                    u.setUserId(rs.getInt("user_id"));
                    u.setRole(rs.getString("role"));
                    u.setFirstName(rs.getString("first_name"));
                    u.setLastName(rs.getString("last_name"));
                    u.setUsername(rs.getString("username"));
                    return u;
                }
                return null;
            }
        }
    }
}
