package cabocha.option.file;

import java.nio.file.Path;
import java.nio.file.Paths;

import cabocha.option.CommandOption;

public abstract class FileOption implements CommandOption {
	protected final Path file;
	
	public FileOption(String first, String... more) {
		this.file = Paths.get(first, more);
	}

	public Path getPath() {
		return file;
	}
	
	@Override
	public String toString() {
		return file.toString();
	}
	
}