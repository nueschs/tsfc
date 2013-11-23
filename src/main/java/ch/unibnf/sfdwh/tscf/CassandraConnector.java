package ch.unibnf.sfdwh.tscf;

import java.util.Map;

import org.json.simple.JSONValue;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

public enum CassandraConnector {

	INSTANCE;

	public static final String SERVER_IP = "127.0.0.1";
	public static final String KEYSPACE = "tsfc";
	private static final String TRAPEZOID_TABLE = "trapezoidTable";

	private Cluster cluster;

	private Session session;

	private CassandraConnector() {
		this.cluster = Cluster.builder().addContactPoint(SERVER_IP).build();
		this.session = this.cluster.connect(KEYSPACE);
	}

	public void insertTrapezoidRow(String tweet, Double lowerLimit, Double lowerSupportLimit, Double upperSupportLimit,
			Double upperLimit, String literal, Double overall, Map<String, Double> singleValues) {

		String values = JSONValue.toJSONString(singleValues);
		values = values.replace("\"", "'");

		if (Double.isNaN(overall)){
			overall = null;
		}

		String cql = "INSERT INTO "
				+ KEYSPACE
				+ "."
				+ TRAPEZOID_TABLE
				+ " (id, lowerLimit, lowerSupportLimit, upperSupportLimit, upperLimit, literal, overall, singleValues) VALUES ('"
				+ tweet + "', " + lowerLimit + ", " + lowerSupportLimit + ", " + upperSupportLimit + ", " + upperLimit
				+ ", '" + literal + "', " + overall + ", " + values + ");";
		try {
			this.session.execute(cql);
		} catch (Exception e){
			System.err.println(cql);
		}

	}
}
