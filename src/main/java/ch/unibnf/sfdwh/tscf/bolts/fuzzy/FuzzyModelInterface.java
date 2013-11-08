package ch.unibnf.sfdwh.tscf.bolts.fuzzy;

import java.util.List;

import backtype.storm.tuple.Tuple;

public interface FuzzyModelInterface {
	
	List<Float> getFuzzyValues(Tuple input);
	List<String> getLinguisticNames();
	List<String> getFields();

}
