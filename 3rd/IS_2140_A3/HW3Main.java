import java.io.IOException;
import java.util.List;

import Classes.*;
import IndexingLucene.*;
import Search.*;

/**
 * !!! YOU CANNOT CHANGE ANYTHING IN THIS CLASS !!!
 * 
 * Main class for running your HW3.
 * 
 */
public class HW3Main {

	public static void main4tuning(double mui) throws IOException {
		// Initialization.
		MyIndexReader ixreader = new MyIndexReader("trectext");
		QueryRetrievalModel model = new QueryRetrievalModel(ixreader,mui);
		System.out.println("----------------\n\nResults for mui= "+ mui + "\n");
		// Extract the queries.
		ExtractQuery queries = new ExtractQuery();

		long startTime = System.currentTimeMillis();
		while (queries.hasNext()) {
			Query aQuery = queries.next();
			System.out.println(aQuery.GetTopicId() + "\t" + aQuery.GetQueryContent());
			// Retrieve 20 candidate documents on the indexing.
			List<Document> results = model.retrieveQuery(aQuery, 20);
			if (results != null) {
				int rank = 1;
				for (Document result : results) {
					System.out.println(aQuery.GetTopicId() + " Q0\t" + result.docno() + "\t" + rank + "\t" + result.score() + "\tMYRUN");
					rank++;
				}
			}
		}
		long endTime = System.currentTimeMillis(); // end time of running code
		System.out.println("\n\n4 queries search time: " + (endTime - startTime) / 60000.0 + " min\n\n----------------\n");
		ixreader.close();
	}
	public static void main(String[] args) throws Exception {
		for (double mui = 1500.0;mui <= 2500;mui+=250){
			main4tuning(mui);
		}
	}

}
