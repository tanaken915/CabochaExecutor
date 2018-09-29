package cabocha.option.charset;

import java.util.Objects;
import java.util.Optional;

public class DicCharsetOption extends CharsetOption {

	private static final String PREFIX = "-t";
	private static final String SHORTEN_PREFIX = "-t";
	private static final String KEY = "dic-charset";

	public static Optional<DicCharsetOption> newInstance(String charsetName) {
		if (Objects.isNull(charsetName))
			return Optional.empty();
		if (charsetName.isEmpty())
			return Optional.empty();
		return Optional.of(new DicCharsetOption(charsetName));
	}

	private DicCharsetOption(String charsetName) {
			super(charsetName);
		}

	@Override
	public String toOption() {
		return SHORTEN_PREFIX + super.toString();
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