package application;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.time.LocalDate;

public class Program {

    public static void main(String[] args) {
        Department d = new Department(10, "TI");
        Seller s = new Seller(25, "João", "joao@gmail.com", LocalDate.now(), 3000.0, d);

        System.out.println(d);
        System.out.println(s);

        SellerDao sellerDao = DaoFactory.createSellerDao();
    }
}
