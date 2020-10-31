package pojos;

/**
 *  pojo that represent event in the system
 */
public class Event {

    private String user_id;
    private String IP;

    public Event(String user_id, String IP) {
        this.user_id = user_id;
        this.IP = IP;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getIP() {
        return IP;
    }
}
