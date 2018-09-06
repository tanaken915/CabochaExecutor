package parser.cabocha.option;

public enum NamedEntityOption implements EnumerativeOption {
	NONUSE("0"),			// 固有表現解析を行わない
	USE_CONSTRAINT("1"),	// 文節の整合性を保ちつつ固有表現解析を行う
	USE_UNCONSTRAINT("2");	// 文節の整合性を保たずに固有表現解析を行う

	private static final String prefix = "--ne=";
	//private static final String shortenPrefix = "-n";

	private final String num_str;


	private NamedEntityOption(String num_str) {
		this.num_str = num_str;
	}


	@Override
	public String getNumberString() {
		return num_str;
	}

	@Override
	public String toOption() {
		return prefix + num_str;
	}

	@Override
	public String toString() {
		return toOption();
	}
}