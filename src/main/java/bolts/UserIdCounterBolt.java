package bolts;

import enums.EMessagesFields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

/**
 * bolt that in charge of counting userids
 */
public class UserIdCounterBolt extends AbstractCounterBolt {

    @Override
    public void execute(Tuple input) {
        String userId = input.getStringByField(EMessagesFields.USER_ID.getFieldName());
        if (userId != null) {
            Long userIdCount = getAndAddCounter(userId);
            this.collector.emit(new Values(input.getStringByField(EMessagesFields.UUID.getFieldName()),
                    userIdCount,
                    userId,
                    input.getStringByField(EMessagesFields.IP.getFieldName())));
        }

    }

}
