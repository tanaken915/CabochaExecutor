package util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

import dic.Immutable;

public abstract class Factory<E extends Immutable> {
	protected final Map<String, Set<E>> constantPool;
	
	/* ================================================== */
	/* =================== Constructor ================== */ 
	/* ================================================== */
	public Factory() {
		 constantPool = new HashMap<>();
	}
	
	
	protected Set<E> getPoolSet(String key) {
		return constantPool.computeIfAbsent(key, s -> new HashSet<>());
	}
	
	/** プールにすでに同等のインスタンスがあればそれを，無ければ{@code construction}から生成して返す. */
	protected E intern(String key, Supplier<? extends E> construction, Object... args) {
    	Set<E> poolSet = getPoolSet(key);
    	Optional<E> opt = poolSet.parallelStream()
				.filter(f -> f.equivalent(args))
				.findAny();
		E e = opt.orElseGet(construction);
		poolSet.add(e);
		return e;
  	}
	
}
