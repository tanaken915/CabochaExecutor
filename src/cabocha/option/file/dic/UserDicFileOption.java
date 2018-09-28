package cabocha.option.file.dic;

import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;

import cabocha.option.file.FileOption;

public class UserDicFileOption extends FileOption {

	//private static final String PREFIX = "--undifined=";
	private static final String SHORTEN_PREFIX = "-u";
	private static final String KEY = "usrdic-file";

	public static Optional<UserDicFileOption> newInstance(Path file) {
		if (Objects.isNull(file))
			return Optional.empty();
		if (file.toString().isEmpty())
			return Optional.empty();
		return Optional.of(new UserDicFileOption(file));
	}	
	private UserDicFileOption(Path file) {
		super(file);
	}

	@Override
	public String toOption() {
		return SHORTEN_PREFIX + super.toString();
	}
	
	@Override
	public String toString() {
		return SHORTEN_PREFIX + super.toString();
	}
	@Override
	public String propertyKey() {
		return KEY;
	}
}