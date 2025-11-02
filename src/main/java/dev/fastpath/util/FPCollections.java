package dev.fastpath.util;

import java.util.Collection;

public final class FPCollections {
    public static <T> void drain(Collection<T> c) { c.clear(); }
}