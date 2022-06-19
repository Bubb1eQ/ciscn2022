package ctf.dao.impl;

import ctf.dao.UserDao;
import ctf.database.SqliteConnection;
import ctf.utils.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDaoImpl implements UserDao {

    private static UserDao instance;
    private Connection connection;

    private UserDaoImpl() {
        connection = SqliteConnection.getInstance().getConnection();
    }

    public static UserDao getInstance() {
        if (instance == null) {
            try {
                instance = new UserDaoImpl();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    @Override
    public boolean checkUser(String username) {
        boolean canFind = false;
        PreparedStatement stmt = null;
        try {
            String sql = "SELECT username FROM users where username = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            canFind = rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(stmt);
        }
        return canFind;
    }

    @Override
    public boolean tryLogin(String username, String password) {
        boolean canLogin = false;
        PreparedStatement stmt = null;
        try {
            String sql = "SELECT * FROM users WHERE username = ? and password = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            canLogin = rs.next();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(stmt);
        }
        return canLogin;
    }
}
