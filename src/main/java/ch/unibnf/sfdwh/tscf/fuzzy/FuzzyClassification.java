package ch.unibnf.sfdwh.tscf.fuzzy;


public interface FuzzyClassification<T extends Number> {

	public Double getClassification(T value);

	public String getLiteral();

}
