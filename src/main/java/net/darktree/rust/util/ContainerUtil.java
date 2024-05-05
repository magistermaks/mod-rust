package net.darktree.rust.util;

import com.google.common.collect.ImmutableMap;

import java.util.function.Function;
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

}
