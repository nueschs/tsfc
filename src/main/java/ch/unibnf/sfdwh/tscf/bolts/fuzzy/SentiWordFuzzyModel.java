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

public class SentiWordFuzzyModel extends FuzzyModel implements Serializable {
	
	private String pathToSWN = "src/main/java/ch/unibnf/sfdwh/tscf/bolts/fuzzy/SentiWordNet_3.0.0_20130122.txt";
	private HashMap<String, Vector<Double>> _dict = null;

	@Override
	public List<Float> getFuzzyValues(Tuple input) {
		
		String text = this.getText(input);
		if(!this.hasDict()){
			this.createDict();
		}
		Vector<String> words = this.doPreprocessing(text);
		
		float[] values = {0.0f, 0.0f, 0.0f};
		int cnt = 0;
		for(String w:words){
			if(_dict.containsKey(w))
			{
				Vector<Double> v = _dict.get(w);
				values[0] += v.get(0);
				values[1] += v.get(1);
				values[2] += v.get(2);
				cnt++;
			}
		}
		
		if(cnt != 0){
			values[0] /= cnt;
			values[1] /= cnt;
			values[2] /= cnt;
		}
		
		List<Float> list = new ArrayList();
		list.add(values[0]);
		list.add(values[1]);
		list.add(values[2]);
		return list;
	}

	@Override
	public List<String> getFields() {
		List<String> list = new ArrayList();
		list.add("tweet");
		list.add("positive");
		list.add("positive_value");
		list.add("negative");
		list.add("negative_value");
		list.add("neutral");
		list.add("neutral_value");
		return list;
	}

	@Override
	public List<String> getLinguisticNames() {
		List<String> list = new ArrayList();
		list.add("positive");
		list.add("negative");
		list.add("neutral");
		return list;
	}
	
	private String getText(Tuple input){
		JSONParser parser = new JSONParser();
		String text = "";
		
		try {
			Object obj = parser.parse(input.getValue(0).toString());
			JSONObject json = (JSONObject) obj;
			text = json.get("text").toString();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return text;
	}
	
	private void createDict(){
		if(this._dict == null){
			_dict = new HashMap<String, Vector<Double>>();
			try{
				BufferedReader csv =  new BufferedReader(new FileReader(pathToSWN));
				String line = "";			
				while((line = csv.readLine()) != null)
				{
					String[] data = line.split("\t");
					String[] words = data[4].split(" ");
					for(String w:words)
					{
						String[] w_n = w.split("#");
						if(!_dict.containsKey(w_n[0]))
						{
							Vector<Double> v = new Vector<Double>();
							Double pos = Double.parseDouble(data[2]);
							Double neg = Double.parseDouble(data[3]);
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
	
	private boolean hasDict(){
		if(this._dict != null)
			return true;
		else
			return false;
	}
	
	private Vector<String> doPreprocessing(String text){
		String[] words = text.split(" ");
		Vector<String> onlyWords = new Vector<String>();
		for(String w:words){
			onlyWords.add(w.replaceAll("[^a-zA-Z0-9]+", ""));
		}
		return onlyWords;
	}
}
