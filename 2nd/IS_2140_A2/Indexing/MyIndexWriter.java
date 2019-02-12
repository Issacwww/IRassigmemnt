package Indexing;

import Classes.Path;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class MyIndexWriter {
	// I suggest you to write very efficient code here, otherwise, your memory cannot hold our corpus...

	//Intermediate blockIndex file (would be deleted once this block merged to the final result)
	private File blockIndex;

	// two writer to the files
	private BufferedWriter blockWr,docSetWr;

	//output directory
	private String type,outDir;

	//store index for one block in the map: <term,<docId,docFreq>>
	private Map<String,Map<Integer,Integer>> index;

	//docId, number of block
	private Integer docId,block;

	public MyIndexWriter(String type) throws IOException{
		// This constructor should initiate the FileWriter to output your index files
		// remember to close files if you finish writing the index
		docId = 0;
		block = 0;
		index = new TreeMap<>();
		this.type = type;
		String docSet;
		if (this.type.equals("trecweb")) {
			outDir = Path.IndexWebDir;
			docSet = Path.DataWebDir;
		} else {
			outDir = Path.IndexTextDir;
			docSet = Path.DataTextDir;
		}
		docSetWr = new BufferedWriter(new FileWriter(docSet,true));
		blockWr = null;
		blockIndex = null;
	}
	
	public void IndexADocument(String docno, String content) throws IOException {//Index a block of documents
		// you are strongly suggested to build the index by installments
		// you need to assign the new non-negative integer docId to each document, which will be used in MyIndexReader
		docId++;
		docSetWr.write(docno+" "+docId+"\n");
		String[] tokens = content.split("\\s+");
		Map<Integer,Integer> temp = null;
		//store the index for a block in the map
		for(String token:tokens){
			if(token.equals(""))
				continue;//skip the "" string
			if(index.containsKey(token)){
				temp = index.get(token);
				if (temp.containsKey(docId))
					temp.put(docId,temp.get(docId)+1);
				else
					temp.put(docId,1);
				index.put(token,temp);
			}
			else {
				temp = new TreeMap<>();
				temp.put(docId,1);
				index.put(token, temp);
			}
		}

		// 40000 documents for a block, if this block is full, write it to the file
		if (docId%40000==0){
			writeBlock();
			//since there are other documents should be assigned id
			//write the docNo docId pair append the end of this file and not close it
			docSetWr.flush();
//			System.out.println("block " + block + " wrote");
			block++;
			index.clear();
		}

	}
	
	public void Close() throws IOException {
		//write the last not-full block and close the writers
		writeBlock();
		docSetWr.flush();
		docSetWr.close();
		//merge all the blocks
		BlockMerger merger = new BlockMerger(this.type);
		merger.Merge();
		merger.Divide();
	}

	public void writeBlock(){
		try{
			blockIndex = new File(outDir + block+".txt");
			if (!blockIndex.exists())
				blockIndex.createNewFile();
			blockWr = new BufferedWriter(new FileWriter(blockIndex));
			for (Entry<String,Map<Integer,Integer>> token: index.entrySet()){
				// use & as a marker to split term and posting
				blockWr.write(token.getKey().trim()+"&");
				for(Entry idFreq:token.getValue().entrySet()){
					// store the posting list in the form of docId:docFreq, and use "," to split each document
					blockWr.write(idFreq.getKey()+":"+idFreq.getValue()+",");
				}
				blockWr.write("\n");
			}
			blockWr.flush();
			blockWr.close();
		}catch (IOException e){
			e.printStackTrace();
		}
	}

}
