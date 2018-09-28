package cabocha;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import cabocha.option.CommandOption;
import cabocha.option.enumerate.NamedEntityOption;
import cabocha.option.enumerate.OutputFormatOption;
import cabocha.option.file.dic.SystemDicDirOption;
import cabocha.option.file.dic.UserDicFileOption;
import cabocha.option.file.io.InputFileOption;
import cabocha.option.file.io.OutputFileOption;
import parser.ParserInterface;
import process.AbstractProcessManager;
import process.WaitTime;
import util.PlatformUtil;

public class Cabocha extends AbstractProcessManager implements ParserInterface {
	/* CaboChaの基本実行コマンド (必須) */
	// 不変なのでstatic
	private static final List<String> COMMAND;
	/** プロセスの待ち時間 */
	private static final WaitTime WAIT;
	/* 実行時に使うかもしれない一時ファイルのパス (必須) */
	/** CaboChaの一時入力ファイルの保存先. */
	private static final Path INPUT_TMPFILE;
	/** CaboChaの一時出力ファイルの保存先 */
	private static final Path OUTPUT_TMPFILE;
	/* 設定ファイルから読み込んだデフォルトオプション */
	/* 設定するかは任意なので、Optionalでラップしている */
	private static final Optional<OutputFormatOption> OUTPUT_FORMAT_OPTION;
	private static final Optional<NamedEntityOption> NAMED_ENTITY_OPTION;
	private static final Optional<InputFileOption> INPUT_FILE_OPTION;
	private static final Optional<OutputFileOption> OUTPUT_FILE_OPTION;
	private static final Optional<SystemDicDirOption> SYSDIC_DIR_OPTION;
	private static final Optional<UserDicFileOption> USRDIC_FILE_OPTION;
	
	static {
		Properties prop = new Properties();
		try (InputStream is = Cabocha.class.getClassLoader().getResourceAsStream("prop/CaboCha-property.xml")) {
			prop.loadFromXML(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String val4command 	= prop.getProperty(
				PlatformUtil.isMac() ? "command-macos" :
				PlatformUtil.isWindows() ? "command-windows" : "");
		String val4wait 	= prop.getProperty("wait");
		String val4i_tmp 	= prop.getProperty("input-tmpfile");
		String val4o_tmp 	= prop.getProperty("output-tmpfile");
		String val4o_form 	= prop.getProperty("output-format");
		String val4ne 		= prop.getProperty("named-entity");
		String val4i_file 	= prop.getProperty("input-file");
		String val4o_file 	= prop.getProperty("output-file");
		String val4sysdic 	= prop.getProperty("sysdic-dir");
		String val4usrdic 	= prop.getProperty("usrdic-file");

		COMMAND 				= Arrays.asList(val4command.split(" "));
		WAIT 					= new WaitTime(Long.valueOf(val4wait), TimeUnit.SECONDS);
		INPUT_TMPFILE 			= Paths.get(val4i_tmp);
		OUTPUT_TMPFILE 			= Paths.get(val4o_tmp);
		OUTPUT_FORMAT_OPTION 	= OutputFormatOption.getInstance(val4o_form);
		NAMED_ENTITY_OPTION 	= NamedEntityOption.getInstance(val4ne);
		INPUT_FILE_OPTION 		= InputFileOption.newInstance(Paths.get(val4i_file));
		OUTPUT_FILE_OPTION 		= OutputFileOption.newInstance(Paths.get(val4o_file));
		SYSDIC_DIR_OPTION 		= SystemDicDirOption.newInstance(Paths.get(val4sysdic));
		USRDIC_FILE_OPTION 		= UserDicFileOption.newInstance(Paths.get(val4usrdic));
	}

	/* インスタンスごとのオプション */
	private Optional<OutputFormatOption> 	output_format_option;
	private Optional<NamedEntityOption> 	named_entity_option;
	private Optional<InputFileOption> 		input_file_option;
	private Optional<OutputFileOption> 		output_file_option;
	private Optional<SystemDicDirOption> 	sysdic_dir_option;
	private Optional<UserDicFileOption> 	usrdic_file_option;


	/* ================================================== */
	/* =================== Constructor ================== */
	/* ================================================== */
	// 設定ファイルに基づいてオプションを設定する
	public Cabocha() {
		this.output_format_option 	= OUTPUT_FORMAT_OPTION;
		this.named_entity_option 	= NAMED_ENTITY_OPTION;
		this.input_file_option 		= INPUT_FILE_OPTION;
		this.output_file_option 	= OUTPUT_FILE_OPTION;
		this.sysdic_dir_option 		= SYSDIC_DIR_OPTION;
		this.usrdic_file_option 	= USRDIC_FILE_OPTION;
	}


	/* ================================================== */
	/* ================== Member Method ================= */
	/* ================================================== */
	private Stream<CommandOption> effectiveOptionsStream() {
		return Stream.of(output_format_option, named_entity_option, input_file_option, output_file_option, sysdic_dir_option, usrdic_file_option)
				.filter(Optional::isPresent)
				.map(Optional::get);
	}
	private List<String> command() {
		Stream<String> commandStream = COMMAND.stream();
		Stream<String> optionStream = effectiveOptionsStream()
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
		finishPresentProcess(WAIT);					// プロセス終了
		return result;
	}

	@Override
	public List<String> parse(Path textPath) {
		// CaboChaの入力も出力もファイルになるよう，コマンドを用意
		// 入力ファイル指定のオプションを用意
		input_file_option = InputFileOption.newInstance(textPath);
		// 一時ファイルに出力するオプションを用意
		if (!output_file_option.isPresent())	// オプション指定がなければ設定ファイルで指定した一時ファイルを使う
			output_file_option = OutputFileOption.newInstance(OUTPUT_TMPFILE);
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
		finishPresentProcess(WAIT);						// プロセス終了
		return result;
	}


	/** 入力したいテキストを一時ファイルに出力 */
	private static Path makeTemporaryFile4Input(List<String> texts) {
		try {
			Files.createDirectories(INPUT_TMPFILE.getParent());
			return Files.write(INPUT_TMPFILE, texts, StandardCharsets.UTF_8, StandardOpenOption.WRITE,
					StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/* ================================================== */
	/* ===================== Setter ===================== */
	/* ================================================== */
	public void setOutput_format_option(Optional<OutputFormatOption> output_format_option) {
		this.output_format_option = output_format_option;
	}
	public void setNamed_entity_option(Optional<NamedEntityOption> named_entity_option) {
		this.named_entity_option = named_entity_option;
	}
	public void setInput_file_option(Optional<InputFileOption> input_file_option) {
		this.input_file_option = input_file_option;
	}
	public void setOutput_file_option(Optional<OutputFileOption> output_file_option) {
		this.output_file_option = output_file_option;
	}
	public void setSysdic_dir_option(Optional<SystemDicDirOption> sysdic_dir_option) {
		this.sysdic_dir_option = sysdic_dir_option;
	}
	public void setUsrdic_file_option(Optional<UserDicFileOption> usrdic_file_option) {
		this.usrdic_file_option = usrdic_file_option;
	}
}