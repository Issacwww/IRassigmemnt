package Classes;

import java.util.List;

public class Query {
	//you can modify this class
	private Filter filter;
	private String queryContent;
	private String topicId;
	private List<String> title; //store the title for search and simplify the following operations using list
//	private List<String> desc;
//	private List<String> narr;

	public Query(String topic){
		queryContent = "";
		String[] tmp = topic.toLowerCase().split("#");
		filter = new Filter();
		topicId = tmp[0];
		title = Preprocess(tmp[1]);
//		desc = Preprocess(tmp[2]);
//		narr = Preprocess(tmp[3]);
		for (String t: title)
			queryContent += t + " ";
		queryContent = queryContent.trim();
//		queryContent +="\nDescription:";
//		for (String t: desc)
//			queryContent += " " + t;
//		queryContent +="\nNarrative:";
//		for (String t: narr)
//			queryContent += " " + t;
	}
	
	public String GetQueryContent() {
		return queryContent;
	}
	public String GetTopicId() {
		return topicId;
	}

	public List<String> getTitle() {
		return title;
	}

	public void SetQueryContent(String content){
		queryContent=content;
	}	
	public void SetTopicId(String id){
		topicId=id;
	}

	public List<String> Preprocess(String input){
		return Normalizer.stem(filter.removeStopWord(Tokenizer.token(input)));
	}

	@Override
	public String toString() {
		return "Number: " + topicId + " --- " + queryContent;
	}
}
