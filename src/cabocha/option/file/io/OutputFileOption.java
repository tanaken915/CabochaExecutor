package cabocha.option.file.io;

import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;

import cabocha.option.file.FileOption;

public class OutputFileOption extends FileOption {

	private static final String PREFIX = "--output=";
	private static final String SHORTEN_PREFIX = "-o";
	private static final String KEY = "output-file";

	public static Optional<OutputFileOption> newInstance(Path file) {
		if (Objects.isNull(file))
			return Optional.empty();
		if (file.toString().isEmpty())
			return Optional.empty();
		return Optional.of(new OutputFileOption(file));
	}	
	public OutputFileOption(Path file) {
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