package alertHandler;

// interface that every alert handler needs to implements
public interface IAlertHandler {
    void alert(String userId, String ip);
}
