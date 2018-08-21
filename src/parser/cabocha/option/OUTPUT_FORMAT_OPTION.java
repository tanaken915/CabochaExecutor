package parser.cabocha.option;

public enum OUTPUT_FORMAT_OPTION implements Option{
	TREE("-f0"),
	LATICE("-f1"),
	TREE_AND_LATTICE("-f2"),
	XML("-f3");


	private final String option;

	private OUTPUT_FORMAT_OPTION(String option) {
		this.option = option;
	}


	@Override
	public String getOption() {
		return option;
	}
}