package cabocha.option.charset;

import cabocha.option.CommandOption;

public abstract class CharsetOption implements CommandOption {
	private String charsetName;
	
	public CharsetOption(String charset) {
		this.charsetName = charset;
	}

	@Override
	public String toString() {
		return charsetName;
	}	
}
