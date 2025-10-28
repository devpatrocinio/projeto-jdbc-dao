package application;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class Program {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        SellerDao sellerDao = DaoFactory.createSellerDao();

        System.out.println("--- Teste 1 --- findById");
        Seller sellerTest1 = sellerDao.findById(1);
        System.out.println(sellerTest1);

        Department department = new Department(2, null);
        List<Seller> sellerList = sellerDao.findByDepartment(department);

        System.out.println();
        System.out.println("--- Teste 2 --- findByDepartment");
        for(Seller s : sellerList) {
            System.out.println(s);
            System.out.println("Department hash code: " + s.getDepartment().hashCode());
        }

        System.out.println();
        System.out.println("--- Teste 3 --- findAll");
        List<Seller> allSellers = sellerDao.findAll();
        for(Seller s : allSellers) {
            System.out.println(s);
            System.out.println("Department hash code: " + s.getDepartment().hashCode());
        }

        System.out.println();
        System.out.println("--- Teste 4 --- insert");
//        department = new Department(3, null);
//        Seller newSeller = new Seller(null,"Goku", "goku@gmail.com", LocalDate.parse("20/10/2003", fmt), 2000.0, department);
//
//        sellerDao.insert(newSeller);
//        System.out.println("Inserted seller id: " + newSeller.getId());

        System.out.println();
        System.out.println("--- Teste 5 --- update");
        sellerTest1 = sellerDao.findById(1);
        sellerTest1.setName("Ash");
        sellerTest1.setEmail("ash@globo.com");

        sellerDao.update(sellerTest1);
        System.out.println("Update completed");

        System.out.println();
        System.out.println("--- Teste 6 --- delete");
        System.out.print("Enter an id to delete: ");
        sellerDao.deleteById(scanner.nextInt());
        System.out.println("Seller deleted");
    }
}
