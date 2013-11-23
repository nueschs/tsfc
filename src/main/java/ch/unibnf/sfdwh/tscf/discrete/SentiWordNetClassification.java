package ch.unibnf.sfdwh.tscf.discrete;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import backtype.storm.tuple.Tuple;
import ch.unibnf.sfdwh.tscf.utils.TwitterHelper;

public class SentiWordNetClassification implements DiscreteClassification<Double>, Serializable{

	private static final long serialVersionUID = -6638321476022107160L;

	private static final String SWN_PATH ="src/main/resources/SentiWordNet_3.0.0_20130122.txt";

	private static final int WORD_POSITION = 4;

	protected final Map<String, Double> dict;

	public SentiWordNetClassification(Sentiment sentiment) throws IOException{
		this.dict = this.createDict(sentiment.getPosition());
	}

	@Override
	public Map<String, Double> getClassifications(Tuple input){
		Map<String, Double> result = new HashMap<>();


		// Get the text of the tweet
		// Do some preprocessing (split into words, remove special chars)
		Vector<String> words = TwitterHelper.getTextVector(input);

		for (String word : words) {
			if (this.dict.containsKey(word)){
				result.put(word, this.dict.get(word));
			}
		}

		return result;
	}

	@Override
	public Double getMaxValue() {
		return 1.0;
	}

	@Override
	public Double getMinValue() {
		return 0.0;
	}

	private Map<String, Double> createDict(int position) throws IOException {
		Map<String, Double> dict = new HashMap<>();
		BufferedReader csv =  new BufferedReader(new FileReader(SWN_PATH));
		String line = "";
		while((line = csv.readLine()) != null){
			// The file is tab separated
			String[] data = line.split("\t");
			// Get entry word
			String[] words = data[WORD_POSITION].split(" ");
			// There can be multiple words
			for(String w : words)
			{
				// Remove the '#'-sign from the words
				String[] w_n = w.split("#");
				if(!dict.containsKey(w_n[0])){
					// implementing class has to supply correct position
					dict.put(w_n[0], Double.parseDouble(data[position]));
				}
			}
		}
		csv.close();
		return dict;
	}
}
