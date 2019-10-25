import java.io.IOException;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Paths;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Comparator;
import java.util.LinkedHashMap;

import org.apache.lucene.benchmark.quality.trec.TrecTopicsReader;
import org.apache.lucene.benchmark.quality.QualityQuery;
import org.apache.lucene.benchmark.quality.QualityQueryParser;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.index.MultiFields;
import org.apache.lucene.index.PostingsEnum;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;

public class searchTRECtopics {
	private String indexPath;
	public enum ENTRIES {TOP5, TOP10, TOP20, TOP100, TOP1000, ALL};
	
	public searchTRECtopics() {
		indexPath = "F:\\Current Study\\Search\\Assignment 2\\retrieval-and-eval\\index";
	}
	
	public static void main(String[] args) throws IOException, ParseException {
		// 1. Find trec_eval 2. Format output according to requirement
		String topicsPath = "F:\\Current Study\\Search\\Assignment 2\\retrieval-and-eval\\topics.51-100";
		String indexPath = "F:\\Current Study\\Search\\Assignment 2\\retrieval-and-eval\\index";
		
//		(new searchTRECtopics()).searchTopics(topicsPath, (new easySearch(indexPath)));
		(new searchTRECtopics()).searchTopics(topicsPath, indexPath, "EasySearch");
		
		
//		LinkedHashMap<String, Double> p = (new searchTRECtopics()).getTopX(easyObj.calculateScores("TEXT", "Donald Trump"), ENTRIES.TOP10);
//		easyObj.prin tScores(p);
		
//		System.out.println(myQueries[2].getValue("description ").split("<smry>")[0]); 
	}
	
	public void searchTopics(String topicsPath, String indexPath, String similarity) throws ParseException, IOException {
		
		
		File file = new File(topicsPath);
		easySearch easyObj = new easySearch(indexPath);
		TrecTopicsReader topics = new TrecTopicsReader();
				
		QualityQuery myQueries[] = topics.readQueries(new BufferedReader(new FileReader(file)));
		LinkedHashMap<String, Double> shortQuery;
		LinkedHashMap<String, Double> longQuery;
		String shortQueryTxt = "", longQueryTxt = "";
		
		for(QualityQuery query: myQueries) {
			// qId returned is of format "051" and we need "51"
			int qId = Integer.parseInt(query.getQueryID());
			/* 
			  Error was raised due to presence of "/" character
			  "Topic: " was removed from <title> query
			  "<smry> " was removed from <desc> query since parser was unable to parse it 
			 */
			String titleQuery = query.getValue("title").replaceFirst("[Tt][Oo][Pp][Ii][Cc]:", "").replace("/", " ");
			String descQuery = query.getValue("description").split("<smry>")[0].replace("/", " ");
			shortQuery = getTopX(easyObj.calculateScores("TEXT", titleQuery), ENTRIES.TOP1000);
			longQuery = getTopX(easyObj.calculateScores("TEXT", descQuery), ENTRIES.TOP1000);
			shortQueryTxt += toText(shortQuery, qId);
			longQueryTxt += toText(longQuery, qId);
	}
		writeToFile("F:\\Current Study\\Search\\Assignment 2\\retrieval-and-eval\\out\\EasySearchshortQuery.txt", shortQueryTxt);
		writeToFile("F:\\Current Study\\Search\\Assignment 2\\retrieval-and-eval\\out\\EasySearchlongQuery.txt", longQueryTxt);
}
	public LinkedHashMap<String, Double> getSimilarityScores(String similarity, String queryString) throws ParseException, IOException {
		LinkedHashMap<String, Double> scores = new LinkedHashMap<String, Double>();
		if(similarity.equals("EasySearch")) {
			easySearch obj = new easySearch(indexPath);
			scores = obj.calculateScores("TEXT", queryString);
		}
		return(scores);
	}
	public void writeToFile(String path , String text) throws IOException {
		File file = new File(path);
		if(!file.exists()) {
			file.createNewFile();
		}
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
        bw.write(text);
        bw.close();
	}
	
	public String toText(LinkedHashMap<String, Double> docScore, int qId) {
		String txt = "";
		int i = 1;
		for(String docId: docScore.keySet()) {
			txt += qId + " 0 " + docId + " " + i + " " + docScore.get(docId) + " EasySearch\n";
			++i;
		}
		return(txt);
	}
	
	public LinkedHashMap<String, Double> getTopX(LinkedHashMap<String, Double> scoreDoc, ENTRIES e) {
		LinkedHashMap<String, Double> topXScoreDoc;
		switch(e) {
		case TOP5:		topXScoreDoc = scoreDoc.entrySet().stream().limit(5).
											collect(LinkedHashMap::new, (k, v) -> k.put(v.getKey(), v.getValue()), LinkedHashMap::putAll);
						break;
		case TOP10: 	topXScoreDoc = scoreDoc.entrySet().stream().limit(10).
											collect(LinkedHashMap::new, (k, v) -> k.put(v.getKey(), v.getValue()), LinkedHashMap::putAll);
						break;
		case TOP20: 	topXScoreDoc = scoreDoc.entrySet().stream().limit(20).
											collect(LinkedHashMap::new, (k, v) -> k.put(v.getKey(), v.getValue()), LinkedHashMap::putAll);
						break;

		case TOP100: 	topXScoreDoc = scoreDoc.entrySet().stream().limit(100).
											collect(LinkedHashMap::new, (k, v) -> k.put(v.getKey(), v.getValue()), LinkedHashMap::putAll);
						break;
		case TOP1000: 	topXScoreDoc = scoreDoc.entrySet().stream().limit(1000).
											collect(LinkedHashMap::new, (k, v) -> k.put(v.getKey(), v.getValue()), LinkedHashMap::putAll);
						break;
		default:		System.out.println("Limit not recognized. Returning all elements");
						topXScoreDoc = scoreDoc;
		}
		return(topXScoreDoc);
	}

}
