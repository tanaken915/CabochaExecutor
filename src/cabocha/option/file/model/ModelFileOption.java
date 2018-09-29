package cabocha.option.file.model;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;

import cabocha.option.file.FileOption;

public class ModelFileOption  extends FileOption {

	private static final String PREFIX = "-m";
	private static final String SHORTEN_PREFIX = "-m";
	private static final String KEY = "model-file";

	public static Optional<ModelFileOption> newInstance(Path file) {
		if (Objects.isNull(file))
			return Optional.empty();
		if (file.toString().isEmpty())
			return Optional.empty();
		if (Files.notExists(file))	// 入力ファイルなので存在もチェック
			return Optional.empty();
		return Optional.of(new ModelFileOption(file));
	}	
	public ModelFileOption(Path file) {
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