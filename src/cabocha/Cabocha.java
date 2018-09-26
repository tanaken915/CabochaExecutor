package cabocha;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import cabocha.option.CommandOption;
import cabocha.option.enumerate.EnumerativeOption;
import cabocha.option.enumerate.NamedEntityOption;
import cabocha.option.enumerate.OutputFormatOption;
import cabocha.option.file.InputFileOption;
import cabocha.option.file.OutputFileOption;
import parser.ParserInterface;
import process.AbstractProcessManager;
import util.PlatformUtil;

public class Cabocha extends AbstractProcessManager implements ParserInterface {
	/* CaboChaの基本実行コマンド (必須) */
	// 不変なのでstatic
	private static final List<String> COMMAND;
	/** プロセスの待ち時間 */
	private static final int WAIT;
	/* 実行時に使うかもしれない一時ファイルのパス (必須) */
	/** CaboChaの入力ファイルの保存先. */
	private static final InputFileOption INPUT_TMPFILE;
	/** CaboChaの出力ファイルの保存先 */
	private static final OutputFileOption OUTPUT_TMPFILE;
	/* 設定ファイルから読み込んだデフォルトオプション */
	/* 設定するかは任意なので、Optionalでラップしている */
	private static final Optional<OutputFormatOption> OUTPUT_FORMAT_OPTION;
	private static final Optional<NamedEntityOption> NAMED_ENTITY_OPTION;
	private static final Optional<InputFileOption> INPUT_FILE_OPTION;
	private static final Optional<OutputFileOption> OUTPUT_FILE_OPTION;
	
	static {
		Properties prop = new Properties();
		try (InputStream is = Cabocha.class.getClassLoader().getResourceAsStream("prop/property.xml")) {
			prop.loadFromXML(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String key4os = PlatformUtil.isMac() ? "command-macos"
				: PlatformUtil.isWindows() ? "command-windows"
				: "";
		String val4wait = prop.getProperty("wait");
		String val4i_tmp = prop.getProperty("input-tmpfile");
		String val4o_tmp = prop.getProperty("output-tmpfile");
		String val4o_form = prop.getProperty("output-format");
		String val4ne = prop.getProperty("named-entity");
		String val4i_file = prop.getProperty("input-file");
		String val4o_file = prop.getProperty("output-file");
		
		COMMAND = Arrays.asList(prop.getProperty(key4os).split(" "));

		WAIT = Integer.valueOf(val4wait);
		
		INPUT_TMPFILE = new InputFileOption(val4i_tmp);
		OUTPUT_TMPFILE = new OutputFileOption(val4o_tmp);

		OUTPUT_FORMAT_OPTION = Optional.ofNullable(EnumerativeOption.valueOf(OutputFormatOption.class, val4o_form));
		NAMED_ENTITY_OPTION = Optional.ofNullable(EnumerativeOption.valueOf(NamedEntityOption.class, val4ne));
		INPUT_FILE_OPTION = Objects.nonNull(val4i_file)? Optional.of(new InputFileOption(val4i_file)) : Optional.empty();
		OUTPUT_FILE_OPTION = Objects.nonNull(val4o_file)? Optional.of(new OutputFileOption(val4o_file)) : Optional.empty();
	}

	/* インスタンスごとのオプション */
	private Optional<OutputFormatOption> output_format_option;
	private Optional<NamedEntityOption> named_entity_option;
	private Optional<InputFileOption> input_file_option;
	private Optional<OutputFileOption> output_file_option;


	/* ================================================== */
	/* =================== Constructor ================== */
	/* ================================================== */
	// オプション全指定
	public Cabocha(OutputFormatOption o_format, NamedEntityOption ne, InputFileOption i_file, OutputFileOption o_file) {
		this.output_format_option = Optional.ofNullable(o_format);
		this.named_entity_option = Optional.ofNullable(ne);
		this.input_file_option = Optional.ofNullable(i_file);
		this.output_file_option = Optional.ofNullable(o_file);
	}

	// オプションを指定しない場合、設定ファイル (/etc/cabochaexecrc) に基づく
	public Cabocha() {
		this.output_format_option = OUTPUT_FORMAT_OPTION;
		this.named_entity_option = NAMED_ENTITY_OPTION;
		this.input_file_option = INPUT_FILE_OPTION;
		this.output_file_option = OUTPUT_FILE_OPTION;
	}


	/* ================================================== */
	/* ================== Member Method ================= */
	/* ================================================== */
	private Stream<Optional<? extends CommandOption>> optionStream() {
		return Stream.of(output_format_option, named_entity_option, input_file_option, output_file_option);
	}
	private List<String> command() {
		Stream<String> commandStream = COMMAND.stream();
		Stream<String> optionStream = optionStream()
				.filter(Optional::isPresent)
				.map(Optional::get)
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
			Path path = makeTemporaryFile4Input(texts);	// 一旦ファイルに出力
			return parse(path);							// そのファイルを入力として解析
		}
	}

	@Override
	public List<String> parse(String text) {
		startNewProcess(command());					// プロセス開始
		writeToProcess(text);						// 入力待ちプロセスにテキスト入力
		List<String> result = readFromProcess();	// 結果を読み込む
		finishPresentProcess(1);					// プロセス終了
		return result;
	}

	@Override
	public List<String> parse(Path textPath) {
		// CaboChaの入力も出力もファイルになるよう，コマンドを用意
		// 入力ファイル指定のオプションを用意
		input_file_option = Optional.of(new InputFileOption(textPath.toString()));
		// 一時ファイルに出力するオプションを用意
		if (!output_file_option.isPresent())					// オプション指定がなければ
			output_file_option = Optional.of(OUTPUT_TMPFILE);	// 設定ファイルで指定した一時ファイルを使う
		startNewProcess(command());		// プロセス開始
		finishPresentProcess(WAIT);		// プロセス終了
		try {
			return Files.readAllLines(output_file_option.get().getPath(), StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Collections.emptyList();
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
		finishPresentProcess(1);						// プロセス終了
		return result;
	}


	/** 入力したいテキストを一時ファイルに出力 */
	private static Path makeTemporaryFile4Input(List<String> texts) {
		try {
			Files.createDirectories(INPUT_TMPFILE.getPath().getParent());
			return Files.write(INPUT_TMPFILE.getPath(), texts, StandardCharsets.UTF_8, StandardOpenOption.WRITE,
					StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
