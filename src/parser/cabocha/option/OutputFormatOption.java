package parser.cabocha.option;

public enum OutputFormatOption implements EnumerativeOption {
	TREE("0"),				// 木構造で出力
	LATICE("1"),			// 格子状に並べて出力
	TREE_AND_LATTICE("2"),	// TREEとLATTICEを両方出力
	XML("3");				// XML形式で出力

	private static final String prefix = "--output-format=";
	private static final String shortenPrefix = "-f";

	private final String num_str;



	private OutputFormatOption(String num_str) {
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