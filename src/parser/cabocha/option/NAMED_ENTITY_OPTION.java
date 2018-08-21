package parser.cabocha.option;

public enum NAMED_ENTITY_OPTION implements Option{
	NONUSE("-n0"),
	USE_CONSTRAINT("-n1"),
	USE_UNCONSTRAINT("-n2");


	private final String option;

	private NAMED_ENTITY_OPTION(String option) {
			this.option = option;
	}


	@Override
	public String getOption() {
		return option;
	}

}