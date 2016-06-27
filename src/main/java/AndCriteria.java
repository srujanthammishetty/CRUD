import java.io.BufferedReader;
import java.io.FileReader;

/**
 * Created by srujant on 24/6/16.
 */
public class AndCriteria implements Criteria {

    BasicCriteria criteriaOne;
    BasicCriteria criteriaTwo;

    public BasicCriteria getCriteria1() {
        return criteriaOne;
    }

    public void setCriteria1(BasicCriteria criteriaOne) {
        this.criteriaOne = criteriaOne;
    }

    public BasicCriteria getCriteria2() {
        return criteriaTwo;
    }

    public void setCriteria2(BasicCriteria criteriaTwo) {
        this.criteriaTwo = criteriaTwo;
    }

    AndCriteria(BasicCriteria criteriaOne, BasicCriteria criteriaTwo){
        this.criteriaOne=criteriaOne;
        this.criteriaTwo=criteriaTwo;
    }


    public boolean isMatching(String[] columnNames, String[] rowData){

        if(criteriaOne.isMatching(columnNames,rowData) && criteriaTwo.isMatching(columnNames,rowData)) {
            return true;
        }
        return false;
    }


}
