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
    public void insert(Seller obj) {

    }

    @Override
    public void update(Seller obj) {

    }

    @Override
    public void deleteById(Integer id) {

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

            while(rs.next()) {
                Department dep = departmentMap.get(rs.getInt("DepartmentId"));

                if(dep == null) {
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
        return null;
    }
}
