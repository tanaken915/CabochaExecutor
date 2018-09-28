package cabocha.option.file.io;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;

import cabocha.option.file.FileOption;

public class InputFileOption extends FileOption {

	private static final String PREFIX = "";
	private static final String SHORTEN_PREFIX = "";
	private static final String KEY = "input-file";

	public static Optional<InputFileOption> newInstance(Path file) {
		if (Objects.isNull(file))
			return Optional.empty();
		if (file.toString().isEmpty())
			return Optional.empty();
		if (Files.notExists(file))	// 入力ファイルなので存在もチェック
			return Optional.empty();
		return Optional.of(new InputFileOption(file));
	}	
	public InputFileOption(Path file) {
		super(file);
	}

	@Override
	public String toOption() {
		return SHORTEN_PREFIX + super.toString();
	}

	@Override
	public String toString() {
		return PREFIX + super.toString();
	}
	
	@Override
	public String propertyKey() {
		return KEY;
	}
}