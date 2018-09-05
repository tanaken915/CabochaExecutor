package parser.cabocha.option;

public class OutputFileOption extends FileOption {

	private static final String prefix = "--output=";
	private static final String shortenPrefix = "-o";


	public OutputFileOption(String first, String... more) {
		super(first, more);
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