import bolts.AlertBolt;
import bolts.IPCounterBolt;
import bolts.SummerBolt;
import bolts.UserIdCounterBolt;
import enums.EMessagesFields;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;
import spouts.UserIdIPSpout;


public class Example {


    public static void main(String[] args) {
        initExampleTopology();
    }

    private static void initExampleTopology(){
        try {

        TopologyBuilder topologyBuilder = new TopologyBuilder();

            topologyBuilder.setSpout("UserIdIPSpout", new UserIdIPSpout(), 1);

            topologyBuilder.setBolt("IPCounterBolt", new IPCounterBolt(), 1).
                    fieldsGrouping("UserIdIPSpout", new Fields(EMessagesFields.IP.getFieldName()));

            topologyBuilder.setBolt("UserIdCounterBolt", new UserIdCounterBolt(), 1).
                    fieldsGrouping("UserIdIPSpout", new Fields(EMessagesFields.USER_ID.getFieldName()));

            topologyBuilder.setBolt("SummerBolt", new SummerBolt(), 1).
                    shuffleGrouping("UserIdCounterBolt").
                    shuffleGrouping("IPCounterBolt");

            topologyBuilder.setBolt("AlertBolt", new AlertBolt(), 1).shuffleGrouping("SummerBolt");

            Config config = new Config();
            config.setNumWorkers(1);
            LocalCluster localCluster = new LocalCluster();
            localCluster.submitTopology("Example", config, topologyBuilder.createTopology());
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
