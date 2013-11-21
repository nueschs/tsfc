package ch.unibnf.sfdwh.tscf.discrete;

public enum Sentiment {
	NEGATIVE(3),POSITIVE(2);

	int position;

	private Sentiment(int pos){
		this.position = pos;
	}

	public int getPosition(){
		return this.position;
	}
}