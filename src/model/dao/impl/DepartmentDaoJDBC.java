package model.dao.impl;

import db.DB;
import db.DBException;
import db.DBIntegrityException;
import model.dao.DepartmentDao;
import model.entities.Department;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDaoJDBC implements DepartmentDao {

    private Connection connection;

    public DepartmentDaoJDBC(Connection conn) {
        this.connection = conn;
    }

    private Department instantiateDepartment(ResultSet rs) throws SQLException {
        Department department = new Department();
        department.setId(rs.getInt("Id"));
        department.setName(rs.getString("Name"));

        return department;
    }

    @Override
    public Department findById(Integer id) {

        PreparedStatement stmt = null;
        ResultSet rs = null;
        String query = "SELECT * FROM department " +
                "WHERE Id = ?";

        try {
            stmt = connection.prepareStatement(query);
            stmt.setInt(1, id);

            rs = stmt.executeQuery();

            if (rs.next()) {
                Department d = instantiateDepartment(rs);
                return d;
            }
            return null;

        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        } finally {
            DB.closeStatement(stmt);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public List<Department> findAll() {

        PreparedStatement stmt = null;
        ResultSet rs = null;

        String query = "SELECT * FROM department";
        try {
            stmt = connection.prepareStatement(query);
            rs = stmt.executeQuery();

            List<Department> departments = new ArrayList<>();
            while (rs.next()) {
                Department d = instantiateDepartment(rs);
                departments.add(d);
            }

            return departments;
        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        }
    }

    @Override
    public void insert(Department obj) {

        PreparedStatement stmt = null;
        String query = "INSERT INTO department(Name) VALUES(?)";

        try {
            stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, obj.getName());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    int recoveredId = rs.getInt(1);
                    obj.setId(recoveredId);
                }
                DB.closeResultSet(rs);
            } else {
                throw new DBException("Insert error, no rows affected.");
            }
        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        } finally {
            DB.closeStatement(stmt);
        }
    }

    @Override
    public void update(Department obj) {

        PreparedStatement stmt = null;
        String query = "UPDATE department " +
                "SET name = ? " +
                "WHERE Id = ?";

        try {
            stmt = connection.prepareStatement(query);
            stmt.setString(1, obj.getName());
            stmt.setInt(2, obj.getId());

            stmt.execute();
        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        } finally {
            DB.closeStatement(stmt);
        }
    }

    @Override
    public void deleteById(Integer id) {

        PreparedStatement stmt = null;
        String query = "DELETE FROM department WHERE id = ?";

        try {
            stmt = connection.prepareStatement(query);
            stmt.setInt(1, id);

            int rowsAffected = stmt.executeUpdate();

            if(rowsAffected == 0) {
                throw new DBException("Delete error, id not found");
            }
        } catch (SQLException e) {
            throw new DBIntegrityException(e.getMessage());
        }
    }
}
