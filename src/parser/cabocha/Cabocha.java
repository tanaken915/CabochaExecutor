package parser.cabocha;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import parser.ParserInterface;
import parser.cabocha.option.NAMED_ENTITY_OPTION;
import parser.cabocha.option.OUTPUT_FORMAT_OPTION;
import process.AbstractProcessManager;
import util.PlatformUtil;

public class Cabocha extends AbstractProcessManager implements ParserInterface {
	/* CaboChaの基本実行コマンド */
	private static final List<String> COMMAND4MACOS = new LinkedList<>(Arrays.asList("/usr/local/bin/cabocha"));
	private static final List<String> COMMAND4WINDOWS = new LinkedList<>(Arrays.asList("cmd", "/c", "cabocha"));
	/* CaboChaのオプション */
	private static final String OPTION_TREE					= "-f0"; 		// 木構造で出力
	private static final String OPTION_LATTICE				= "-f1"; 		// 格子状に並べて出力
	private static final String OPTION_TREE_AND_LATTICE		= "-f2"; 		// TREEとLATTICEを両方出力
	private static final String OPTION_XML					= "-f3";		// XML形式で出力

	private static final String OPTION_NO_NE				= "-n0";		// 固有表現解析を行わない
	private static final String OPTION_NE_CONSTRAINT		= "-n1";		// 文節の整合性を保ちつつ固有表現解析を行う
	private static final String OPTION_NE_UNCONSTRAINT		= "-n2";		// 文節の整合性を保たずに固有表現解析を行う

	private static final String OPTION_OUTPUT2FILE			= "--output=";	// CaboChaの結果をファイルに書き出す


	/** CaboChaの入力ファイルの保存先. */
	private static Path INPUT_TXTFILE_PATH = Paths.get("tmp/parserIO/CaboChaInput.txt");
	/** CaboChaの出力ファイルの保存先 */
	private static Path OUTPUT_TXTFILE_PATH = Paths.get("tmp/parserIO/CaboChaOutput.txt");

	private List<String> options;

	/* ================================================== */
	/* ==========          Constructor         ========== */
	/* ================================================== */
	/* デフォルトではオプション(-f1, -n1)でセッティング */
	public Cabocha() {
		this(OPTION_LATTICE, OPTION_NE_CONSTRAINT);
	}
	/* オプションをリストで渡すことも可能 */
	public Cabocha(String... options) {
		command = PlatformUtil.isMac() ?		COMMAND4MACOS
				: PlatformUtil.isWindows() ?	COMMAND4WINDOWS
				: null;		// mac, windows以外のOSは実装予定なし
		command.addAll(Arrays.asList(options));
	}
	public Cabocha(OUTPUT_FORMAT_OPTION of, NAMED_ENTITY_OPTION ne) {

	}

	/* ================================================== */
	/* ==========        Member  Method        ========== */
	/* ================================================== */
	public void setOutputFormatType() {

	}


	/* ================================================== */
	/* ======== AbstractProcessManager's  Method ======== */
	/* ================================================== */
	// nothing

	/* ================================================== */
	/* ==========   ParserInterface's Method   ========== */
	/* ================================================== */
	@Override
	public List<String> parse(String text) {
		startProcess();								// プロセス開始
		writeInput2Process(text.toString());		// 入力待ちプロセスにテキスト入力
		List<String> result = readProcessResult();	// 結果を読み込む
		finishProcess();							// プロセス終了
		return result;
	}
	@Override
	public List<String> parse(List<String> texts) {
		Path path = output_ParserInput(texts);		// 一旦ファイルに出力
		return parse(path);							// そのファイルを入力として解析
	}
	@Override
	public List<String> parse(Path textPath) {
		// CaboChaの入力も出力もファイルになるよう，コマンドを用意
		command.add(textPath.toString());				// 入力テキストのファイル名
		command.add(OPTION_OUTPUT2FILE + OUTPUT_TXTFILE_PATH.toString());	// ファイルに出力するコマンドを追加
		startProcess();										// プロセス開始
		finishProcess();									// プロセス終了
		try {
			return Files.readAllLines(OUTPUT_TXTFILE_PATH, StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new ArrayList<>();
	}


	/**
	 * プロセスへの入力を繰り返し出力をまとめて得る. 多分，数が多いと使えない.
	 * @param texts
	 * @return 解析結果の文字列リスト
	 */
	public List<String> passContinualArguments(List<String> texts) {
		startProcess();
		PrintWriter pw = new PrintWriter(new BufferedWriter(
				new OutputStreamWriter(process.getOutputStream(), StandardCharsets.UTF_8)));
		texts.forEach(text -> pw.println(text.toString()));
		pw.close();
		List<String> result = readProcessResult();		// 結果を読み込む
		finishProcess();								// プロセス終了
		return result;
	}



	/** 入力したいテキスト(List<NL>)を一旦ファイル(parserInput)に出力 **/
	/* List<NL>のサイズが2以上ならこれらを呼び出し、executeParser(Path)に渡される */
	private static Path output_ParserInput(List<String> texts) {
		// List<NL>からList<String>へ
		try {
			return Files.write(INPUT_TXTFILE_PATH, texts,
					StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}