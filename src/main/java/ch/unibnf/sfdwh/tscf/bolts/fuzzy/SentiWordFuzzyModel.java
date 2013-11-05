package ch.unibnf.sfdwh.tscf.bolts.fuzzy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import backtype.storm.tuple.Tuple;

public class SentiWordFuzzyModel extends FuzzyModel implements Serializable {

	@Override
	public List<Float> getFuzzyValues(Tuple input) {
		List<Float> list = new ArrayList();
		list.add(1.0f);
		list.add(2.0f);
		list.add(3.0f);
		return list;
	}

	@Override
	public List<String> getFields() {
		List<String> list = new ArrayList();
		list.add("tweet");
		list.add("positive");
		list.add("positive_value");
		list.add("neutral");
		list.add("neutral_value");
		list.add("negative");
		list.add("negative_value");
		return list;
	}

	@Override
	public List<String> getLinguisticNames() {
		List<String> list = new ArrayList();
		list.add("positive");
		list.add("neutral");
		list.add("negative");
		return list;
	}
}
