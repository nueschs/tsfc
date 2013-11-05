package ch.unibnf.sfdwh.tscf.bolts.fuzzy;

import java.util.List;

import backtype.storm.tuple.Tuple;

public abstract class FuzzyModel implements FuzzyModelInterface {
	
	public abstract List<Float> getFuzzyValues(Tuple input);
	public abstract List<String> getFields();
	public abstract List<String> getLinguisticNames();
}
