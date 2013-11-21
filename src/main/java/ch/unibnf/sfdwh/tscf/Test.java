package ch.unibnf.sfdwh.tscf;

import java.io.IOException;

import ch.unibnf.sfdwh.tscf.fuzzy.TrapezoidClassification;

public class Test {

	public static void main(String[] args) throws IOException {
		//		CassandraConnector connection = CassandraConnector.INSTANCE;
		//		connection.writeSomething();
		//
		//		String pathToSWN = "src/main/java/ch/unibnf/sfdwh/tscf/bolts/fuzzy/SentiWordNet_3.0.0_20130122.txt";
		//		BufferedReader csv =  new BufferedReader(new FileReader(pathToSWN));
		//		String line = "";
		//		while((line = csv.readLine()) != null){
		//			String[] temp = line.split("\t");
		//			if (!temp[2].equals("0") && !temp[3].equals("0")){
		//				System.out.println(line);
		//			}
		//		}
		//		csv.close();

		new TrapezoidClassification<Double>("", 0.1, null, 0.1, 0.1);



	}

}
