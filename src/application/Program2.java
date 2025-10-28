package application;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

import java.util.List;
import java.util.Scanner;

public class Program2 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        DepartmentDao departmentDao = DaoFactory.createDepartmentDao();

        Department department1 = departmentDao.findById(1);
        System.out.println(department1);

        System.out.println();
        List<Department> allDepartments = departmentDao.findAll();
        for (Department d : allDepartments) {
            System.out.println(d);
        }

        System.out.println();
        // Department newDepartment = new Department(null, "Dpt2");
        // departmentDao.insert(newDepartment);
        System.out.println("Insert");
        // System.out.println("Id inserted: " + newDepartment.getId());

        System.out.println();
        Department targetDep = departmentDao.findById(8);
        targetDep.setName("RH");
        departmentDao.update(targetDep);
        System.out.println("Updated department");

        System.out.println();
        System.out.print("Enter an id to delete: ");
        departmentDao.deleteById(scanner.nextInt());
        System.out.println("Deleted department");
    }
}
