package spouts;


import enums.EMessagesFields;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;
import pojos.Event;

import java.util.Map;
import java.util.Random;
import java.util.UUID;


/**
 * spout to handle events
 * support only IPV4
 */
public class UserIdIPSpout extends BaseRichSpout {

    private SpoutOutputCollector collector;
    private final String IP_REGEX_PATTERN = "^((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.){3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)$";

    @Override
    public void open(Map<String, Object> conf, TopologyContext context, SpoutOutputCollector collector) {
        this.collector = collector;

    }

    @Override
    public void nextTuple() {
        //Random uuid for internal system usage
        UUID uuid = UUID.randomUUID();
        Event event = getEvent();
        //validate that the event has user_id and IP and that the IP is valid.
        validateEvent(event);
        collector.emit(new Values(uuid.toString(), event.getUser_id(), event.getIP()));
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields(EMessagesFields.UUID.getFieldName(),
                EMessagesFields.USER_ID.getFieldName(),
                EMessagesFields.IP.getFieldName()));
    }

    //get events from external source
    //in our case we generate random data
    private Event getEvent(){
        String[] ips = {"100.1.1.2", "0.0.0.1", "0.0.0.2", "0.0.0.3", "0.0.0.4"};
        String[] userIds = {"user1", "user2", "user3", "user4", "user5"};
        Random random = new Random();
        String userId = userIds[random.nextInt(userIds.length)];
        String ip = ips[random.nextInt(ips.length)];
        return new Event(userId, ip);
    }

    private void validateEvent(Event event) throws IllegalArgumentException{
        if(event.getUser_id() == null || event.getUser_id().isEmpty()){
            throw new IllegalArgumentException("user_id cannot be empty or null");
        }
        if(event.getIP() == null || event.getIP().isEmpty()){
            throw new IllegalArgumentException("IP cannot be empty or null");
        }
        if(!event.getIP().matches(IP_REGEX_PATTERN)){
            throw new IllegalArgumentException("IP is not valid");
        }

    }
}
