package enums;

/**
 * enum to access all properties from property file.
 * can also be used to set other configuration like amounts of bolt etc.
 */
public enum EPropertiesFields {
    MAX_THRESHOLD("max_threshold")
    ;
    private String fieldName;

    EPropertiesFields(String filedName) {
        this.fieldName = filedName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
