package dic;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import cabocha.Cabocha;
import cabocha.option.CommandOption;
import cabocha.option.charset.CSVCharsetOption;
import cabocha.option.charset.DicCharsetOption;
import cabocha.option.file.dic.SystemDicDirOption;
import cabocha.option.file.dic.UserDicFileOption;
import cabocha.option.file.io.InputFileOption;
import cabocha.option.file.io.OutputFileOption;
import process.AbstractProcessManager;
import process.WaitTime;
import util.PlatformUtil;

/**
 * 
 * @author tanabekentaro
 * 単語(名詞に限定)の集合を受け取り、活用形や読みを列挙した所定のCSV形式に変換する。
 * CSVからdicファイルへはMecabのコマンドを使う
 */
public class DictionaryIndexer extends AbstractProcessManager {
	/* mecab-dict-indexの基本実行コマンド (必須) */
	// 不変なのでstatic
	private static final List<String> COMMAND;
	/** プロセスの待ち時間 */
	private static final WaitTime WAIT;
	
	private static final OutputFileOption OUTPUT_CSV;
	/* 設定ファイルから読み込んだデフォルトオプション */
	/* いずれも必須のオプション */
	private static final SystemDicDirOption SYSDIC_DIR_OPTION;
	private static final UserDicFileOption USRDIC_FILE_OPTION;
	private static final CSVCharsetOption CSV_CHARSET_OPTION;
	private static final DicCharsetOption DIC_CHARSET_OPTION;
	
	static {
		Properties prop = new Properties();
		try (InputStream is = Cabocha.class.getClassLoader().getResourceAsStream("prop/MeCabDictIndex-property.xml")) {
			prop.loadFromXML(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String val4command 	= prop.getProperty(
				PlatformUtil.isMac() ? "command-macos" :
				PlatformUtil.isWindows() ? "command-windows" : "");
		String val4wait 	= prop.getProperty("wait");
		String val4o_csv 	= prop.getProperty("tmp_csv");
		String val4sysdic 	= prop.getProperty("sysdic-dir");
		String val4usrdic 	= prop.getProperty("usrdic-file");
		String val4csvchar 	= prop.getProperty("csv-charset");
		String val4dicchar 	= prop.getProperty("dic-charset");
		
		COMMAND 				= Arrays.asList(val4command.split(" "));
		WAIT 					= new WaitTime(Long.valueOf(val4wait), TimeUnit.SECONDS);
		OUTPUT_CSV 				= OutputFileOption.newInstance(Paths.get(val4o_csv)).get();
		SYSDIC_DIR_OPTION 		= SystemDicDirOption.newInstance(Paths.get(val4sysdic)).get();
		USRDIC_FILE_OPTION 		= UserDicFileOption.newInstance(Paths.get(val4usrdic)).get();
		CSV_CHARSET_OPTION 		= CSVCharsetOption.newInstance(val4csvchar).get();
		DIC_CHARSET_OPTION 		= DicCharsetOption.newInstance(val4dicchar).get();
	}
	
	private SystemDicDirOption sysdic_dir_option;
	private UserDicFileOption usrdic_file_option;
	private CSVCharsetOption csv_charset_option;
	private DicCharsetOption dic_charset_option;
	private InputFileOption input_file_option;
	
	/* ================================================== */
	/* ================== Constructor =================== */
	/* ================================================== */
	public DictionaryIndexer() {
		this.sysdic_dir_option 	= SYSDIC_DIR_OPTION;
		this.usrdic_file_option = USRDIC_FILE_OPTION;
		this.csv_charset_option = CSV_CHARSET_OPTION;
		this.dic_charset_option = DIC_CHARSET_OPTION;
	}
	
	/* ================================================== */
	/* ================= Member Method ================== */
	/* ================================================== */
	private Stream<CommandOption> OptionsStream() {
		return Stream.of(sysdic_dir_option, usrdic_file_option, csv_charset_option, dic_charset_option, input_file_option);
	}
	private List<String> command() {
		Stream<String> commandStream = COMMAND.stream();
		Stream<String> optionStream = OptionsStream()
				.map(CommandOption::toOption);
		return Stream.concat(commandStream, optionStream).collect(Collectors.toList());
	}
	
	public void execute(Path csv) {
		this.input_file_option = new InputFileOption(csv);
		List<String> command = command();
		startNewProcess(command);
		readFromProcess().forEach(System.out::println);	//PRINT
		readErrorFromProcess().forEach(System.out::println);	//PRINT
		finishPresentProcess(WAIT);						// プロセス終了
	}
	
	
	public Path nouns2csv(Collection<String> nouns) {
		List<String> csv = nouns.stream()
				.map(DictionaryEntry::new)
				.peek(DictionaryEntry::setAsNoun)
				.map(DictionaryEntry::toCSV)
				.collect(Collectors.toList());
		try {
			Files.write(OUTPUT_CSV.getPath(), csv, StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return OUTPUT_CSV.getPath();
	}
	
	public DictionaryEntry makesEntry(String noun) {
		DictionaryEntry de = new DictionaryEntry(noun);
		return de;
	}
}