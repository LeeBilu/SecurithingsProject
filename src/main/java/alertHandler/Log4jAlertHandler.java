package alertHandler;


import org.apache.log4j.Logger;

public class Log4jAlertHandler implements IAlertHandler{
    private static final Logger logger = Logger.getLogger(Log4jAlertHandler.class);
    public void alert(String userId, String ip) {
       logger.info("\n\n******************** danger ********************\n\n" +
                "user: " + userId + " from ip:" + ip + " has exceed the threshold\n\n" +
                "******************** danger ********************\n\n");
    }
}
