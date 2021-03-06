package cabocha.option.file.dic;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;

import cabocha.option.file.FileOption;

public class SystemDicDirOption extends FileOption {

	private static final String PREFIX = "--mecab-dicdir=";
	// cabochaで使えることは保証されているが、mecab-dict-indexでは不明なので
	private static final String SHORTEN_PREFIX = "-d";
	private static final String KEY = "sysdic-dir";

	public static Optional<SystemDicDirOption> newInstance(Path file) {
		if (Objects.isNull(file))
			return Optional.empty();
		if (file.toString().isEmpty())
			return Optional.empty();
		if (Files.notExists(file))	// 入力ファイルなので存在もチェック
			return Optional.empty();
		return Optional.of(new SystemDicDirOption(file));
	}	
	public SystemDicDirOption(Path file) {
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