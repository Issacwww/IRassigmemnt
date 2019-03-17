package Search;

import Classes.Path;
import Classes.Query;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


public class ExtractQuery {
	//store the queries
	private Query[] queries;
	private int idx;
	public ExtractQuery() {
		//you should extract the 4 queries from the Path.TopicDir
		//NT: the query content of each topic should be 1) tokenized, 2) to lowercase, 3) remove stop words, 4) stemming
		//NT: you can simply pick up title only for query, or you can also use title + description + narrative for the query content.
		queries = new Query[4];
		idx = 0;
		Boolean top = false, des = false, nar = false;
		try{
			BufferedReader reader = new BufferedReader(new FileReader(
					new File(Path.TopicDir)));
			String line = "";
			StringBuffer stringBuffer = new StringBuffer();
			while ((line = reader.readLine())!=null){

				if (line.equals("<top>")) {
					top = true;
					des = false;
					nar = false;
					continue;
				}
				if (line.equals("</top>")){
					//init new Query, complete the preprocess
					// 1) tokenize, 2) to lowercase
					// 3) remove stop words, 4) stemming,
					queries[idx++] = new Query(stringBuffer.toString());
					stringBuffer.setLength(0);
					nar = false;
					top = false;
					continue;
				}
				if (top){
					if (line.substring(0,5).equals("<num>"))
						stringBuffer.append(line.split(":")[1].trim() + "#");
					else if ((line.substring(0,7).equals("<title>")))
						stringBuffer.append(line.substring(8)+"#");
					else if (line.substring(0,6).equals("<desc>")) {
						des = true;
						continue;
					}
					else if (line.substring(0,6).equals("<narr>")) {
						stringBuffer.append("#");
						des = false;
						nar = true;
						continue;
					}
					if (des || nar){
						stringBuffer.append(line + " ");
					}

				}
			}
		}catch (IOException IOe){
			IOe.printStackTrace();
		}
		idx = 0;
	}
	
	public boolean hasNext()
	{
		return idx <= queries.length - 1;
	}
	
	public Query next() {
		return queries[idx++];
	}


}
