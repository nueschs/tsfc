package ch.unibnf.sfdwh.tscf;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;
import ch.unibnf.sfdwh.tscf.bolts.CassandraBolt;
import ch.unibnf.sfdwh.tscf.bolts.SentiWordNetBolt;
import ch.unibnf.sfdwh.tscf.bolts.factory.SentiWordNetFactory;
import ch.unibnf.sfdwh.tscf.spouts.TwitterSpout;

public class MainTopology {
	private static LocalCluster cluster;

	/*------ STORM CONFIGURATION ---------*/
	private static final int maxSpoutPending = 1000;
	private static final int numWorkers = 2;
	private static final int numAckers = 1;
	private static final int messageTimeout = 30; //seconds

	private static final boolean localCluster = true;
	/* ----------------------------- */

	/*------ OAuth CONFIGURATION --------*/
	//You need to have a Twitter dev account: https://dev.twitter.com and create a new application : https://dev.twitter.com/apps/new
	private static String oauth_consumer_key = "GFhG7Bs4rQ02PalsVweYrg";
	private static String oauth_token = "2174400397-mwJA5C1R4Cuy1YRdxc5k29SGkFqLQHpPTl8L2pD";
	private static String oauth_consumer_secret = "Te42bkKmhF4sSANGrg2ZkiDhd7yUBxrfcJHt6PW2U";
	private static String oauth_access_token_secret = "iKrN9SdaUwYthx8PQiaDfKjNWlVUSLmcI0l2dg1mniQxI";
	/* ----------------------------- */

	/*------ TWITTER TRACK ------*/
	private static String track;
	/* ----------------------------- */


	public static void main( String[] args ) throws InterruptedException, AlreadyAliveException, InvalidTopologyException
	{
		if (args.length == 0){
			System.err.println("Must supply tweet topics to follow (comma separated, without spaces)");
			return;
		}

		track = args[0];

		TopologyBuilder builder = new TopologyBuilder();

		builder.setSpout("twitterStream", new TwitterSpout(oauth_consumer_key, oauth_token, oauth_consumer_secret,
				oauth_access_token_secret, track));

		SentiWordNetBolt<Double> bolt = SentiWordNetFactory.createPositiveSentiWordNetBolt();
		builder.setBolt("swnPositiveBolt", bolt).shuffleGrouping("twitterStream");
		builder.setBolt("cassandraBolt", new CassandraBolt(),1).
		shuffleGrouping("swnPositiveBolt");

		/*------ SETUP CONFIG --------*/
		Config conf = new Config();
		conf.setNumWorkers(numWorkers);
		conf.setMaxSpoutPending(maxSpoutPending);
		conf.setNumAckers(numAckers);
		conf.setMessageTimeoutSecs(messageTimeout);
		/*---------------------------*/

		if(localCluster){
			/*----- LOCAL CLUSTER -------*/
			cluster = new LocalCluster();
			conf.setDebug(false);
			cluster.submitTopology("TwitterStorm", conf, builder.createTopology());
			/*---------------------------*/
		}
		else {
			/*----- CLUSTER -------*/
			StormSubmitter.submitTopology("TwitterStorm", conf, builder.createTopology());
			/*---------------------*/
		}
	}

}