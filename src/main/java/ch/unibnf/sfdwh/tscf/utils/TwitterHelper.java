package ch.unibnf.sfdwh.tscf.utils;

import java.util.Vector;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import backtype.storm.tuple.Tuple;

public class TwitterHelper {

	public static String getProcessedString(String tweet){
		Vector<String> words = doPreprocessing(tweet);
		return vectorToString(words);

	}

	/**
	 * Get the text out of the output of the twitter spout
	 * @param input Output of the twitter spout
	 * @return The raw tweet with all the information (text, date, username)
	 */
	public static String getText(Tuple input){
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

	public static Vector<String> getTextVector(Tuple input){
		return doPreprocessing(getText(input));
	}

	/**
	 * Do some tweet text preprocessing
	 * @param text The tweet text
	 * @return The text of the tweet without special chars
	 */
	private static Vector<String> doPreprocessing(String text){
		String[] words = text.split(" ");
		Vector<String> onlyWords = new Vector<String>();
		for(String w:words){
			onlyWords.add(w.replaceAll("[^a-zA-Z0-9]+", "").toLowerCase());
		}
		return onlyWords;
	}

	private static String vectorToString(Vector<String> words) {
		String res = "";
		for (String word : words) {
			res += word+" ";
		}
		return res.substring(0,res.length()-1);
	}

}
