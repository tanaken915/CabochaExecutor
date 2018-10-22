package dic;

import java.util.Arrays;

public interface Immutable {
	/** コンストラクタの引数をObject配列にする */
	Object[] initArgs();
	
	default boolean equivalent(Object... initArgs) {
		return Arrays.equals(initArgs(), initArgs);
	}
}
