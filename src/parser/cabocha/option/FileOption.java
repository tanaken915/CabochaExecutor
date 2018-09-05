package parser.cabocha.option;

import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class FileOption implements CommandOption {
	protected final Path file;
	
	public FileOption(String first, String... more) {
		this.file = Paths.get(first, more);
	}

	public Path getPath() {
		return file;
	}
	
}