package model.dao.impl;

import db.DB;
import db.DBException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellerDaoJDBC implements SellerDao {

    private Connection connection;

    public SellerDaoJDBC(Connection conn) {
        this.connection = conn;
    }

    private static Department instantianteDepartment(ResultSet rs) throws SQLException {
        Department dep = new Department();
        dep.setId(rs.getInt("DepartmentId"));
        dep.setName(rs.getString("DepName"));

        return dep;
    }

    private static Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {
        Seller seller = new Seller();
        seller.setId(rs.getInt("Id"));
        seller.setName(rs.getString("Name"));
        seller.setEmail(rs.getString("Email"));
        seller.setBirthDate(rs.getDate("BirthDate").toLocalDate());
        seller.setBaseSalary(rs.getDouble("BaseSalary"));
        seller.setDepartment(dep);

        return seller;
    }

    @Override
    public Seller findById(Integer id) {

        PreparedStatement stmt = null;
        ResultSet rs = null;
        String query = "SELECT seller.*, department.Name as DepName" +
                " FROM seller" +
                " INNER JOIN department" +
                " ON DepartmentId = department.id" +
                " WHERE seller.id = ?";

        try {
            stmt = connection.prepareStatement(query);
            stmt.setInt(1, id);

            rs = stmt.executeQuery();

            if (rs.next()) {
                Department dep = instantianteDepartment(rs);
                Seller seller = instantiateSeller(rs, dep);

                return seller;
            }

            return null;

        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        } finally {
            DB.closeStatement(stmt);
            DB.closeResultSet(rs);
        }
    }

    public List<Seller> findByDepartment(Department department) {

        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {

            String query = "SELECT seller.*, department.Name as DepName" +
                    " FROM seller" +
                    " INNER JOIN department " +
                    "ON seller.DepartmentId = department.id" +
                    " WHERE DepartmentId = ?" +
                    " ORDER BY Name";
            stmt = connection.prepareStatement(query);
            stmt.setInt(1, department.getId());

            rs = stmt.executeQuery();

            List<Seller> sellers = new ArrayList<>();
            Map<Integer, Department> departmentMap = new HashMap<>();

            while (rs.next()) {
                Department dep = departmentMap.get(rs.getInt("DepartmentId"));

                if (dep == null) {
                    dep = instantianteDepartment(rs);
                    departmentMap.put(rs.getInt("DepartmentId"), dep);
                }
                Seller seller = instantiateSeller(rs, dep);
                sellers.add(seller);
            }

            return sellers;

        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        } finally {
            DB.closeStatement(stmt);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public List<Seller> findAll() {

        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {

            String query = "SELECT seller.*, department.Name as DepName" +
                    " FROM seller" +
                    " INNER JOIN department ON seller.DepartmentId = department.id" +
                    " ORDER BY Name";

            stmt = connection.prepareStatement(query);
            rs = stmt.executeQuery();

            List<Seller> sellers = new ArrayList<>();
            Map<Integer, Department> departmentMap = new HashMap<>();

            while(rs.next()) {
                Department d = departmentMap.get(rs.getInt("DepartmentId"));

                if(d == null) {
                    d = instantianteDepartment(rs);
                    departmentMap.put(rs.getInt("DepartmentId"), d);
                }
                Seller s = instantiateSeller(rs, d);
                sellers.add(s);
            }

            return sellers;
        } catch (SQLException e) {
            throw new DBException(e.getMessage());
        } finally {
            DB.closeStatement(stmt);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public void insert(Seller obj) {
        PreparedStatement stmt = null;

        String query = "INSERT INTO seller(Name, Email, BirthDate, BaseSalary, DepartmentId) VALUES(?, ?, ?, ?, ?)";

        try {
            stmt = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            stmt.setString(1, obj.getName());
            stmt.setString(2, obj.getEmail());
            stmt.setDate(3, java.sql.Date.valueOf(obj.getBirthDate()));
            stmt.setDouble(4, obj.getBaseSalary());
            stmt.setInt(5, obj.getDepartment().getId());

            int rowsAffected = stmt.executeUpdate();

            if(rowsAffected > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if(rs.next()) {
                    int recoveredId = rs.getInt(1);
                    obj.setId(recoveredId);
                }
                DB.closeResultSet(rs);
            } else {
                throw new DBException("Insert error, no rows affected.");
            }
        } catch(SQLException e) {
            throw new DBException(e.getMessage());
        } finally {
            DB.closeStatement(stmt);
        }
    }

    @Override
    public void update(Seller obj) {

    }

    @Override
    public void deleteById(Integer id) {

    }
}
