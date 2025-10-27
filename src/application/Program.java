package application;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.util.List;

public class Program {

    public static void main(String[] args) {
        SellerDao sellerDao = DaoFactory.createSellerDao();

        System.out.println("--- Teste 1 --- findById 1");
        Seller sellerTest1 = sellerDao.findById(1);
        System.out.println(sellerTest1);

        Department department = new Department(2, "Estoque");
        List<Seller> sellerList = sellerDao.findByDepartment(department);

        System.out.println();
        System.out.println("--- Teste 2 --- findByDepartment " + department.getId());
        for(Seller s : sellerList) {
            System.out.println(s);
            System.out.println("Hash code: " + s.getDepartment().hashCode());
        }
    }
}
