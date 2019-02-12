import java.io.FileWriter;
import java.util.Map;

import Classes.Path;
import PreProcessData.*;

/**
 * !!! YOU CANNOT CHANGE ANYTHING IN THIS CLASS !!! For INFSCI 2140 in 2019.
 */
public class HW1Main {

	public static void main(String[] args) throws Exception {
		long startTime = System.currentTimeMillis();
		HW1Main hm1 = new HW1Main();
		hm1.PreProcess("trectext");// 1.96min 503473 files
		long endTime = System.currentTimeMillis();
		System.out.println("text corpus running time: " + (endTime - startTime) / 60000.0 + " min");
//		startTime = System.currentTimeMillis();
//		hm1.PreProcess("trecweb");// 1.39min 198361 files
//		long endTime = System.currentTimeMillis();
//		System.out.println("web corpus running time: " + (endTime - startTime) / 60000.0 + " min");
	}

	public void PreProcess(String dataType) throws Exception {
		// Initiate the DocumentCollection.
		DocumentCollection corpus;
		if (dataType.equals("trectext"))
			corpus = new TrectextCollection();
		else
			corpus = new TrecwebCollection();

		// Loading stopword, and initiate StopWordRemover.
		StopWordRemover stopwordRemover = new StopWordRemover();
		// Initiate WordNormalizer.
		WordNormalizer normalizer = new WordNormalizer();

		// Initiate the BufferedWriter to output result.
		FileWriter wr = new FileWriter(Path.ResultHM1 + dataType);

		// <String, Object> can hold document number and content.
		Map<String, Object> doc = null;

		// Process the corpus, document by document, iteratively.
		int count = 0;
		while ((doc = corpus.nextDocument()) != null) {
			// Load document number of the document.
			String docno = doc.keySet().iterator().next();
			// Load document content.
			char[] content = (char[]) doc.get(docno);

			// Write docno into the result file.
			wr.append(docno + "\n");

			// Initiate the WordTokenizer class.
			WordTokenizer tokenizer = new WordTokenizer(content);

			// Initiate a word object, which can hold a word.
			char[] word = null;

			// Process the document word by word iteratively.
			while ((word = tokenizer.nextWord()) != null) {
				// Each word is transformed into lowercase.
				word = normalizer.lowercase(word);

				// Only non-stopword will appear in result file.
				if (!stopwordRemover.isStopword(word))
					// Words are stemmed.
					wr.append(normalizer.stem(word) + " ");
			}
			wr.append("\n");// Finish processing one document.
			count++;
			if (count % 10000 == 0)
				System.out.println("finish " + count + " docs");
		}
		System.out.println("totaly document count:  " + count);
		wr.close();
	}
}
