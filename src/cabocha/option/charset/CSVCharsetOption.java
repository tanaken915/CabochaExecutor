package cabocha.option.charset;

import java.util.Objects;
import java.util.Optional;

public class CSVCharsetOption extends CharsetOption {

	private static final String PREFIX = "-f";
	private static final String SHORTEN_PREFIX = "-f";
	private static final String KEY = "csv-charset";

	public static Optional<CSVCharsetOption> newInstance(String charsetName) {
		if (Objects.isNull(charsetName))
			return Optional.empty();
		if (charsetName.isEmpty())
			return Optional.empty();
		return Optional.of(new CSVCharsetOption(charsetName));
	}	
	private CSVCharsetOption(String charsetName) {
		super(charsetName);
	}

	
	@Override
	public String toOption() {
		return SHORTEN_PREFIX + super.toString();
	}

	@Override
	public String toString() {
		return SHORTEN_PREFIX + super.toString();
	}
	
	@Override
	public String propertyKey() {
		return KEY;
	}
}