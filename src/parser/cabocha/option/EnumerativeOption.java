package parser.cabocha.option;
import java.util.Arrays;

public interface EnumerativeOption extends CommandOption {

	String getNumberString();

	/**
     * 第一引数に指定されたEnumの中から、第2引数のコード値と一致するものを取得する。
     *
     * @param target	取得したいEnumのクラス
     * @param num_str	検索する数値の文字列表現
     * @param <E>   	CodeInterfaceを実装したEnumクラス
     * @return E
     */
    public static <E extends Enum<?> & EnumerativeOption> E valueOf(Class<E> target, String num_str) {
        return Arrays.stream(target.getEnumConstants())
                .filter(data -> data.getNumberString().equals(num_str))
                .findFirst()
                .orElse(null);
    }
}