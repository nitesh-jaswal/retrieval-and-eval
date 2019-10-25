import java.io.IOException;
import java.io.File;
import java.io.BufferedReader;
import java.nio.file.Paths;
import java.util.LinkedHashSet;
import java.util.Comparator;
import java.util.LinkedHashMap;

import org.apache.lucene.benchmark.quality.trec.TrecTopicsReader;
import org.apache.lucene.benchmark.quality.QualityQuery;

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
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;

public class searchTRECtopics {
	public enum ENTRIES {TOP5, TOP10, TOP100, TOP1000, ALL};
	
	public searchTRECtopics () {
		
	}
	
	public static void main(String[] args) throws IOException {
		// TODO
		// 1. Find trec_eval 2. Format output according to requirement
		String topicsPath = "F:\\Current Study\\Search\\Assignment 2\\retrieval-and-eval";
//		File file
		searchTRECtopics obj = new searchTRECtopics();
		TrecTopicsReader topics = new TrecTopicsReader();
//		QualityQuery my_queries = topics.readQueries();

	}
	
	public void searchTopics() {
		
	}

}
