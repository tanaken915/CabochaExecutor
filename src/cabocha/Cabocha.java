package cabocha;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import cabocha.option.CabochaOptionManager;
import cabocha.option.CommandOption;
import cabocha.option.file.io.InputFileOption;
import cabocha.option.file.io.OutputFileOption;
import parser.ParserInterface;
import process.AbstractProcessManager;
import process.WaitTime;
import util.PlatformUtil;

public class Cabocha extends AbstractProcessManager implements ParserInterface {
	private static final String DEF_PROP_PATH = "prop/CaboCha-property.xml";
	/** デフォルトの設定 */
	private static final Properties DEFAULT_PROPERTY;
	
	/** CaboChaの基本実行コマンド (必須) */
	private static final List<String> COMMAND;
	/** プロセスの待ち時間 */
	private static final WaitTime WAIT;
	/* メソッドによっては使う一時ファイルのパス (必須) */
	/** CaboChaの一時入力ファイルの保存先. */
	private static final Path TMPFILE4INPUT;
	/** CaboChaの一時出力ファイルの保存先 */
	private static final Path TMPFILE4OUTPUT;
	
	static {
		DEFAULT_PROPERTY = new Properties();
		try (InputStream is = Cabocha.class.getClassLoader().getResourceAsStream(DEF_PROP_PATH)) {
			DEFAULT_PROPERTY.loadFromXML(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String val4command 	= DEFAULT_PROPERTY.getProperty(
				PlatformUtil.isMac() ? "command-macos" :
				PlatformUtil.isWindows() ? "command-windows" : "");
		String val4wait 	= DEFAULT_PROPERTY.getProperty("wait");
		String val4i_tmp 	= DEFAULT_PROPERTY.getProperty("input-tmpfile");
		String val4o_tmp 	= DEFAULT_PROPERTY.getProperty("output-tmpfile");
		
		COMMAND 				= Arrays.asList(val4command.split(" "));
		WAIT 					= new WaitTime(Long.valueOf(val4wait), TimeUnit.SECONDS);
		TMPFILE4INPUT 			= Paths.get(val4i_tmp);
		TMPFILE4OUTPUT 			= Paths.get(val4o_tmp);
	}

	/* インスタンスごとのオプション */
	private CabochaOptionManager options;
	
	/* ================================================== */
	/* =================== Constructor ================== */
	/* ================================================== */
	// 設定ファイルに基づいてオプションを設定する
	public Cabocha() {
		this.options = new CabochaOptionManager(DEFAULT_PROPERTY);
	}
	public Cabocha(Properties prop) {
		this.options = new CabochaOptionManager(prop);
	}

	/* ================================================== */
	/* ================== Member Method ================= */
	/* ================================================== */
	private List<String> command() {
		Stream<String> commandStream = COMMAND.stream();
		Stream<String> optionStream = options.stream()
				.map(CommandOption::toOption);
		return Stream.concat(commandStream, optionStream).collect(Collectors.toList());
	}

	/* ================================================== */
	/* ========= AbstractProcessManager's Method ======== */
	/* ================================================== */
	// nothing

	/* ================================================== */
	/* ============ ParserInterface's Method ============ */
	/* ================================================== */
	@Override
	public List<String> parse(List<String> texts) {
		if (texts.isEmpty())
			return Collections.emptyList();
		else if (texts.size() == 1)
			return parse(texts.get(0));
		else if (texts.size() < 5)						// 境界値は適当
			return passContinualArguments(texts);		// 標準入力を繰り返す手法を試したいだけ
		else {
			Path path = makeTemporaryFile4Input(texts, TMPFILE4INPUT);	// 一旦ファイルに出力
			return parse(path);							// そのファイルを入力として解析
		}
	}

	@Override
	public List<String> parse(String text) {
		startNewProcess(command());					// プロセス開始
		writeToProcess(text);						// 入力待ちプロセスにテキスト入力
		List<String> result = readFromProcess();	// 結果を読み込む
		finishPresentProcess(WAIT);					// プロセス終了
		return result;
	}

	@Override
	public List<String> parse(Path textPath) {
		// CaboChaの入力も出力もファイルになるよう，コマンドを用意
		// 入力ファイル指定のオプションを用意
		options.setInputFileOption(new InputFileOption(textPath));
		
		// 一時ファイルに出力するオプションを用意
		if (options.outputFileOptionIsEmpty())	// オプション指定がなければ設定ファイルで指定した一時ファイルを使う
			options.setOutputFileOption(new OutputFileOption(TMPFILE4OUTPUT));
		
		startNewProcess(command());		// プロセス開始
		finishPresentProcess(WAIT);		// プロセス終了
		return options.readFromOutputOptionFile();
	}

	/**
	 * プロセスへの入力を繰り返し、出力をまとめて得る. 多分，数が多いと使えない.
	 * @param texts
	 * @return 解析結果の文字列リスト
	 */
	public List<String> passContinualArguments(List<String> texts) {
		startNewProcess(command());
		writeAllToProcess(texts);
		List<String> result = readFromProcess();		// 結果を読み込む
		finishPresentProcess(WAIT);						// プロセス終了
		return result;
	}


	/** 入力したいテキストを一時ファイルに出力 */
	private static Path makeTemporaryFile4Input(List<String> texts, Path tmpFile) {
		try {
			if (Objects.nonNull(tmpFile.getParent()))
				Files.createDirectories(tmpFile.getParent());
			return Files.write(tmpFile, texts, StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}