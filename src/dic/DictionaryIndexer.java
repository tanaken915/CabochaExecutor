package dic;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import cabocha.Cabocha;
import process.AbstractProcessManager;

/**
 * 
 * @author tanabekentaro
 * 単語(名詞に限定)の集合を受け取り、活用形や読みを列挙した所定のCSV形式に変換する。
 * CSVからdicファイルへはMecabのコマンドを使う
 */
public class DictionaryIndexer extends AbstractProcessManager {

	static {
		Properties prop = new Properties();
		try (InputStream is = Cabocha.class.getClassLoader().getResourceAsStream("prop/property.xml")) {
			prop.loadFromXML(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/* ================================================== */
	/* ================== Constructor =================== */
	/* ================================================== */
	public DictionaryIndexer() {
	
	}
	
	public Path nouns2csv(Collection<String> nouns) {
		List<String> csv = nouns.stream()
				.map(DictionaryEntry::new)
				.peek(DictionaryEntry::setAsNoun)
				.map(DictionaryEntry::toCSV)
				.collect(Collectors.toList());
		Path p = Paths.get("");
		try {
			Files.write(p, csv);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return p;
	}
	
	public DictionaryEntry makesEntry(String noun) {
		DictionaryEntry de = new DictionaryEntry(noun);
		return de;
	}
}