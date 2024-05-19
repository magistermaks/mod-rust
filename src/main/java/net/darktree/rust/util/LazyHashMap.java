package net.darktree.rust.util;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class LazyHashMap <K, V> extends HashMap<K, V> {

	protected Function<K, V> fallback;

	public LazyHashMap(Function<K, V> fallback) {
		this.fallback = fallback;
	}

	@Override
	@SuppressWarnings("unchecked")
	public V get(Object key) {
		return computeIfAbsent((K) key, fallback);
	}

	@Deprecated
	public <N> Map<K, N> map(Supplier<Map<K, N>> maps, Function<V, N> mapper) {
		return ContainerUtil.remap(this, maps, mapper);
	}

}