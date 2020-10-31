package bolts;

import configuration.ConfigurationLoader;
import enums.EMessagesFields;
import enums.EPropertiesFields;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.util.HashMap;
import java.util.Map;

//Bolt to sum all the counts from the system.
//This bolt cannot run on several instances and cannot be run multithreaded.
public class SummerBolt extends BaseRichBolt {
    private OutputCollector collector;

    //map from uuid to the count of userid/ip
    private Map<String, Long> uuidToCountMap;

    private long maxThreshold;
    //default value for threshold for events
    private static final long MAX_THRESHOLD_DEFAULT = 10;

    @Override
    public void prepare(Map<String, Object> topoConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
        uuidToCountMap = new HashMap<String, Long>();
        //load the threshold from the properties file
        ConfigurationLoader configurationLoader = ConfigurationLoader.getInstance();
        String maxThresholdProperties = configurationLoader.getProperty(EPropertiesFields.MAX_THRESHOLD);
        if (maxThresholdProperties == null) {
            this.maxThreshold = MAX_THRESHOLD_DEFAULT;
        } else {
            try {
                this.maxThreshold = Long.parseLong(maxThresholdProperties);
            } catch (NumberFormatException e) {
                this.maxThreshold = MAX_THRESHOLD_DEFAULT;
            }
        }
    }

    @Override
    public void execute(Tuple input) {
        String uuid = input.getStringByField(EMessagesFields.UUID.getFieldName());
        Long counterFromCounterBolt = input.getLongByField(EMessagesFields.COUNTER.getFieldName());
        Long count = uuidToCountMap.get(uuid);
        if (count == null) {
            //if null than this is the first time we see the uuid, just need to save it
            uuidToCountMap.put(uuid, counterFromCounterBolt);
        } else {
            //if not null than this is not the first time we see the uuid.
            //check that the total counts doesnt exceed threshold, if so we send to alert bolt.
            long sum = count + counterFromCounterBolt;
            if (sum >= maxThreshold) {
                collector.emit(new Values(input.getStringByField(EMessagesFields.USER_ID.getFieldName()),
                        input.getStringByField(EMessagesFields.IP.getFieldName()),
                        sum));
            }
            uuidToCountMap.remove(uuid);
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields(EMessagesFields.USER_ID.getFieldName(),
                EMessagesFields.IP.getFieldName(),
                EMessagesFields.COUNTER.getFieldName()));
    }
}
