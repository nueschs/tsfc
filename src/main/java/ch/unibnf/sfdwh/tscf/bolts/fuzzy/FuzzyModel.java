package ch.unibnf.sfdwh.tscf.bolts.fuzzy;

import backtype.storm.tuple.Tuple;

public abstract class FuzzyModel implements FuzzyModelInterface {
	
	public abstract String getFuzzyValues(Tuple input);

}
