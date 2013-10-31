package ch.unibnf.sfdwh.tscf;

public class Test {

	public static void main(String[] args) {
		CassandraConnector connection = CassandraConnector.INSTANCE;
		connection.writeSomething();

	}

}
