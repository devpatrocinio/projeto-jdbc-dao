package application;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Seller;

public class Program {

    public static void main(String[] args) {
        SellerDao sellerDao = DaoFactory.createSellerDao();

        Seller sellerTest = sellerDao.findById(1);
        System.out.println(sellerTest);
    }
}
