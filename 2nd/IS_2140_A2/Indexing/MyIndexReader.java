package Indexing;

import Classes.Path;

import java.io.*;


public class MyIndexReader {
	//you are suggested to write very efficient code here, otherwise, your memory cannot hold our corpus...
	String docSet,dicPath,postPath;
	LineNumberReader lineNumberReader;
	public MyIndexReader( String type ) {
		//read the index files you generated in task 1
		//remember to close them when you finish using them
		//use appropriate structure to store your index
		if (type.equals("trecweb")) {
			docSet = Path.DataWebDir;
			dicPath = Path.IndexWebDir+"dictionary.trec";
			postPath = Path.IndexWebDir+"posting.trec";
		} else {
			docSet = Path.DataTextDir;
			dicPath = Path.IndexTextDir+"dictionary.trec";
			postPath = Path.IndexTextDir+"posting.trec";
		}
		lineNumberReader = null;
	}
	
	//get the non-negative integer dociId for the requested docNo
	//If the requested docno does not exist in the index, return -1
	public int GetDocid( String docno ) {
		String[] res = GetDocInfo(docno);
		if (res != null)
			return Integer.valueOf(res[1]);
		else
			return -1;
	}

	// Retrieve the docno for the integer docid
	public String GetDocno( int docid ) {
		String[] res = GetDocInfo(String.valueOf(docid));
		if (res != null)
			return res[0];
		else
			return null;
	}

	private String[] GetDocInfo(String para){
		try {
			String[] docInfo = null;
			BufferedReader bfr = new BufferedReader(new FileReader(new File(docSet)), 10*1024*1024);
			String line = "";
			while(line!=null){
				line = bfr.readLine();
				if(line.length()!=0) {
					docInfo = line.split(" ");
					if(para.equals(docInfo[0].trim()) || para.equals(docInfo[1].trim())) {
						break;
					}
				}
			}
			bfr.close();
			return docInfo;
		}catch (IOException e){
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Get the posting list for the requested token.
	 * 
	 * The posting list records the documents' docids the token appears and corresponding frequencies of the term, such as:
	 *  
	 *  [docid]		[freq]
	 *  1			3
	 *  5			7
	 *  9			1
	 *  13			9
	 * 
	 * ...
	 * 
	 * In the returned 2-dimension array, the first dimension is for each document, and the second dimension records the docid and frequency.
	 * 
	 * For example:
	 * array[0][0] records the docid of the first document the token appears.
	 * array[0][1] records the frequency of the token in the documents with docid = array[0][0]
	 * ...
	 * 
	 * NOTE that the returned posting list array should be ranked by docid from the smallest to the largest. 
	 * 
	 * @param token
	 * @return
	 */
	public int[][] GetPostingList( String token ) throws IOException {
		String postingString = GetPostingString(token);
		int[][] postingList = null;
		if (postingString!=null){
			String[] posts = postingString.split(",");
			int docs = posts.length;
			postingList = new int[docs][2];
			for (int i = 0;i < docs;i++) {
				String[] temp = posts[i].split(":");
				postingList[i][0] = Integer.valueOf(temp[0]);
				postingList[i][1] = Integer.valueOf(temp[1]);
			}
		}
		return postingList;
	}

	// Return the number of documents that contains the token.
	public int GetDocFreq( String token ) {
		String postingString = GetPostingString(token);
		int docFreq = 0;
		if (postingString!=null){
			docFreq = postingString.split(",").length;
		}
		return docFreq;
	}
	
	// Return the total number of times the token appears in the collection.
	public long GetCollectionFreq( String token ) {
		String postingString = GetPostingString(token);
		long collectionFreq = 0l;
		if (postingString!=null){
			for(String post:postingString.split(",")){
				collectionFreq += Long.valueOf(post.split(":")[1]);
			}
		}
		return collectionFreq;
	}
	
	public void Close() throws IOException {
		if(lineNumberReader!=null)
			lineNumberReader.close();
	}

	//get the line number of the token, if not exist, return -1
	private int GetRowNumber(String token){
		int lineNumber = -1;
		try{
			lineNumberReader = new LineNumberReader(new FileReader(new File(dicPath)), 10*1024*1024);
			String line = lineNumberReader.readLine();
			while(line!=null) {
				if (line.length() != 0) {
					if (token.equals(line.trim())){
						lineNumber = lineNumberReader.getLineNumber();
						break;
					}
				}
				line = lineNumberReader.readLine();
			}
			lineNumberReader.close();
		}catch (IOException e){
			e.printStackTrace();
		}
		return lineNumber;
	}

	/**
	 * get the posting list in string(line)
	 * which looks like: 18:1,19:1,11:1,12:2,14:1,10:1,
	 */
	private String GetPostingString(String token){
		try{
			int lineNumber = GetRowNumber(token);
			if (lineNumber==-1)
				return null;
			lineNumberReader = new LineNumberReader(new FileReader(new File(postPath)), 10*1024*1024);
			String line = lineNumberReader.readLine();
			int lines = 0;
			while (line != null) {
				lines++;
				if (lines == lineNumber && line.length()!=0) {
					break;
				}
				line = lineNumberReader.readLine();
			}
			lineNumberReader.close();
			return line;
		}catch (IOException e){
			e.printStackTrace();
		}
		return null;
	}
}