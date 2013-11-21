package ch.unibnf.sfdwh.tscf.fuzzy;

import com.google.common.base.Preconditions;

public class TrapezoidClassification<T extends Number> implements FuzzyClassification<T> {

	private static final String CLOSED_FUNCTION_MESSAGE = "null support values only allowed for functions open on the corresponding side";
	private static final String BONDARY_MESSAGE = "The function needs to have a boundary on at least one side";
	private final String literal;
	private final T lowerLimit, lowerSupportLimit, upperSupportLimit, upperLimit;
	private final boolean leftOpen;
	private final boolean rightOpen;
	private final boolean isClosed;

	public TrapezoidClassification(String literal, T lowerLimit, T lowerSupportLimit, T upperSupportLimit, T upperLimit) {

		/*
		 * Check if functions are defined correctly
		 */
		Preconditions.checkArgument(lowerLimit == null || lowerSupportLimit != null, CLOSED_FUNCTION_MESSAGE);
		Preconditions.checkArgument(upperLimit == null || upperSupportLimit != null, CLOSED_FUNCTION_MESSAGE);

		this.lowerLimit = lowerLimit;
		this.lowerSupportLimit = lowerSupportLimit;
		this.upperSupportLimit = upperSupportLimit;
		this.upperLimit = upperLimit;
		this.literal = literal;

		this.leftOpen = this.lowerLimit == null;
		this.rightOpen = this.upperLimit == null;

		/*
		 * The function cannot be open on both sides
		 */
		Preconditions.checkArgument(!(this.leftOpen && this.rightOpen), BONDARY_MESSAGE);

		this.isClosed = !this.rightOpen && !this.leftOpen;
	}

	@Override
	public Double getClassification(T value) {

		if (this.isOne(value)) {
			return 1.0;
		} else if (this.isRising(value)) {
			return this.getDifference(this.lowerLimit, this.lowerSupportLimit, value, false);
		} else if (this.isFalling(value)) {
			return this.getDifference(this.upperSupportLimit, this.upperLimit, value, true);
		} else {
			return 0.0;
		}
	}

	@Override
	public String getLiteral() {
		return this.literal;
	}

	private double getDifference(T lowValue, T highValue, T value, boolean isFalling) {
		double diff = highValue.doubleValue() - lowValue.doubleValue();
		double relValue = value.doubleValue() - lowValue.doubleValue();
		return isFalling ? 1.0 - (relValue / diff) : relValue / diff;
	}

	/*
	 * Value is in the falling part
	 */
	private boolean isFalling(T value) {
		return (this.isClosed || this.leftOpen)
				&& (this.upperSupportLimit.doubleValue() < value.doubleValue() && value.doubleValue() < this.upperLimit
						.doubleValue());
	}

	/*
	 * If the function has no boundary to the left, the value will be 1.0 if it
	 * is smaller than the upperSupportLimit (and vice versa for the right
	 * side). If the function is closed, 1.0 will be return for value between
	 * lower and upper support limit
	 */
	private boolean isOne(T value) {
		return (this.leftOpen && value.doubleValue() <= this.upperSupportLimit.doubleValue())
				|| (this.rightOpen && value.doubleValue() >= this.lowerSupportLimit.doubleValue())
				|| (this.isClosed && this.lowerSupportLimit.doubleValue() <= value.doubleValue() && value.doubleValue() <= this.upperSupportLimit
				.doubleValue());
	}

	/*
	 * Value is in the uprising part
	 */
	private boolean isRising(T value) {
		return (this.isClosed || this.rightOpen)
				&& (this.lowerLimit.doubleValue() < value.doubleValue() && value.doubleValue() < this.lowerSupportLimit
						.doubleValue());
	}

}
