package ch.unibnf.sfdwh.tscf.bolts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import ch.unibnf.sfdwh.tscf.discrete.DiscreteClassification;
import ch.unibnf.sfdwh.tscf.fuzzy.FuzzyClassification;
import ch.unibnf.sfdwh.tscf.utils.TwitterHelper;

public class SentiWordNetBolt<T extends Number> extends BaseRichBolt {

	private static final long serialVersionUID = 6985821895661986468L;

	private static final String SINGLE_VALUES = "singleValues";

	private final DiscreteClassification<T> discreteClassification;

	private final FuzzyClassification<T> fuzzyClassification;

	private OutputCollector collector;

	public SentiWordNetBolt(FuzzyClassification<T> fuzzyClassification, DiscreteClassification<T> discreteClassification) {
		this.discreteClassification = discreteClassification;
		this.fuzzyClassification = fuzzyClassification;
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		List<String> fields = new ArrayList<>(Arrays.asList(FuzzyClassification.GLOBAL_FIELDS));
		fields.addAll(this.fuzzyClassification.getFields());
		fields.add(SINGLE_VALUES);
		declarer.declare(new Fields(fields));
	}

	@Override
	public void execute(Tuple input) {
		this.collector.ack(input);
		Map<String, T> discreteValues = this.discreteClassification.getClassifications(input);
		Map<String, Double> fuzzyValues = this.fuzzyClassification.getClassifications(discreteValues);
		List<Object> output = new ArrayList<>();
		output.add(TwitterHelper.getText(input));
		output.add(this.fuzzyClassification.getType());
		output.add(this.fuzzyClassification.getLinguisticTerm());
		output.add(this.fuzzyClassification.getAverage(fuzzyValues.values()));
		for (String name : this.fuzzyClassification.getFields()) {
			output.add(this.fuzzyClassification.getFieldValue(name));
		}
		output.add(fuzzyValues);
		this.collector.emit(input, output);
	}

	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
		this.collector = collector;
	}

}
