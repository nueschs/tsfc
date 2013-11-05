package ch.unibnf.sfdwh.tscf.bolts.fuzzy;

public class FuzzyModelFactory {
	public static FuzzyModel getFuzzyModel(String name){
		if (name.equals("SentiWordNet"))
			return new SentiWordFuzzyModel();
		else
			return null;
	}
}
