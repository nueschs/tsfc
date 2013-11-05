package ch.unibnf.sfdwh.tscf.bolts.fuzzy;

import backtype.storm.tuple.Tuple;

public interface FuzzyModelInterface {
	
	String getFuzzyValues(Tuple input);

}
