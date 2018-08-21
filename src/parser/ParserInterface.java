package parser;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

public interface ParserInterface {

	/*** 自然言語文をParserに通し，出力結果をListに保管 ***/
	/** 入力: Path or String or List<String> **/
	/** 出力: List<String> **/	// Listの1要素=出力の1行
	/** 入力が自然言語文1文のみ */
	public List<String> parse(String nlText);
	/** 入力が自然言語文のList */
	public List<String> parse(List<String> nlTextList);
	/** 入力がテキストファイル */
	public List<String> parse(Path nlTextFilePath);

	/** 入力されたList<NL>が空だった場合の処理 */
	public default List<String> emptyInput() {
		System.err.println("Input List is Empty!!!");
		return Collections.emptyList();
	}
}