package dev.fastpath;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

public class FastpathMixinPlugin implements IMixinConfigPlugin {
    private static boolean enabled = true;

    static {
        try {
            Path path = Path.of("config", "fastpath.json");
            if (Files.exists(path)) {
                String json = Files.readString(path);
                JsonObject obj = JsonParser.parseString(json).getAsJsonObject();
                enabled = obj.has("enabled") && obj.get("enabled").getAsBoolean();
            }
        } catch (IOException e) {
            System.err.println("[Fastpath] Could not read config, defaulting enabled=true");
        }
    }

    @Override
    public void onLoad(String mixinPackage) {}

    @Override
    public String getRefMapperConfig() { return null; }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return enabled;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}

    @Override
    public List<String> getMixins() { return null; }

    @Override
    public void preApply(String targetClassName,
                         ClassNode targetClass,
                         String mixinClassName,
                         IMixinInfo mixinInfo) {}

    @Override
    public void postApply(String targetClassName,
                          ClassNode targetClass,
                          String mixinClassName,
                          IMixinInfo mixinInfo) {}
}