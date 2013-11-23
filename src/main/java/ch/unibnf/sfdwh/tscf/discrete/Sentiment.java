package ch.unibnf.sfdwh.tscf.discrete;

public enum Sentiment {
	NEGATIVE(3, "negative"),POSITIVE(2, "positive");

	int position;
	private String term;

	private Sentiment(int pos, String term){
		this.position = pos;
		this.term = term;
	}

	public int getPosition(){
		return this.position;
	}

	public String getTerm(){
		return this.term;
	}
}