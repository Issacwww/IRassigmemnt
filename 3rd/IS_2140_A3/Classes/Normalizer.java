package Classes;

import java.util.ArrayList;
import java.util.List;

public class Normalizer {
    public static List<String> stem(List<String> input) {
        List<String> output = new ArrayList<>();
        for (String in: input){
            char[] chars = in.toCharArray();
            Stemmer s = new Stemmer();
            s.add(chars, chars.length);
            s.stem();
            output.add(s.toString());
        }
        return output;
    }
}
