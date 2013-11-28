package ch.unibnf.sfdwh.tscf.spouts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.UUID;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import ch.unibnf.sfdwh.tscf.utils.OAuthConnection;

public class TwitterSpout extends BaseRichSpout{

	static String STREAMING_API_URL = "https://stream.twitter.com/1.1/statuses/filter.json";

	private DefaultHttpClient client;
	private BufferedReader reader;
	private String in;

	private SpoutOutputCollector _collector;
	private final String _cKey, _token, _cSecret, _acessTokenSecret, _track;

	public TwitterSpout(String cKey, String token, String cSecret, String accessTokenSecret, String track) {
		this._cKey = cKey;
		this._token = token;
		this._cSecret = cSecret;
		this._acessTokenSecret = accessTokenSecret;
		this._track = track;
	}

	@Override
	public void ack(Object id) {
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("str"));
	}

	@Override
	public void fail(Object id) {
	}

	@Override
	public void nextTuple(){
		try {
			this.in = this.reader.readLine();
			if(this.in!=null && !this.in.isEmpty()){
				UUID id = UUID.randomUUID();
				this._collector.emit(new Values(this.in),id);
			}
			else {
				Thread.sleep(10);
			}
		} catch (Exception e){
			System.err.println("Error reading line "+e.getMessage());
		}

	}

	@Override
	public void open(Map conf, TopologyContext context, SpoutOutputCollector collector){
		this.client = new DefaultHttpClient();
		OAuthConnection connection = new OAuthConnection(STREAMING_API_URL, this._track, this._cKey, this._token, this._cSecret, this._acessTokenSecret);
		HttpPost httpPost = connection.GetConnection();
		HttpResponse response;
		try {
			response = this.client.execute(httpPost);
			StatusLine status = response.getStatusLine();
			if(status.getStatusCode() == 200){
				InputStream inputStream = response.getEntity().getContent();
				this.reader = new BufferedReader(new InputStreamReader(inputStream));
			}
			else System.out.println("Http error :"+status.getStatusCode());
		} catch (IOException e) {
			System.err.println("Error in communication with twitter api ["+httpPost.getURI().toString()+"]");
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e1) {
			}
		}
		this._collector = collector;

	}

}
