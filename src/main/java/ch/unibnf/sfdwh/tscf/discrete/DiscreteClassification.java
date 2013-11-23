package ch.unibnf.sfdwh.tscf.discrete;

import java.util.Map;

import backtype.storm.tuple.Tuple;

public interface DiscreteClassification<T extends Number> {

	public Map<String, T> getClassifications(Tuple input);

	public T getMaxValue();

	public T getMinValue();

}
