package cabocha.option.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import cabocha.option.CommandOption;

public abstract class FileOption implements CommandOption {
	protected final Path file;
	
	public FileOption(Path file) {
		this.file = file;
		try {
			if (Objects.nonNull(file.getParent()))
				Files.createDirectories(file.getParent());
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}

	public Path getPath() {
		return file;
	}
	
	@Override
	public String toString() {
		return file.toString();
	}
	
}