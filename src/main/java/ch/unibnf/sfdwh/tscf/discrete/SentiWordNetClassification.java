package ch.unibnf.sfdwh.tscf.discrete;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import backtype.storm.tuple.Tuple;

public class SentiWordNetClassification implements DiscreteClasification<Double> {

	private static final String SWN_PATH ="src/main/resources/SentiWordNet_3.0.0_20130122.txt";

	private static final int WORD_POSITION = 4;

	protected final Map<String, Double> dict;

	public SentiWordNetClassification(Sentiment sentiment) throws IOException{
		this.dict = this.createDict(sentiment.getPosition());
	}

	public Map<String, Double> getClassifications(Tuple input){
		Map<String, Double> result = new HashMap<>();

		// Get the text of the tweet
		String text = this.getText(input);

		// Do some preprocessing (split into words, remove special chars)
		Vector<String> words = this.doPreprocessing(text);

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
				w = w.replace("#", "");
				if(!dict.containsKey(w)){
					// implementing class has to supply correct position
					dict.put(w, Double.parseDouble(data[position]));
				}
			}
		}
		csv.close();

		return dict;
	}

	/**
	 * Do some tweet text preprocessing
	 * @param text The tweet text
	 * @return The text of the tweet without special chars
	 */
	private Vector<String> doPreprocessing(String text){
		String[] words = text.split(" ");
		Vector<String> onlyWords = new Vector<String>();
		for(String w:words){
			onlyWords.add(w.replaceAll("[^a-zA-Z0-9]+", "").toLowerCase());
		}
		return onlyWords;
	}

	/**
	 * Get the text out of the output of the twitter spout
	 * @param input Output of the twitter spout
	 * @return The raw tweet with all the information (text, date, username)
	 */
	private String getText(Tuple input){
		// Create a JSON parser
		JSONParser parser = new JSONParser();
		// Save the text of the tweet
		String text = "";

		try {
			// Create a JSON object
			Object obj = parser.parse(input.getValue(0).toString());
			JSONObject json = (JSONObject) obj;
			// Get the text
			text = json.get("text").toString();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return text;
	}
}
