import java.util.Arrays;

/**
 * Created by srujant on 24/6/16.
 */
public class BasicCriteria implements Criteria {

    private String field;
    private Operand operand;
    private String operandValue;

    BasicCriteria(String field, Operand operand, String operandValue) {
        this.field = field;
        this.operand = operand;
        this.operandValue = operandValue;
    }

    public String getOperandValue() {
        return operandValue;
    }

    public String getField() {
        return field;
    }

    public Operand getOperand() {
        return operand;
    }

    public boolean isMatching(String[] columnNames, String[] rowData) {

        int columnIndex = Arrays.asList(columnNames).indexOf(field);
        switch (operand) {
            case STARTS_WITH:
                if(rowData[columnIndex].startsWith(this.getOperandValue())){
                    return true;
                }
                break;
            case ENDS_WITH:
                if(rowData[columnIndex].endsWith(this.getOperandValue())){
                    return true;
                }
                break;
            case EQUALS:
                if(rowData[columnIndex].equals(this.getOperandValue())){
                    return true;
                }
                break;
            case CONTAINS:
                if(rowData[columnIndex].contains(this.getOperandValue())){
                    return true;
                }
                break;
            default:
                throw new IllegalArgumentException();
        }

        return false;
    }
}
