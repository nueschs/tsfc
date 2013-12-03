package ch.unibnf.sfdwh.tscf.bolts;

import java.io.IOException;

import ch.unibnf.sfdwh.tscf.discrete.DiscreteClassification;
import ch.unibnf.sfdwh.tscf.discrete.SentiWordNetClassification;
import ch.unibnf.sfdwh.tscf.discrete.Sentiment;
import ch.unibnf.sfdwh.tscf.fuzzy.FuzzyClassification;
import ch.unibnf.sfdwh.tscf.fuzzy.TrapezoidClassification;

public class SentiWordNetFactory {

	public static SentiWordNetBolt<Double> createPositiveSentiWordNetBolt(){
		DiscreteClassification<Double> discrete;
		try {
			discrete = new SentiWordNetClassification(Sentiment.POSITIVE);
			FuzzyClassification<Double> fuzzy = new TrapezoidClassification<Double>(Sentiment.POSITIVE.getTerm(), 0.0, 2.0/3.0, null, null);
			SentiWordNetBolt<Double> bolt = new SentiWordNetBolt<>(fuzzy, discrete);
			return bolt;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}

}
