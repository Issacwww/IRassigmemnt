package PreProcessData;
import Classes.*;

import java.io.*;
import java.util.HashSet;

public class StopWordRemover {
	// Essential private methods or variables can be added.
	private HashSet<String> stopwords;
	// YOU SHOULD IMPLEMENT THIS METHOD.
	public StopWordRemover( ) {
		// Load and store the stop words from the fileinputstream with appropriate data structure.
		// NT: address of stopword.txt is Path.StopwordDir
		try {
			//HashSet won't have duplicate value， O(1) for search
			this.stopwords = new HashSet<>();
			BufferedInputStream fis = new BufferedInputStream(new FileInputStream(new File(Path.StopwordDir)));
			BufferedReader reader = new BufferedReader(new InputStreamReader(fis, "utf-8"), 10 * 1024 * 1024);//read file with 10MB Buffer
			String line;
			while((line = reader.readLine()) != null)
				stopwords.add(line);
		}
		catch (IOException ex){
			ex.printStackTrace();
		}
	}

	// YOU SHOULD IMPLEMENT THIS METHOD.
	public boolean isStopword( char[] word ) {
		// Return true if the input word is a stopword, or false if not.
		return this.stopwords.contains(String.valueOf(word));
	}
}
