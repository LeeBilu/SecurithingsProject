package alertHandler;

public class AlertHandlerManager {
    private static IAlertHandler alertHandler;

    public static IAlertHandler getInstance(){
        if(alertHandler == null){
            synchronized (AlertHandlerManager.class){
                if(alertHandler == null){
                    //in this case we use log4j alert handler, can use factory to create several different handlers base on usage
                    alertHandler = new Log4jAlertHandler();
                }
            }
        }
        return alertHandler;
    }
}
