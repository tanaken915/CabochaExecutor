package cabocha.option;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.stream.Stream;

import cabocha.option.enumerate.NamedEntityOption;
import cabocha.option.enumerate.OutputFormatOption;
import cabocha.option.file.dic.SystemDicDirOption;
import cabocha.option.file.dic.UserDicFileOption;
import cabocha.option.file.io.InputFileOption;
import cabocha.option.file.io.OutputFileOption;

public class CabochaOptionManager {
	private Optional<OutputFormatOption> 	outputFormatOption;
	private Optional<NamedEntityOption> 	namedEntityOption;
	private Optional<InputFileOption> 		inputFileOption;
	private Optional<OutputFileOption> 		outputFileOption;
	private Optional<SystemDicDirOption> 	sysdicDirOption;
	private Optional<UserDicFileOption> 	usrdicFileOption;
	
	/* ================================================== */
	/* =================== Constructor ================== */
	/* ================================================== */
	public CabochaOptionManager(Properties prop) {
		this.outputFormatOption = OutputFormatOption.getInstance(prop.getProperty("output-format"));
		this.namedEntityOption 	= NamedEntityOption.getInstance(prop.getProperty("named-entity"));
		this.inputFileOption	= InputFileOption.newInstance(Paths.get(prop.getProperty("input-file")));
		this.outputFileOption 	= OutputFileOption.newInstance(Paths.get(prop.getProperty("output-file")));
		this.sysdicDirOption 	= SystemDicDirOption.newInstance(Paths.get(prop.getProperty("sysdic-dir")));
		this.usrdicFileOption 	= UserDicFileOption.newInstance(Paths.get(prop.getProperty("usrdic-file")));
	}
	
	/* ================================================== */
	/* ================== Member Method ================= */
	/* ================================================== */
	public Stream<CommandOption> stream() {
		return Stream.of(outputFormatOption, namedEntityOption, 
				inputFileOption, outputFileOption, 
				sysdicDirOption, usrdicFileOption)
				.filter(Optional::isPresent)
				.map(Optional::get);
	}
	
	/**
	 * {@code OutputFileOption}で指定したファイルを読み込む.
	 * @return 読み込んだファイルの文字列リスト
	 */
	public List<String> readFromOutputOptionFile() {
		try {
			return Files.readAllLines(outputFileOption.get().getPath(), StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Collections.emptyList();
	}

	public boolean outputFormatOptionIsEmpty() {
		return ! outputFormatOption.isPresent();
	}
	public boolean namedEntityOptionIsEmpty() {
		return ! namedEntityOption.isPresent();
	}
	public boolean inputFileOptionIsEmpty() {
		return ! inputFileOption.isPresent();
	}
	public boolean outputFileOptionIsEmpty() {
		return ! outputFileOption.isPresent();
	}
	public boolean sysdicDirOptionIsEmpty() {
		return ! sysdicDirOption.isPresent();
	}
	public boolean usrdicFileOptionIsEmpty() {
		return ! usrdicFileOption.isPresent();
	}
	
	/* ================================================== */
	/* ===================== Setter ===================== */
	/* ================================================== */
	public void setOutputFormatOption(OutputFormatOption outputFormatOption) {
		this.outputFormatOption = Optional.of(outputFormatOption);
	}

	public void setNamedEntityOption(NamedEntityOption namedEntityOption) {
		this.namedEntityOption = Optional.of(namedEntityOption);
	}

	public void setInputFileOption(InputFileOption inputFileOption) {
		this.inputFileOption = Optional.of(inputFileOption);
	}

	public void setOutputFileOption(OutputFileOption outputFileOption) {
		this.outputFileOption = Optional.of(outputFileOption);
	}

	public void setSysdicDirOption(SystemDicDirOption sysdicDirOption) {
		this.sysdicDirOption = Optional.of(sysdicDirOption);
	}

	public void setUsrdicFileOption(UserDicFileOption usrdicFileOption) {
		this.usrdicFileOption = Optional.of(usrdicFileOption);
	}
}