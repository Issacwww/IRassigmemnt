package Classes;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Filter {
    private HashSet<String> stopwords;
    public Filter() {
        try {
            this.stopwords = new HashSet<>();
            BufferedReader reader = new BufferedReader(new FileReader(new File(Path.StopwordDir)), 10 * 1024 * 1024);//read file with 10MB Buffer
            String line;
            while((line = reader.readLine()) != null)
                stopwords.add(line.trim());
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public boolean isStopword( String word ) {
        // Return true if the input word is a stopword, or false if not.
        return this.stopwords.contains(word);
    }

    public List<String> removeStopWord(List<String> input){
        List<String> output = new ArrayList<>();
        for (String s:input){
            if (!isStopword(s))
                output.add(s);
        }
        return output;
    }
}
