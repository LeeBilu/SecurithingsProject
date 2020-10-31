package enums;

/**
 * enum for all the fields that can be sent in the system
 */
public enum EMessagesFields {
    UUID("uuid"),
    USER_ID("user_id"),
    IP("IP"),
    COUNTER("counter")
    ;
    private String fieldName;

    EMessagesFields(String fieldName){
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }

}
