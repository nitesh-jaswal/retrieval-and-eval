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

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Index all text files under a directory.
 * <p>
 * This is a command-line application demonstrating simple Lucene indexing. Run
 * it with no command-line arguments for usage information.
 */
public class IndexFiles {

	private IndexFiles() {
	}

	/** Index all text files under a directory. */
	public static void main(String[] args) {
		ArrayList<HashMap<String, String>> documents = new ArrayList();

		HashMap<String, String> document1 = new HashMap<String, String>();
		document1.put("DOCNO", "AP891205-0034");
		document1
				.put("TEXT",
						"The District of Columbia police department has "
								+ "concluded its officers acted properly in handling allegations that "
								+ "Mayor Marion Barry was hospitalized for a drug overdose six years "
								+ "ago, a newspaper reported today. "
								+ "Police Chief Isaac Fulwood believes the department and "
								+ "then-chief Maurice Turner were right to drop the matter after the "
								+ "allegation could not be corroborated, sources told The Washington "
								+ "Post. "
								+ "A Howard University psychiatrist reportedly told police "
								+ "investigators in 1984 that the mayor suffered from a drug overdose "
								+ "in 1983. " + "Barry, in an effort M");
		documents.add(document1);

		HashMap<String, String> document2 = new HashMap<String, String>();
		document2.put("DOCNO", "AP891205-0023");
		document2
				.put("TEXT",
						"A mistrial was declared in burglary "
								+ "case when a prospective juror told the prosecutor he had determined "
								+ "through psychic powers that the defendants were guilty. "
								+ "Seven jurors had been seated Monday in the second-degree "
								+ "burglary and larceny trial of Argentina Alford and Bobby McNeil "
								+ "when Mellen T. Benedict made his statement to prosecutor Andy "
								+ "Dempster, according to court officials. "
								+ "Asked by Dempster if he could judge the case on evidence from "
								+ "the witness stand, Benedict responded that it was his professional "
								+ "opinion the two were guilty. "
								+ "Benedict is listed in the telephone book as a ``metaphysical "
								+ "expert and professional psychic.''"
								+ "Ray Vallery, representing Alford, said the defense couldn't "
								+ "proceed if any of the jurors believed what Benedict said. "
								+ "``I didn't hear anyone laughing in the courtroom when he said "
								+ "it,'' Vallery said. "
								+ "Alford and McNeil are accused of a 1988 burglary. The trial was "
								+ "rescheduled.");
		documents.add(document2);

		String indexPath = "/Users/chunguo/Downloads/index";

		try {
			System.out.println("Indexing to directory '" + indexPath + "'...");

			Directory dir = FSDirectory.open(Paths.get(indexPath));
			Analyzer analyzer = new StandardAnalyzer();
			IndexWriterConfig iwc = new IndexWriterConfig(analyzer);

			iwc.setOpenMode(OpenMode.CREATE);

			IndexWriter writer = new IndexWriter(dir, iwc);

			for (HashMap<String, String> document : documents) {
				indexDoc(writer, document);
			}

			writer.close();
			System.out.println("Done ...");
		} catch (IOException e) {
			System.out.println(" caught a " + e.getClass()
					+ "\n with message: " + e.getMessage());
		}
	}

	/** Indexes a single document 
	 * @throws IOException */
	static void indexDoc(IndexWriter writer, HashMap<String, String> document) throws IOException {
		// make a new, empty document
		Document lDoc = new Document();

		lDoc.add(new StringField("DOCNO", document.get("DOCNO"),
				Field.Store.YES));

		lDoc.add(new TextField("TEXT", document.get("TEXT"), Field.Store.NO));
		writer.addDocument(lDoc);
	}
}
