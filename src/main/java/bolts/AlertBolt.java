package bolts;

import alertHandler.AlertHandlerManager;
import alertHandler.IAlertHandler;
import enums.EMessagesFields;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;

import java.util.Map;

/**
 * bolt that in charge to alert the system for dangerous event
 */
public class AlertBolt extends BaseRichBolt {

    private IAlertHandler alertHandler;

    @Override
    public void prepare(Map<String, Object> topoConf, TopologyContext context, OutputCollector collector) {
        alertHandler = AlertHandlerManager.getInstance();
    }

    @Override
    public void execute(Tuple input) {
        String userId = input.getStringByField(EMessagesFields.USER_ID.getFieldName());
        String ip = input.getStringByField(EMessagesFields.IP.getFieldName());
        alertHandler.alert(userId, ip);
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
    }
}
