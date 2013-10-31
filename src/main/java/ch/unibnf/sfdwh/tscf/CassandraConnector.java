package ch.unibnf.sfdwh.tscf;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

public enum CassandraConnector {

	INSTANCE;

	public static final String SERVER_IP = "127.0.0.1";
	public static final String KEYSPACE = "tsfc";

	private Cluster cluster;
	private Session session;

	private CassandraConnector(){
		this.cluster = Cluster.builder().addContactPoint(SERVER_IP).build();
		this.session = this.cluster.connect(KEYSPACE);
	}

	public void writeSomething(){
		this.createTable();
		String cql = "INSERT INTO "+KEYSPACE+".someTable (k, some) VALUES (1,'someString')";
		this.session.execute(cql);
	}

	private void createTable() {
		String cql = "CREATE TABLE IF NOT EXISTS someTable (k int PRIMARY KEY, some text)";
		this.session.execute(cql);
	}


}
