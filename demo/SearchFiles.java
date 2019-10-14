/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.store.FSDirectory;

public class SearchFiles {

	private SearchFiles() {
	}

	public static void main(String[] args) throws Exception {
		String queryString = "police";

		String index = "/Users/chunguo/Downloads/index";
		IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths
				.get(index)));
		IndexSearcher searcher = new IndexSearcher(reader);
		Analyzer analyzer = new StandardAnalyzer();
		searcher.setSimilarity(new BM25Similarity());

		QueryParser parser = new QueryParser("TEXT", analyzer);
		Query query = parser.parse(queryString);
		System.out.println("Searching for: " + query.toString("TEXT"));

		TopDocs results = searcher.search(query, 1000);		

		//Print number of hits
		int numTotalHits = results.totalHits;
		System.out.println(numTotalHits + " total matching documents");

		//Print retrieved results
		ScoreDoc[] hits = results.scoreDocs;
		for(int i=0;i<hits.length;i++){	
			System.out.println("doc="+hits[i].doc+" score="+hits[i].score);
			Document doc=searcher.doc(hits[i].doc);	
			System.out.println("DOCNO: "+doc.get("DOCNO"));
			System.out.println("TEXT: "+doc.get("TEXT"));
		}
		
		reader.close();
	}
}
