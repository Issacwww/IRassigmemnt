package Classes;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tokenizer {
    protected static String pattern = "(?:[A-Z]\\.)+" +
            "|\\$?\\d+(?:\\.\\d+)?%?" +
            "|\\w+(?:[-']\\w+)*" +
            "|\\.\\.\\." +
            "|(?:[-.,;\"'?():_`<>{}+&@!^#*\\[\\]])";
    public static List<String> token(String input){
        List<String> tokens = new ArrayList<>();
        Matcher m = Pattern.compile(pattern)
                .matcher(input);
        //store all the tokens
        while (m.find()) {
            tokens.add(m.group());
        }
        return tokens;
    }
}
