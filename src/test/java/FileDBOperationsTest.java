/**
 * Created by srujant on 23/6/16.
 */


import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FileDBOperationsTest {

    private static Logger logger = LoggerFactory.getLogger(FileDBOperationsTest.class.getName());

    private Employee employee1 = new Employee(1, "max", "jam", "max@kai", 23);
    private Employee employee2 = new Employee(2, "lam", "sam", "sax@kai", 22);
    private Employee employee3 = new Employee(3, "maxkaim", "bai", "bax@kai", 21);
    private Employee employee4 = new Employee(4, "tyson", "bai", "jax@kai", 21);


    private Bike duke = new Bike("Duke", 28, 50, 80000);
    private Bike ninjaKawasaki = new Bike("ninjaKawasaki", 20, 100, 300000);
    private Bike bullet = new Bike("bullet", 22, 150, 90000);

    private File dbPathDirectory;
    private FileDB fileDB = null;

    @Before
    public void before() throws IOException {
        Path dbPath = Files.createTempDirectory("db");
        dbPathDirectory = dbPath.toFile();
        fileDB = new FileDB(dbPathDirectory);
    }

    @After
    public void after() {
        try {
            File[] files=dbPathDirectory.listFiles();
            for(File file:files){
                try{
                    file.delete();
                }catch (Exception e){
                    throw new RuntimeException("Denied to delete fiel",e);
                }

            }
            Files.delete(Paths.get(dbPathDirectory.getAbsolutePath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testAdd() {
        EntityCRUD<Employee> entityCRUD = new EntityCRUD<>(fileDB, Employee.class);
        entityCRUD.add(employee1);
        entityCRUD.add(employee2);
        entityCRUD.add(employee3);
        entityCRUD.add(employee4);

        BasicCriteria basicCriteria = new BasicCriteria("firstName", Operand.EQUALS,"tyson");
        Assert.assertEquals(1,entityCRUD.get(basicCriteria).size());


        EntityCRUD<Bike> entityCRUD2 = new EntityCRUD<>(fileDB, Bike.class);
        entityCRUD2.add(duke);
        entityCRUD2.add(ninjaKawasaki);
        entityCRUD2.add(bullet);

        BasicCriteria basicCriteria2 = new BasicCriteria("name", Operand.STARTS_WITH,"D");
        Assert.assertEquals(1,entityCRUD2.get(basicCriteria2).size());
    }

    @Test
    public void testGetAll() {

        EntityCRUD<Employee> entityCRUD = new EntityCRUD<>(fileDB, Employee.class);
        entityCRUD.add(employee1);
        entityCRUD.add(employee2);
        entityCRUD.add(employee3);
        entityCRUD.add(employee4);
        Assert.assertEquals(4, entityCRUD.getAll().size());

        EntityCRUD<Bike> entityCRUD2 = new EntityCRUD<>(fileDB, Bike.class);
        entityCRUD2.add(duke);
        entityCRUD2.add(ninjaKawasaki);
        entityCRUD2.add(bullet);
        Assert.assertEquals(3, entityCRUD2.getAll().size());
    }

    @Test
    public void testGet() {


        EntityCRUD<Employee> entityCRUD = new EntityCRUD<>(fileDB, Employee.class);
        entityCRUD.add(employee1);
        entityCRUD.add(employee2);
        entityCRUD.add(employee3);
        entityCRUD.add(employee4);

        EntityCRUD<Bike> entityCRUD2 = new EntityCRUD<>(fileDB, Bike.class);
        entityCRUD2.add(duke);
        entityCRUD2.add(ninjaKawasaki);
        entityCRUD2.add(bullet);



        List<Employee> criteria1ExpectedResult = new ArrayList<>();
        List<Employee> criteria2ExpectedResult = new ArrayList<>();
        List<Employee> andCriteriaExpectedResult = new ArrayList<>();

        criteria1ExpectedResult.add(employee1);
        criteria1ExpectedResult.add(employee3);

        criteria2ExpectedResult.add(employee1);
        criteria2ExpectedResult.add(employee2);

        andCriteriaExpectedResult.add(employee1);


        List<Employee> criteria1ActualResult;
        List<Employee> criteria2ActualResult;
        List<Employee> andCriteriaActualResult;

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


        EntityCRUD<Employee> entityCRUD = new EntityCRUD<>(fileDB, Employee.class);
        entityCRUD.add(employee1);
        entityCRUD.add(employee2);
        entityCRUD.add(employee3);
        entityCRUD.add(employee4);

        BasicCriteria basicCriteria1 = new BasicCriteria("firstName", Operand.STARTS_WITH, "m");
        Employee employee1 = new Employee(0, "maxm", "kam", "max@kai", 23);
        entityCRUD.update(employee1, basicCriteria1);
        Assert.assertEquals(4, entityCRUD.getAll().size());
    }

    @Test
    public void testDelete() {

        EntityCRUD<Employee> entityCRUD = new EntityCRUD<>(fileDB, Employee.class);
        entityCRUD.add(employee1);
        entityCRUD.add(employee2);
        entityCRUD.add(employee3);
        entityCRUD.add(employee4);

        BasicCriteria basicCriteria1 = new BasicCriteria("firstName", Operand.EQUALS, "tyson");
        entityCRUD.delete(basicCriteria1);
        /*Below Assertion should fail. It confirms,record with given criteria is deleted*/
        Assert.assertEquals(1,entityCRUD.get(basicCriteria1).size());
    }
}
