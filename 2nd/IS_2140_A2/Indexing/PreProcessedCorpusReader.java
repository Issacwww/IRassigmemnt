package Indexing;

import Classes.Path;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class PreProcessedCorpusReader {

	static BufferedReader reader;
	
	public PreProcessedCorpusReader(String type) throws IOException {
		// This constructor opens the pre-processed corpus file, Path.ResultHM1 + type
		// You can use your own version, or download from http://crystal.exp.sis.pitt.edu:8080/iris/resource.jsp
		// Close the file when you do not use it any more
		String path = Path.ResultHM1 + type;
		this.reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(path)), StandardCharsets.UTF_8), 10 * 1024 * 1024);//read file with 10mb Buffer

	}
	

	public Map<String, String> NextDocument() throws IOException {
		// read a line for docNo, put into the map with <"DOCNO", docNo>
		// read another line for the content , put into the map with <"CONTENT", content>
		Map<String, String> document = new HashMap<>();
		String docNo,content;
		if((docNo = reader.readLine())!=null){
			content = reader.readLine();

			document.put("DOCNO",docNo.trim());
			document.put("CONTENT",content.trim());
			return document;
		}else{
			reader.close();
			return null;
		}
	}
}
