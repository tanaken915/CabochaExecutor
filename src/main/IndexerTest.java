package main;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import dic.DictionaryIndexer;

public class IndexerTest {

	public static void main(String[] args) {
		Path nounFile = Paths.get("resource/words/nouns.txt");
		List<String> nouns = Collections.emptyList();
		try (Stream<String> lines = Files.lines(nounFile, StandardCharsets.UTF_8)) {
			nouns = lines.collect(Collectors.toList());
		} catch (IOException e) {
			e.printStackTrace();
		}

		DictionaryIndexer indexer = new DictionaryIndexer();

		Path csv = indexer.nouns2csv(nouns);
		indexer.execute(csv);
	}

}
