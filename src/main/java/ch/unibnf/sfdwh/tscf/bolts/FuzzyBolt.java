package ch.unibnf.sfdwh.tscf.bolts;

import java.util.Map;

import ch.unibnf.sfdwh.tscf.bolts.fuzzy.FuzzyModel;
import ch.unibnf.sfdwh.tscf.bolts.fuzzy.FuzzyModelFactory;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;

public class FuzzyBolt extends BaseBasicBolt {
	
	private String fuzzyModelName = null;
	private FuzzyModel fuzzyModel = null;
	
	public String getFuzzyModelName() {
		return fuzzyModelName;
	}

	public void setFuzzyModelName(String name) {
		this.fuzzyModelName = name;
		fuzzyModel = FuzzyModelFactory.getFuzzyModel(this.fuzzyModelName);
	}

	public FuzzyModel getFuzzyModel() {
		return fuzzyModel;
	}

	@Override
	public void prepare(Map stormConf, TopologyContext context){
		fuzzyModel = FuzzyModelFactory.getFuzzyModel(this.fuzzyModelName);
		
	}	

	@Override
	public void execute(Tuple input, BasicOutputCollector collector) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields(""));
		
	}

}
