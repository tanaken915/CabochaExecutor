package parser.cabocha.option;

public class InputFileOption extends FileOption {


	public InputFileOption(String first, String... more) {
		super(first, more);
	}


	@Override
	public String toOption() {
		return super.toString();
	}

	@Override
	public String toString() {
		return toOption();
	}
}