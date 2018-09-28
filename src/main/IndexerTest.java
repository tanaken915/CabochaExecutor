package main;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import dic.DictionaryIndexer;
import dic.WordReader;

public class IndexerTest {

	public static void main(String[] args) {
		Path nounFile = Paths.get("resource/words/nouns.txt");
		List<String> nouns = WordReader.read(nounFile); 
		
		DictionaryIndexer indexer = new DictionaryIndexer();
		
		Path csv = indexer.nouns2csv(nouns);
		indexer.execute(csv);
	}

}