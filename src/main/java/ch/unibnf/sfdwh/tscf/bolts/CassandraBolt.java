package ch.unibnf.sfdwh.tscf.bolts;

import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;
import ch.unibnf.sfdwh.tscf.CassandraConnector;

public class CassandraBolt extends BaseRichBolt {

	private final CassandraConnector cassandraConnector = CassandraConnector.INSTANCE;

	private OutputCollector outputCollector;

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub

	}

	@Override
	public void execute(Tuple input) {
		System.out.println(input);

	}

	@Override
	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
		this.outputCollector = collector;

	}


}
