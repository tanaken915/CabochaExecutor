package parser.cabocha.option;

import java.io.IOException;
import java.nio.file.Files;

public class OutputFileOption extends FileOption {

	private static final String prefix = "--output=";
	//private static final String shortenPrefix = "-o";


	public OutputFileOption(String first, String... more) {
		super(first, more);
		try {
			Files.createDirectories(getPath().getParent());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	@Override
	public String toOption() {
		return prefix + super.toString();
	}
	
	@Override
	public String toString() {
		return toOption();
	}
}