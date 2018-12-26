package language.pos;

import java.util.List;

public interface Concatable<C> {

	C concat(C other);
	
	static <V extends Concatable<V>> V join(List<V> values) {
		return values.stream().reduce((v1, v2) -> v1.concat(v2)).get();
	}

}
