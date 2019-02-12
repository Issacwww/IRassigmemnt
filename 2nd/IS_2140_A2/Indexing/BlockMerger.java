package Indexing;

import Classes.Path;

import java.io.*;
import java.util.Map;
import java.util.TreeMap;

public class BlockMerger {
    private String dir;

    //store the final index:<term,posting>
    private Map<String,String> index;
    public BlockMerger(String type){
        if (type.equals("trecweb"))
            dir = Path.IndexWebDir;
        else
            dir = Path.IndexTextDir;
        index = null;
    }

    public void Merge(){//O(n2)
        try{
            File file = new File(dir);
            File[] files = file.listFiles();
            Map<String, String> blockIndex;
            for(File f:files){
                //all the intermediate block indexes are stored in txt format
                if (f.getName().endsWith(".txt")) {
                    blockIndex = file2Map(f);
                    mergeHelper(blockIndex);
                    //delete the intermediate file
                    f.delete();
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    private void mergeHelper(Map<String,String> blockIndex) {
        if(index==null){
            index = new TreeMap(blockIndex);
        }else{
            for (Map.Entry<String,String> entry: blockIndex.entrySet()) {
                String term = entry.getKey();
                if (index.containsKey(term))
                    index.put(term,index.get(term)+entry.getValue());
                else
                    index.put(term,entry.getValue());
            }
        }
    }

    private Map file2Map(File file) throws IOException{
        Map<String,String> blockIndex = new TreeMap();
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line = br.readLine();
        while (line!=null&&line.length()!=0){
            String[] entry = line.split("&");
            //skip the empty string
            if (!entry[0].equals(""))
                blockIndex.put(entry[0],entry[1]);
            line = br.readLine();
        }
        br.close();
        return blockIndex;
    }

    public void Divide() {
        try {
            File dictionary = new File(dir + "dictionary.trec"), posting = new File(dir + "posting.trec");
            BufferedWriter dicBr = new BufferedWriter(new FileWriter(dictionary,true)),
                    postBr = new BufferedWriter(new FileWriter(posting,true));
            if (!dictionary.exists() || !posting.exists()) {
                dictionary.createNewFile();
                posting.createNewFile();
            }
            int cnt = 0;
            //System.out.println("index size:"+index.size());
            //store the term and posting separately into the files
            for (Map.Entry<String,String> entry : index.entrySet()) {
                dicBr.write(entry.getKey());
                dicBr.newLine();
                postBr.write(entry.getValue());
                postBr.newLine();
                cnt++;
                //every 20000 terms, write one time
                if (cnt == 20000) {
                    dicBr.flush();
                    postBr.flush();
                    cnt = 0;
                }
            }
            dicBr.flush();
            dicBr.close();
            postBr.flush();
            postBr.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
