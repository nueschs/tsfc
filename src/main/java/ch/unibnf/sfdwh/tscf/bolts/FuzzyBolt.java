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

/**
 * Bolt for the fuzzification of the tweet text
 * @author Steve Aschwanden (steve.aschwanden@students.unibe.ch)
 *
 */
public class FuzzyBolt implements IRichBolt {
	
	// Name of the fuzzy model
	private String fuzzyModelName = null;
	// The fuzzy model itself
	private FuzzyModel fuzzyModel = null;
	
	// Collector of the output fields
	private OutputCollector collector;
	
	/**
	 * Get the name of the used fuzzy model
	 * @return Name of fuzzy model
	 */
	public String getFuzzyModelName() {
		return fuzzyModelName;
	}

	/**
	 * Set the name of the fuzzy model and create a new instance of a fuzzy model object
	 * @param name
	 */
	public void setFuzzyModelName(String name) {
		// Set the name of the fuzzy model
		this.fuzzyModelName = name;
		// Create a concrete fuzzy model
		if(this.fuzzyModel == null)
			this.fuzzyModel = FuzzyModelFactory.getFuzzyModel(this.fuzzyModelName);
	}

	/**
	 * Get the fuzzy model object
	 * @return Fuzzy model object
	 */
	public FuzzyModel getFuzzyModel() {
		return fuzzyModel;
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// If there is a fuzzy model ...
		if(this.fuzzyModel != null)
			// ... declare the output fields of this bolt
			declarer.declare(new Fields(this.fuzzyModel.getFields()));
		
	}

	@Override
	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
		// If there is no fuzzy model ...
		if(this.fuzzyModel == null)
			// ... create it by the factory
			this.fuzzyModel = FuzzyModelFactory.getFuzzyModel(this.fuzzyModelName);
		this.collector = collector;
		
	}

	@Override
	public void execute(Tuple input) {
		// If there is a fuzzy model
		if(this.fuzzyModelName != null){
			// Assure that the number of linguistic variables and the corresponding values are the same 
			if(this.fuzzyModel.getFuzzyValues(input).size() == this.fuzzyModel.getLinguisticNames().size()){
				List<Object> list = new ArrayList();
				// Add the raw tweet at the first position
				list.add(input.getValue(0));
				// For every linguistic variable
				for(int i=0; i<this.fuzzyModel.getFuzzyValues(input).size(); i++){
					// Add the name ...
					list.add(this.fuzzyModel.getLinguisticNames().get(i));
					// ... and the corresponding value
					list.add(this.fuzzyModel.getFuzzyValues(input).get(i).toString());
				}
				// Send it to the output of the bolt
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
