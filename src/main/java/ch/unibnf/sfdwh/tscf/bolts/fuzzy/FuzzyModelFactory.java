package ch.unibnf.sfdwh.tscf.bolts.fuzzy;

/**
 * Fuzzy model factory to create a concrete fuzzy model
 * @author Steve Aschwanden (steve.aschwanden@students.unibe.ch)
 *
 */
public class FuzzyModelFactory {
	
	/**
	 * Fuzzy model factory
	 * @param name Name of the fuzzy model
	 * @return New instance of the corresponding fuzzy model
	 */
	public static FuzzyModel getFuzzyModel(String name){
		// Fuzzy model based on SentiWordNet approach
		if (name.equals("SentiWordNet"))
			return new SentiWordFuzzyModel();
		else
			return null;
	}
}
