package ch.unibnf.sfdwh.tscf.bolts.fuzzy;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import backtype.storm.tuple.Tuple;

/**
 * A concrete fuzzy model with the SentiWordNet implementation (http://sentiwordnet.isti.cnr.it)
 * @author Steve Aschwanden (steve.aschwanden@students.unibe.ch)
 *
 */
public class SentiWordFuzzyModel extends FuzzyModel implements Serializable {
	
	// Path to the SentiWordNet file
	private String pathToSWN = "src/main/java/ch/unibnf/sfdwh/tscf/bolts/fuzzy/SentiWordNet_3.0.0_20130122.txt";
	// The dictionary of all words in the SentiWordNet file with
	// the corresponding values
	private HashMap<String, Vector<Double>> _dict = null;

	@Override
	public List<Float> getFuzzyValues(Tuple input) {
		// Get the text of the tweet
		String text = this.getText(input);
		// Check if the dictionary is available
		if(!this.hasDict()){
			// If not, create it
			this.createDict();
		}
		// Do some preprocessing (split into words, remove special chars)
		Vector<String> words = this.doPreprocessing(text);
		
		float[] values = {0.0f, 0.0f, 0.0f};
		int cnt = 0;
		// Go through all words in the tweet
		for(String w:words){
			// If the word is in the dictionary
			if(_dict.containsKey(w))
			{
				// Add the corresponding values to the vector
				Vector<Double> v = _dict.get(w);
				values[0] += v.get(0);
				values[1] += v.get(1);
				values[2] += v.get(2);
				// Count the number of accumulations to average the score later
				cnt++;
			}
		}
		
		// Calculate the average of the score over all words in the tweet
		if(cnt != 0){
			values[0] /= cnt;
			values[1] /= cnt;
			values[2] /= cnt;
		}
		
		// Create the list with the final values and return it
		List<Float> list = new ArrayList();
		list.add(values[0]);
		list.add(values[1]);
		list.add(values[2]);
		return list;
	}

	@Override
	public List<String> getFields() {
		// Create a new list for all the output fields
		List<String> list = new ArrayList();
		// The whole tweet with all the information (text, date, username)
		list.add("tweet");
		// Name of the first linguistic variable
		list.add("positive");
		// Fuzzy value of first the linguistic variable
		list.add("positive_value");
		// Name of the second linguistic variable
		list.add("negative");
		// Fuzzy value of the second linguistic variable
		list.add("negative_value");
		// Name of the third linguistic variable
		list.add("neutral");
		// Fuzzy value of the third linguistic variable		
		list.add("neutral_value");
		return list;
	}

	@Override
	public List<String> getLinguisticNames() {
		// List all names of the linguistic variables
		List<String> list = new ArrayList();
		list.add("positive");
		list.add("negative");
		list.add("neutral");
		return list;
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
	
	/**
	 * Create the dictionary out of the SentiWordNet source file
	 */
	private void createDict(){
		// Create the dictionary only if necessary
		if(!this.hasDict()){
			_dict = new HashMap<String, Vector<Double>>();
			try{
				// Read the textfile ...
				BufferedReader csv =  new BufferedReader(new FileReader(pathToSWN));
				String line = "";		
				// ... line by line
				while((line = csv.readLine()) != null)
				{
					// The file is tab separated
					String[] data = line.split("\t");
					// Take the fifth column
					String[] words = data[4].split(" ");
					// There can be multiple words
					for(String w:words)
					{
						// Remove the '#'-sign from the words
						String[] w_n = w.split("#");
						// If the word is not in the dictionary ...
						if(!_dict.containsKey(w_n[0]))
						{
							// ... add it!
							Vector<Double> v = new Vector<Double>();
							// Get the positive value
							Double pos = Double.parseDouble(data[2]);
							// Get the negative value
							Double neg = Double.parseDouble(data[3]);
							// Calculate the neutral value
							Double neutral = 1 - (pos+neg);
							v.add(pos);
							v.add(neg);
							v.add(neutral);
							_dict.put(w_n[0], v);
						}
					}
				}
			}
			catch(Exception e){e.printStackTrace();}
		}
	}
	
	/**
	 * Is the dictionary created already
	 * @return The availability of the dictionary
	 */
	private boolean hasDict(){
		if(this._dict != null)
			return true;
		else
			return false;
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
}
