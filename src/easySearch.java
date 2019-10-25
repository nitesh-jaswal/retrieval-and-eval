import java.io.IOException;
import java.nio.file.Paths;
import java.util.LinkedHashSet;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

public class easySearch {
	
	private String indexPath;
		
	public easySearch(String p) {
		indexPath = p;
	}
	
	public static void main(String[] args)  throws ParseException, IOException {
//		String indexPath = "F:\\Current Study\\Search\\Assignment 2\\retrieval-and-eval\\index";
		String queryString = "Donald Trump";
		easySearch obj = new easySearch("F:\\Current Study\\Search\\Assignment 2\\retrieval-and-eval\\index");
		LinkedHashMap<Integer, Double> sortedDocScore = obj.calculateScores("TEXT", queryString);
		obj.printScores(sortedDocScore);
	}
	
	public LinkedHashMap<Integer, Double> calculateScores(String zone, String queryString) throws ParseException, IOException {
		
		LinkedHashMap<Integer, Double> docScore = new LinkedHashMap<Integer, Double>();
		// Stores F(q,doc) for each doc 
		IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths
				.get(indexPath)));
		IndexSearcher searcher = new IndexSearcher(reader);
		
		// Get the preprocessed query terms
		Analyzer analyzer = new StandardAnalyzer();
		QueryParser parser = new QueryParser(zone, analyzer);
		Query query = parser.parse(queryString);
		Set<Term> queryTerms = new LinkedHashSet<Term>();
		searcher.createNormalizedWeight(query, false).extractTerms(queryTerms);
//		System.out.println("Terms in the query: ");
		
		int N = reader.maxDoc(); // Total number of docs
		for (Term t : queryTerms) {

			int df = reader.docFreq(t); // Document Frequency
			// Use DefaultSimilarity.decodeNormValue() to decode normalized
			// document length
			ClassicSimilarity dSimi = new ClassicSimilarity();
			// Get the segments of the index
			List<LeafReaderContext> leafContexts = reader.getContext().reader()
					.leaves();
			// Processing each segment
			for (int i = 0; i < leafContexts.size(); i++) {
				// Get document length
				LeafReaderContext leafContext = leafContexts.get(i);
				int startDocNo = leafContext.docBase;
				PostingsEnum de = MultiFields.getTermDocsEnum(leafContext.reader(),
						zone, new BytesRef(t.text()));
				
				int doc;
				if (de != null) {
					while ((doc = de.nextDoc()) != PostingsEnum.NO_MORE_DOCS) {
						int docId = de.docID();
						float normDocLeng = dSimi.decodeNormValue(leafContext.reader()
								.getNormValues(zone).get(docId));
						float docLeng = 1 / (normDocLeng * normDocLeng);
						int tf = de.freq();
						int docKey = docId + startDocNo;
						// 
						double score = (double)(tf/docLeng)*Math.log10((1 + (double)N/df));
						if(docScore.containsKey(docKey)) {
							double currVal = docScore.get(docKey);
							docScore.put(docKey, currVal + score);
						}
						else {
							docScore.put(docKey, score);
						}
					}
				}
			}
		}
		
		LinkedHashMap<Integer, Double> sortedDocScore = new LinkedHashMap<Integer, Double>();
		 docScore.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
		 					.forEach(x -> sortedDocScore.put(x.getKey(), x.getValue()));
		return(sortedDocScore);
	}
	
	
	public void printScores(LinkedHashMap<Integer, Double> docScore) {
		for(int docid: docScore.keySet())
			System.out.println("DocID: " + docid + "\tScore: " + docScore.get(docid));
		System.out.println(docScore.keySet());
	}
	
}


