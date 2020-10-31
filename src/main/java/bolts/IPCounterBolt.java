package bolts;

import enums.EMessagesFields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

/**
 * bolt that in charge of counting ips
 */
public class IPCounterBolt extends AbstractCounterBolt {


    @Override
    public void execute(Tuple input) {
        String ip = input.getStringByField(EMessagesFields.IP.getFieldName());
        if (ip != null) {
            Long ipCount = getAndAddCounter(ip);
            this.collector.emit(new Values(input.getStringByField(EMessagesFields.UUID.getFieldName()),
                    ipCount,
                    input.getStringByField(EMessagesFields.USER_ID.getFieldName()),
                    ip));
        }
    }
}
