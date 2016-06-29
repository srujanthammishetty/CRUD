import java.io.BufferedReader;
import java.io.FileReader;

/**
 * Created by srujant on 24/6/16.
 */
public class AndCriteria implements Criteria {

    Criteria criteriaOne;
    Criteria criteriaTwo;

    public Criteria getCriteria1() {
        return criteriaOne;
    }

    public void setCriteria1(Criteria criteriaOne) {
        this.criteriaOne = criteriaOne;
    }

    public Criteria getCriteria2() {
        return criteriaTwo;
    }

    public void setCriteria2(Criteria criteriaTwo) {
        this.criteriaTwo = criteriaTwo;
    }

    AndCriteria(Criteria criteriaOne, Criteria criteriaTwo){
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
