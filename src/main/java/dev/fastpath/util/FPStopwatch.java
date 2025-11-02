package dev.fastpath.util;

public final class FPStopwatch {
    private long start;
    private long last;

    public void start() { start = System.nanoTime(); }
    public void stop() { last = System.nanoTime() - start; }
    public double lastMs() { return last / 1_000_000.0; }
}