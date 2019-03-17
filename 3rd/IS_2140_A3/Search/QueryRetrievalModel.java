package Search;

import Classes.Document;
import Classes.Query;
import IndexingLucene.MyIndexReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class QueryRetrievalModel {
	
	protected MyIndexReader indexReader;

	// store as global variable to reduce the file IO
	private long collection_len;
	private double mui;
	public QueryRetrievalModel(MyIndexReader ixreader, double mui) {
		indexReader = ixreader;
		this.mui = mui;
	}
	
	/**
	 * Search for the topic information. 
	 * The returned results (retrieved documents) should be ranked by the score (from the most relevant to the least).
	 * TopN specifies the maximum number of results to be returned.
	 * 
	 * @param aQuery The query to be searched for.
	 * @param TopN The maximum number of returned document
	 * @return
	 */
	
	public List<Document> retrieveQuery(Query aQuery, int TopN ) throws IOException {
		// NT: you will find our IndexingLucene.Myindexreader provides method: docLength()
		// implement your retrieval model here, and for each input query, return the topN retrieved documents
		// sort the documents based on their relevance score, from high to low
		collection_len = indexReader.collectionLength();


		//Choose TreeSet to remove duplicate and rank the docs
		Set<Document> candidates = new TreeSet<>(Document::compareTo);

		//get candidate documents(contain query terms)
		for (String term : aQuery.getTitle()){
			int[][] posting = indexReader.getPostingList(term);
			if (posting != null) {
				for (int ix = 0; ix < posting.length; ix++) {
					int docid = posting[ix][0];
					candidates.add(new Document(String.valueOf(docid), indexReader.getDocno(docid), refence(aQuery, docid)));
				}
			}
		}
		//get topN
		return topN(candidates,TopN);
	}

	private List<Document> topN(Set<Document> docs, int n){
		int tmp = 0;
		List<Document> results = new ArrayList<>();
		for (Document d:docs){
			if (tmp == n)
				break;
			results.add(d);
			tmp++;
		}
		return results;
	}

	//language model
	private double refence(Query query,int docid) throws IOException{
		double score = 1.0;
		List<String> title = query.getTitle();
		for(String term:title)
			score*=Dirichlet(term,docid);
		return score;
	}

	//smoothing
	private double Dirichlet(String term, int docid) throws IOException{
		int doclen = indexReader.docLength(docid);
		long colFreq = indexReader.CollectionFreq(term);
		if (colFreq == 0)
			return 1.0;
		long docTermFreq = indexReader.DocTermsFreq(term,docid);
		double score = (docTermFreq + mui * colFreq / collection_len) / (doclen + mui);
		return score;
	}

}