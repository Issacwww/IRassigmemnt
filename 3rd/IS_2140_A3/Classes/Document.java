package Classes;

public class Document implements Comparable<Document>{
	
	protected String docid;
	protected String docno;
	protected double score;
	
	public Document( String docid, String docno, double score ) {
		this.docid = docid;
		this.docno = docno;
		this.score = score;
	}
	
	public String docid() {
		return docid;
	}
	
	public String docno() {
		return docno;
	}
	
	public double score() {
		return score;
	}
	
	public void setDocid( String docid ) {
		this.docid = docid;
	}
	
	public void setDocno( String docno ) {
		this.docno = docno;
	}
	
	public void setScore( double score ) {
		this.score = score;
	}

	@Override
	public String toString() {
		return docno + " : " + score;
	}

	// override following functions to store and sort the documents in the set

	@Override
	public int compareTo(Document document) {
		// sort the set in decrease order
		return Double.compare(document.score(), score);
	}

	// remove the duplicate document while getting the candidates
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Document){
			Document doc = (Document)obj;
			return docno.equals(doc.docno())&&docid.equals(doc.docid());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return docid.hashCode() + docno.hashCode();
	}
}
