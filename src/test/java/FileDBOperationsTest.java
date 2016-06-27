/**
 * Created by srujant on 23/6/16.
 */


import org.junit.*;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FileDBOperationsTest {

    private static Logger logger = LoggerFactory.getLogger(FileDBOperationsTest.class.getName());

    Employee employee1 = new Employee(1, "max", "jam", "max@kai", 23);
    Employee employee2 = new Employee(2, "lam", "sam", "sax@kai", 22);
    Employee employee3 = new Employee(3, "maxkaim", "bai", "bax@kai", 21);
    Employee employee4 = new Employee(4, "tyson", "bai", "jax@kai", 21);


    Bike duke = new Bike("Duke", 28, 50, 80000);
    Bike ninjaKawasaki = new Bike("ninjaKawasaki", 20, 100, 300000);
    Bike bullet = new Bike("bullet", 22, 150, 90000);


    @Test
    public void testAdd() {
        FileDB fileDb = new FileDB("/home/srujant/db");
        EntityCRUD<Employee> entityCRUD = new EntityCRUD<Employee>(fileDb, Employee.class);
        entityCRUD.add(employee1);
        entityCRUD.add(employee2);
        entityCRUD.add(employee3);
        entityCRUD.add(employee4);


        EntityCRUD<Bike> entityCRUD2 = new EntityCRUD<Bike>(fileDb, Bike.class);
        entityCRUD2.add(duke);
        entityCRUD2.add(ninjaKawasaki);
        entityCRUD2.add(bullet);
    }

    @Test
    public void testgetAll() {
        FileDB fileDb = new FileDB("/home/srujant/db");
        EntityCRUD<Employee> entityCRUD = new EntityCRUD<Employee>(fileDb, Employee.class);
        Assert.assertEquals(4, entityCRUD.getAll().size());


        EntityCRUD<Bike> entityCRUD2 = new EntityCRUD<Bike>(fileDb, Bike.class);
        Assert.assertEquals(3, entityCRUD2.getAll().size());
    }

    @Test
    public void testget() {

        FileDB fileDb = new FileDB("/home/srujant/db");

        List<Employee> criteria1ExpectedResult = new ArrayList<Employee>();
        List<Employee> criteria2ExpectedResult = new ArrayList<Employee>();
        List<Employee> andCriteriaExpectedResult = new ArrayList<Employee>();

        criteria1ExpectedResult.add(employee1);
        criteria1ExpectedResult.add(employee3);

        criteria2ExpectedResult.add(employee1);
        criteria2ExpectedResult.add(employee2);

        andCriteriaExpectedResult.add(employee1);


        List<Employee> criteria1ActualResult;
        List<Employee> criteria2ActualResult;
        List<Employee> andCriteriaActualResult;

        EntityCRUD<Employee> entityCRUD = new EntityCRUD<Employee>(fileDb, Employee.class);

        BasicCriteria basicCriteria1 = new BasicCriteria("firstName", Operand.STARTS_WITH, "m");
        BasicCriteria basicCriteria2 = new BasicCriteria("lastName", Operand.ENDS_WITH, "m");
        AndCriteria andCriteria = new AndCriteria(basicCriteria1, basicCriteria2);

        criteria1ActualResult = entityCRUD.get(basicCriteria1);
        criteria2ActualResult = entityCRUD.get(basicCriteria2);
        andCriteriaActualResult = entityCRUD.get(andCriteria);


        Assert.assertEquals(criteria1ExpectedResult.size(), criteria1ActualResult.size());
        Assert.assertEquals(criteria2ExpectedResult.size(), criteria2ActualResult.size());
        Assert.assertEquals(andCriteriaExpectedResult.size(), andCriteriaActualResult.size());


        Iterator criteria1ActualResultIterator = criteria1ActualResult.iterator();

        for (Employee employeeExpected : criteria1ExpectedResult) {
            Employee employee = (Employee) criteria1ActualResultIterator.next();
            Assert.assertEquals(employeeExpected.getId(), employee.getId());
        }
    }

    @Test
    public void testUpdate() {

        FileDB fileDb = new FileDB("/home/srujant/db");
        BasicCriteria basicCriteria1 = new BasicCriteria("firstName", Operand.STARTS_WITH, "m");
        EntityCRUD<Employee> entityCRUD = new EntityCRUD<Employee>(fileDb, Employee.class);
        Employee employee1 = new Employee(0, "maxm", "kam", "max@kai", 23);
        entityCRUD.update(employee1, basicCriteria1);
    }

    @Test
    public void testDelete() {
        FileDB fileDb = new FileDB("/home/srujant/db");
        BasicCriteria basicCriteria1 = new BasicCriteria("firstName", Operand.STARTS_WITH, "m");
        EntityCRUD<Employee> entityCRUD = new EntityCRUD<Employee>(fileDb, Employee.class);
         entityCRUD.delete(basicCriteria1);
    }

}
