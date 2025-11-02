package dev.fastpath.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class FPLog {
    private static final Logger LOG = LogManager.getLogger("Fastpath");
    public static void info(String s) { LOG.info(s); }
    public static void warn(String s) { LOG.warn(s); }
    public static void error(String s, Throwable t) { LOG.error(s, t); }
}