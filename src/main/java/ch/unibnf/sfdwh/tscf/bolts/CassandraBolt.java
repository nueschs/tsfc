package ch.unibnf.sfdwh.tscf.bolts;

import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;
import ch.unibnf.sfdwh.tscf.CassandraConnector;
import ch.unibnf.sfdwh.tscf.fuzzy.FuzzyClassificationType;
import ch.unibnf.sfdwh.tscf.utils.TwitterHelper;

public class CassandraBolt extends BaseRichBolt {

	private final CassandraConnector cassandraConnector = CassandraConnector.INSTANCE;
	private OutputCollector collector;

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {

	}

	@Override
	public void execute(Tuple input) {
		FuzzyClassificationType type = (FuzzyClassificationType) this.getField(1, input);
		this.storeTuple(type, input);
		System.out.println(">>> Stored tweet: "+this.getField(0, input));
		this.collector.ack(input);

	}

	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
	}

	private Double getDouble(Object d) {
		return d == null ? null : (Double) d;
	}

	private Object getField(int position, Tuple input) {
		return input.get(input.getFields().get(position));
	}

	private Map<String, Double> getMap(Object m) {
		return m == null ? null : (Map<String, Double>) m;
	}

	private String getString(Object s) {
		return s == null ? null : (String) s;
	}

	private void storeTuple(FuzzyClassificationType type, Tuple input) {
		switch (type) {
		case TRAPEZOID:
			Object[] fields = { this.getField(0, input), this.getField(4, input), this.getField(5, input),
					this.getField(6, input), this.getField(7, input), this.getField(2, input), this.getField(3, input),
					this.getField(8, input) };
			String tweet = TwitterHelper.getProcessedString(this.getString(fields[0]));
			this.cassandraConnector.insertTrapezoidRow(tweet, this.getDouble(fields[1]),
					this.getDouble(fields[2]), this.getDouble(fields[3]), this.getDouble(fields[4]),
					this.getString(fields[5]), this.getDouble(fields[6]), this.getMap(fields[7]));
			break;
		}
	}

}
