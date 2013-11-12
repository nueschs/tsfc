package ch.unibnf.sfdwh.tscf.bolts.fuzzy;

import java.util.List;

import backtype.storm.tuple.Tuple;

/**
 * Interface of a fuzzy model
 * @author Steve Aschwanden (steve.aschwanden@students.unibe.ch)
 *
 */
public interface FuzzyModelInterface {
	
	/**
	 * Get the fuzzy values of the defined linguistic variables
	 * @param input A tweet with all information from the twitter spout
	 * @return All calculated fuzzy values
	 */
	List<Float> getFuzzyValues(Tuple input);
	
	/**
	 * Get the linguistic variable names
	 * @return All used linguistic variable names
	 */
	List<String> getLinguisticNames();
	
	/**
	 * Get all output fields for the corresponding bolt
	 * @return All output fields of the bolt
	 */
	List<String> getFields();

}
