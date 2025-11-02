package dev.fastpath.integration;

import dev.fastpath.core.FPConfig;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class FastpathConfigScreen extends Screen {
    private final Screen parent;

    public FastpathConfigScreen(Screen parent) {
        super(Component.literal("Fastpath Config"));
        this.parent = parent;
    }

    // Helper to render boolean values as ON (green) / OFF (red)
    private Component booleanLabel(String name, boolean value) {
        if (value) {
            return Component.literal(name + ": ON")
                    .withStyle(style -> style.withColor(0x00FF00)); // green
        } else {
            return Component.literal(name + ": OFF")
                    .withStyle(style -> style.withColor(0xFF0000)); // red
        }
    }

    @Override
    protected void init() {
        int y = this.height / 4;

        // Enabled toggle
        this.addRenderableWidget(Button.builder(
                booleanLabel("Enabled", FPConfig.enabled),
                button -> {
                    FPConfig.enabled = !FPConfig.enabled;
                    FPConfig.save();
                    button.setMessage(booleanLabel("Enabled", FPConfig.enabled));
                }
        ).bounds(this.width / 2 - 100, y, 200, 20).build());

        y += 30;

        // Aggressive Culling toggle
        this.addRenderableWidget(Button.builder(
                booleanLabel("Aggressive Culling", FPConfig.aggressiveCulling),
                button -> {
                    FPConfig.aggressiveCulling = !FPConfig.aggressiveCulling;
                    FPConfig.save();
                    button.setMessage(booleanLabel("Aggressive Culling", FPConfig.aggressiveCulling));
                }
        ).bounds(this.width / 2 - 100, y, 200, 20).build());

        y += 30;

        // Overlay toggle
        this.addRenderableWidget(Button.builder(
                booleanLabel("Overlay", FPConfig.overlayEnabled),
                button -> {
                    FPConfig.overlayEnabled = !FPConfig.overlayEnabled;
                    FPConfig.save();
                    button.setMessage(booleanLabel("Overlay", FPConfig.overlayEnabled));
                }
        ).bounds(this.width / 2 - 100, y, 200, 20).build());

        y += 30;

        // Slider for maxConcurrentRebuilds (range 1–16)
        this.addRenderableWidget(new AbstractSliderButton(
                this.width / 2 - 100, y, 200, 20,
                Component.literal("Max Rebuilds: " + FPConfig.maxConcurrentRebuilds),
                (FPConfig.maxConcurrentRebuilds - 1) / 15.0
        ) {
            @Override
            protected void updateMessage() {
                this.setMessage(Component.literal("Max Rebuilds: " + FPConfig.maxConcurrentRebuilds));
            }

            @Override
            protected void applyValue() {
                FPConfig.maxConcurrentRebuilds = 1 + (int)(this.value * 15);
                FPConfig.save();
            }
        });

        y += 40;

        // Done button
        this.addRenderableWidget(Button.builder(
                Component.literal("Done"),
                button -> this.minecraft.setScreen(parent)
        ).bounds(this.width / 2 - 100, y, 200, 20).build());
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        // If your mappings have this helper, you can uncomment:
        // this.renderDirtBackground(guiGraphics);

        super.render(guiGraphics, mouseX, mouseY, delta);
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 20, 0xFFFFFF);
    }
}