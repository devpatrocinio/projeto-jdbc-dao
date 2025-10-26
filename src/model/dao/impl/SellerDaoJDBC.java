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
import java.util.List;

public class SellerDaoJDBC implements SellerDao {

    private Connection connection;

    public SellerDaoJDBC(Connection conn) {
        this.connection = conn;
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
                Department dep = new Department();
                dep.setId(rs.getInt("DepartmentId"));
                dep.setName(rs.getString("DepName"));

                return new Seller(rs.getInt("Id"), rs.getString("Name"), rs.getString("Email"), rs.getDate("BirthDate").toLocalDate(), rs.getDouble("BaseSalary"), dep);
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
    public List<Seller> findAll() {
        return null;
    }
}
