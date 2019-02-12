package PreProcessData;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.Map;
import Classes.Path;

/**
 * This is for INFSCI 2140 in 2019
 *
 */
public class TrectextCollection implements DocumentCollection {
	// Essential private methods or variables can be added.
	static BufferedReader reader;
	// YOU SHOULD IMPLEMENT THIS METHOD.
	public TrectextCollection() throws IOException {
		// 1. Open the file in Path.DataTextDir.
		// 2. Make preparation for function nextDocument().
		// NT: you cannot load the whole corpus into memory!!
		try {

			BufferedInputStream fis = new BufferedInputStream(new FileInputStream(new File(Path.DataTextDir)));
			this.reader = new BufferedReader(new InputStreamReader(fis, "utf-8"), 10 * 1024 * 1024);//read file with 10MB Buffer

		}
		catch (IOException ex){
			ex.printStackTrace();
		}
		
	}
	
	// YOU SHOULD IMPLEMENT THIS METHOD.
	public Map<String, Object> nextDocument() throws IOException {
		// 1. When called, this API processes one document from corpus, and returns its doc number and content.
		// 2. When no document left, return null, and close the file.
		try{
			Map document = new LinkedHashMap();
			String line, docNo = "", text = "";
			Boolean txt = null;
			while((line = reader.readLine()) != null){
				int l = line.length();
				//parser the docno field
				if(l > 15 && line.substring(0,7).equals("<DOCNO>")) {
					docNo = line.split("</")[0].substring(8).trim();
					document.put(docNo, "");
					continue;
				}
				//parser the date field
				else if(l > 15 && line.substring(0,11).equals("<DATE_TIME>"))
					document.put("date", line.split("</")[0].substring(12).trim().toCharArray());
				//parser the headline field
				else if (l > 15 && line.substring(0,10).equals("<HEADLINE>"))
					document.put("headline", line.split("</")[0].substring(11).trim().toCharArray());
				//begin of text field
				else if(line.equals("<TEXT>")) {
					txt = true;
					continue;
				}
				// end of text field
				else if(line.equals("</TEXT>"))
					txt = false;
				//end of the document
				else if(line.equals("</DOC>"))
					return document;
				//store and process the text
				if(txt != null){
					if(txt)
						text += line.trim();
					else {
						document.put(docNo, text.toCharArray());
						txt = null;
					}
				}

			}
		}
		catch (IOException ex){
			ex.printStackTrace();
		}
		return null;
	}
	
}
