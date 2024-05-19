package net.darktree.rust.util;

import com.google.common.collect.ImmutableMap;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ContainerUtil {

	/**
	 * Create an immutable map where every key is an enum value and values are
	 * created with the given function.
	 */
	public static <T extends Enum<T>, V> ImmutableMap<T, V> enumMapOf(Class<T> clazz, Function<T, V> supplier) {
		return ImmutableMap.copyOf(Stream.of(clazz.getEnumConstants()).collect(Collectors.toMap(Function.identity(), supplier)));
	}

	/**
	 * Remaps the given java Map by running each map value
	 * though the given mapping functions
	 */
	public static <K, A, B> Map<K, B> remap(Map<K, A> source, Supplier<Map<K, B>> maps, Function<A, B> mapper) {
		Map<K, B> map = maps.get();

		for (Map.Entry<K, A> entry : source.entrySet()) {
			map.put(entry.getKey(), mapper.apply(entry.getValue()));
		}

		return map;
	}

}
