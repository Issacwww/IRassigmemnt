package PreProcessData;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.Map;

import Classes.HtmlParser;
import Classes.Path;

import javax.swing.text.html.parser.ParserDelegator;

/**
 * This is for INFSCI 2140 in 2018
 *
 */
public class TrecwebCollection implements DocumentCollection {
	// Essential private methods or variables can be added.
	static BufferedReader reader;
	Boolean web;
	// YOU SHOULD IMPLEMENT THIS METHOD.
	public TrecwebCollection() throws IOException {
		// 1. Open the file in Path.DataWebDir.
		// 2. Make preparation for function nextDocument().
		// NT: you cannot load the whole corpus into memory!!
		try {

			BufferedInputStream fis = new BufferedInputStream(new FileInputStream(new File(Path.DataWebDir)));
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
		// 3. the HTML tags should be removed in document content.
		try{

			String line, docNo = "";
			StringBuffer content = new StringBuffer();
			while((line = reader.readLine()) != null){
				int l = line.length();
				//parser the docno field
				if(l > 15 && line.substring(0,7).equals("<DOCNO>")) {
					docNo = line.split("</")[0].substring(8).trim();
				}
				else if (line.equals("<DOCHDR>"))
					web = false;
				else if(line.equals("<html>") || (l > 10 && line.substring(0,5).equals("<html")))  //begin to read the web content into buffer
					web = true;
				//end of the document
				else if(line.equals("</DOC>")) {
					web = null;
					if (content == null)
						return null;
					//parse the html and get the text
					Map webpage = new LinkedHashMap();
					HtmlParser p = new HtmlParser();
					ParserDelegator pd = new ParserDelegator();
					pd.parse(new BufferedReader(new InputStreamReader(new ByteArrayInputStream(content.toString().getBytes()))), p, true);
					webpage.put(docNo,p.getWebContent().toString().toCharArray());
					return webpage;
				}

				if(web != null && web)
					content.append(line);
			}
		}
		catch (IOException ex){
			ex.printStackTrace();
		}
		return null;
	}
	
}
