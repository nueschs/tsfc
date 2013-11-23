package ch.unibnf.sfdwh.tscf.fuzzy;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface FuzzyClassification<T extends Number> {

	/**
	 * Used to declare output fields. All according values are accessible via
	 * interface methods.
	 */
	public static final String[] GLOBAL_FIELDS = { "tweet", "classificationType", "linguisticTerm", "overalAverage" };

	/**
	 * Get the overall average of values. Note that this function is not supposed to compute the fuzzy classifications first.
	 * @param values
	 * @return
	 */
	public Double getAverage(Collection<Double> values);

	/**
	 * Get fuzzy classification of a single value.
	 * 
	 * @param value
	 *            a numeral value to be fuzzily classified
	 * @return a {@link Double} value between 0 and 1, representing the
	 *         membership degree of value to the group defined by the linguistic
	 *         term of this classification.
	 */
	public Double getClassification(T value);

	/**
	 * @param discreteValues
	 * @return a {@link Map}, relating the keys from discreteValues to their respective fuzzy classification
	 */
	public Map<String, Double> getClassifications(Map<String, T> discreteValues);

	/**
	 * @return The list of fields which define this classification
	 */
	public List<String> getFields();

	/**
	 * Access the value of a field of this classification
	 * 
	 * @param fieldName
	 * @return
	 * @throws IllegalArgumentException if the classification does not have a field with name = fieldName
	 */
	public T getFieldValue(String fieldName) throws IllegalArgumentException;

	/**
	 * @return The linguistic name this classification can calculate the membership degree for.
	 */
	public String getLinguisticTerm();

	/**
	 * @return The type of this fuzzy classification
	 */
	public FuzzyClassificationType getType();

}
