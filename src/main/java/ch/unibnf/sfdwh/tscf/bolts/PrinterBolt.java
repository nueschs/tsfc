package ch.unibnf.sfdwh.tscf.bolts;

import java.util.Map;

import backtype.storm.task.TopologyContext;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Tuple;

public class PrinterBolt extends BaseBasicBolt {
	
	@Override
	public void cleanup() {
		
	}
	
	@Override
	public void prepare(Map stormConf, TopologyContext context){
		
	}

	@Override
	public void execute(Tuple input, BasicOutputCollector collector) {
		for(int i=0; i<input.size(); i++){
			System.out.println(input.getValue(i));	
		}
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		
	}

}
