/**
 * Created by srujant on 23/6/16.
 */
public enum ObjectType {
    INTEGER("int") {
        @Override
        public Object convertToTypeValue(String value) {
            return Integer.valueOf(value);
        }
    },
    BOOLEAN("boolean") {
        @Override
        public Object convertToTypeValue(String value) {
            return Boolean.valueOf(value);
        }
    },
    CHAR("char"){
        @Override
        public Object convertToTypeValue(String value) {
            return Character.valueOf(value.toCharArray()[0]);
        }
    },
    LONG("long"){
        @Override
        public Object convertToTypeValue(String value) {
            return Long.valueOf(value);
        }
    };

    private String primitiveType;

    ObjectType(String primitiveType) {
        this.primitiveType = primitiveType;
    }

    public static Object getValueByType(String type, String value) {
        for(ObjectType objectType : ObjectType.values()) {
            if(type.equals(objectType.primitiveType)) {
                return objectType.convertToTypeValue(value);
            }
        }
        return value;
    }

    public abstract Object convertToTypeValue(String value);
}
