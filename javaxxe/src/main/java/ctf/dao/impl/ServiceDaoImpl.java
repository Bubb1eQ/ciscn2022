package ctf.dao.impl;

import ctf.dao.ServiceDao;
import ctf.database.SqliteConnection;
import ctf.model.Service;
import ctf.utils.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ServiceDaoImpl implements ServiceDao {
    private static ServiceDao instance;
    private Connection connection;

    private ServiceDaoImpl() {
        connection = SqliteConnection.getInstance().getConnection();
    }

    public static ServiceDao getInstance() {
        if (instance == null) {
            try {
                instance = new ServiceDaoImpl();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    @Override
    public void addService(Service service) {
        PreparedStatement stmt = null;
        try {
            String sql = "INSERT INTO services(uid, name, operation, params) VALUES (?, ?, ?, ?);";
            stmt = connection.prepareStatement(sql);

            stmt.setString(1, service.getUid());
            stmt.setString(2, service.getName());
            stmt.setString(3, service.getOperation());
            stmt.setString(4, service.getParams());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(stmt);
        }
    }

    @Override
    public Service getServiceById(String uid) {
        Service service = null;
        PreparedStatement stmt = null;
        try {
            String sql = "SELECT * FROM services where uid = ?;";

            stmt = connection.prepareStatement(sql);
            stmt.setString(1, uid);
            ResultSet rs = stmt.executeQuery();
            service = new Service();
            service.setUid(rs.getString("uid"));
            service.setName(rs.getString("name"));
            service.setOperation(rs.getString("operation"));
            service.setParams(rs.getString("params"));

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(stmt);
        }
        return service;
    }

    @Override
    public Service getServiceByName(String name) {
        Service service = null;
        PreparedStatement stmt = null;
        try {
            String sql = "SELECT * FROM services where name = ?;";

            stmt = connection.prepareStatement(sql);
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            service = new Service();
            service.setUid(rs.getString("uid"));
            service.setName(rs.getString("name"));
            service.setOperation(rs.getString("operation"));
            service.setParams(rs.getString("params"));

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(stmt);
        }
        return service;
    }
}
