package ch.unibnf.sfdwh.tscf.bolts;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ch.unibnf.sfdwh.tscf.bolts.fuzzy.FuzzyModel;
import ch.unibnf.sfdwh.tscf.bolts.fuzzy.FuzzyModelFactory;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class FuzzyBolt implements IRichBolt {
	
	private String fuzzyModelName = null;
	private FuzzyModel fuzzyModel = null;
	
	private OutputCollector collector;
	
	public String getFuzzyModelName() {
		return fuzzyModelName;
	}

	public void setFuzzyModelName(String name) {
		this.fuzzyModelName = name;
		if(this.fuzzyModel == null)
			this.fuzzyModel = FuzzyModelFactory.getFuzzyModel(this.fuzzyModelName);
	}

	public FuzzyModel getFuzzyModel() {
		return fuzzyModel;
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		if(this.fuzzyModel != null)
			declarer.declare(new Fields(this.fuzzyModel.getFields()));
		
	}

	@Override
	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
		if(this.fuzzyModel == null)
			this.fuzzyModel = FuzzyModelFactory.getFuzzyModel(this.fuzzyModelName);
		this.collector = collector;
		
	}

	@Override
	public void execute(Tuple input) {
		if(this.fuzzyModelName != null){
			if(this.fuzzyModel.getFuzzyValues(input).size() == this.fuzzyModel.getLinguisticNames().size()){
				List<Object> list = new ArrayList();
				list.add(input.getValue(0));
				for(int i=0; i<this.fuzzyModel.getFuzzyValues(input).size(); i++){
					list.add(this.fuzzyModel.getLinguisticNames().get(i));
					list.add(this.fuzzyModel.getFuzzyValues(input).get(i).toString());
				}
				this.collector.emit(input, list);
			}
		}
			
		
		
	}

	@Override
	public void cleanup() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

}
