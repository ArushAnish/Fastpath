package dev.fastpath.core;

import java.nio.file.*;
import java.io.*;
import com.google.gson.*;

public final class FPConfig {
    private static final Path PATH = Paths.get("config", "fastpath.json");

    // Core toggles
    public static boolean enabled = true;
    public static int maxConcurrentRebuilds = Math.max(2, Runtime.getRuntime().availableProcessors() - 2);
    public static boolean aggressiveCulling = false;
    public static boolean overlayEnabled = true;

    // Rebuild control
    public static boolean shortCircuitCompile = false; // skip all compiles (debug/testing)
    public static boolean skipEmptySections = true;    // skip empty chunk sections

    // New: smarter skipping
    public static boolean skipLightOnly = true;        // skip geometry rebuild if only lighting changed

    // New: adaptive scheduling
    public static boolean adaptiveThreads = true;
    public static int minThreads = 1;
    public static int maxThreads = Math.max(2, Runtime.getRuntime().availableProcessors());
    public static double targetFps = 60.0;
    public static double frameTimeSmoothAlpha = 0.2;   // smoothing factor for FPS EMA

    // New: compile throttling
    public static int maxCompilesPerTick = 4;          // limit compiles per frame to smooth FPS

    // HUD diagnostics
    public static boolean hudShowAdvanced = true;

    public static void load() {
        try {
            if (Files.exists(PATH)) {
                String json = Files.readString(PATH);
                JsonObject obj = JsonParser.parseString(json).getAsJsonObject();

                enabled = obj.has("enabled") ? obj.get("enabled").getAsBoolean() : enabled;
                maxConcurrentRebuilds = obj.has("maxConcurrentRebuilds") ? obj.get("maxConcurrentRebuilds").getAsInt() : maxConcurrentRebuilds;
                aggressiveCulling = obj.has("aggressiveCulling") ? obj.get("aggressiveCulling").getAsBoolean() : aggressiveCulling;
                overlayEnabled = obj.has("overlayEnabled") ? obj.get("overlayEnabled").getAsBoolean() : overlayEnabled;

                shortCircuitCompile = obj.has("shortCircuitCompile") ? obj.get("shortCircuitCompile").getAsBoolean() : shortCircuitCompile;
                skipEmptySections = obj.has("skipEmptySections") ? obj.get("skipEmptySections").getAsBoolean() : skipEmptySections;
                skipLightOnly = obj.has("skipLightOnly") ? obj.get("skipLightOnly").getAsBoolean() : skipLightOnly;

                adaptiveThreads = obj.has("adaptiveThreads") ? obj.get("adaptiveThreads").getAsBoolean() : adaptiveThreads;
                minThreads = obj.has("minThreads") ? obj.get("minThreads").getAsInt() : minThreads;
                maxThreads = obj.has("maxThreads") ? obj.get("maxThreads").getAsInt() : maxThreads;
                targetFps = obj.has("targetFps") ? obj.get("targetFps").getAsDouble() : targetFps;
                frameTimeSmoothAlpha = obj.has("frameTimeSmoothAlpha") ? obj.get("frameTimeSmoothAlpha").getAsDouble() : frameTimeSmoothAlpha;

                maxCompilesPerTick = obj.has("maxCompilesPerTick") ? obj.get("maxCompilesPerTick").getAsInt() : maxCompilesPerTick;

                hudShowAdvanced = obj.has("hudShowAdvanced") ? obj.get("hudShowAdvanced").getAsBoolean() : hudShowAdvanced;
            } else {
                save();
            }
        } catch (Exception e) {
            System.err.println("[Fastpath] Failed to load config, using defaults: " + e);
        }
    }

    public static void save() {
        try {
            Files.createDirectories(PATH.getParent());
            JsonObject obj = new JsonObject();

            obj.addProperty("enabled", enabled);
            obj.addProperty("maxConcurrentRebuilds", maxConcurrentRebuilds);
            obj.addProperty("aggressiveCulling", aggressiveCulling);
            obj.addProperty("overlayEnabled", overlayEnabled);

            obj.addProperty("shortCircuitCompile", shortCircuitCompile);
            obj.addProperty("skipEmptySections", skipEmptySections);
            obj.addProperty("skipLightOnly", skipLightOnly);

            obj.addProperty("adaptiveThreads", adaptiveThreads);
            obj.addProperty("minThreads", minThreads);
            obj.addProperty("maxThreads", maxThreads);
            obj.addProperty("targetFps", targetFps);
            obj.addProperty("frameTimeSmoothAlpha", frameTimeSmoothAlpha);

            obj.addProperty("maxCompilesPerTick", maxCompilesPerTick);

            obj.addProperty("hudShowAdvanced", hudShowAdvanced);

            Files.writeString(PATH, new GsonBuilder().setPrettyPrinting().create().toJson(obj));
        } catch (Exception e) {
            System.err.println("[Fastpath] Failed to save config: " + e);
        }
    }
}