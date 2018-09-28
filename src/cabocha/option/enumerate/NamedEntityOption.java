package cabocha.option.enumerate;

import java.util.Optional;

public enum NamedEntityOption implements EnumerativeOption {
	NONUSE("0"),			// 固有表現解析を行わない
	USE_CONSTRAINT("1"),	// 文節の整合性を保ちつつ固有表現解析を行う
	USE_UNCONSTRAINT("2");	// 文節の整合性を保たずに固有表現解析を行う

	private static final String PREFIX = "--ne=";
	private static final String SHORTEN_PREFIX = "-n";
	private static final String KEY = "named-entity";
	
	private final String num_str;

	public static Optional<NamedEntityOption> getInstance(String num_str) {
		return Optional.ofNullable(
				EnumerativeOption.valueOf(NamedEntityOption.class, num_str));
	}
	private NamedEntityOption(String num_str) {
		this.num_str = num_str;
	}

	@Override
	public String getNumberString() {
		return num_str;
	}

	@Override
	public String toOption() {
		return SHORTEN_PREFIX + num_str;
	}

	@Override
	public String toString() {
		return PREFIX + super.toString();
	}
	@Override
	public String propertyKey() {
		return KEY;
	}
}