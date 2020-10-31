package bolts;

import enums.EMessagesFields;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;

import java.util.HashMap;
import java.util.Map;

//abstract class for bolts that count userid/ip
public abstract class AbstractCounterBolt extends BaseRichBolt {

    protected OutputCollector collector;

    //map between userid/ip to the amount they appear
    private Map<String, Long> CounterMap;

    @Override
    public void prepare(Map<String, Object> topoConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
        CounterMap = new HashMap<String, Long>();
    }

    //thread sage method to get and increase the amount of appearance of userid/ip
    protected long getAndAddCounter(String key) {
        Long count;
        synchronized (this) {
            count = CounterMap.get(key);
            if (count == null) {
                count = 0L;
            }
            count += 1;
            CounterMap.put(key, count);
        }

        return count;
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields(EMessagesFields.UUID.getFieldName(),
                EMessagesFields.COUNTER.getFieldName(),
                EMessagesFields.USER_ID.getFieldName(),
                EMessagesFields.IP.getFieldName()));
    }
}
