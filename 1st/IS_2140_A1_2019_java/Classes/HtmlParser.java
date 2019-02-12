package Classes;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;

public class HtmlParser extends HTMLEditorKit.ParserCallback{
    //parse the html file
    private boolean toLoad;
    private StringBuffer webContent;
    public HtmlParser(){
        super();
        this.webContent = new StringBuffer();
        this.toLoad = true;
    }

    public void handleStartTag(HTML.Tag t, MutableAttributeSet a, int pos)
    {
        if(t.equals(HTML.Tag.BODY))
        {
            toLoad = true;
        }
    }

    public void handleEndTag(HTML.Tag t, int pos)
    {
        if(t.equals(HTML.Tag.BODY))
        {
            toLoad = false;
        }
    }

    //handle the text in the tag <body>
    public void handleText(char[] data, int pos)
    {
        if(toLoad)
            this.webContent.append(data).append(' ');
    }

    public StringBuffer getWebContent() {
        return webContent;
    }
}
