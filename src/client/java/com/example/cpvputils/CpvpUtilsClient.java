package com.example.cpvputils;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.block.Blocks;
import net.minecraft.block.RespawnAnchorBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;

public class CpvpUtilsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        HudElementRegistry.attachElementBefore(
            VanillaHudElements.CHAT,
            Identifier.of("cpvputils", "anchor_hud"),
            (context, tickCounter) -> {
                MinecraftClient mc = MinecraftClient.getInstance();
                if (mc.player == null || mc.world == null || mc.options.hudHidden) return;

                int glow = 0;
                for (int i = 0; i < mc.player.getInventory().size(); i++) {
                    ItemStack s = mc.player.getInventory().getStack(i);
                    if (s.isOf(Items.GLOWSTONE)) glow += s.getCount();
                }
                int y = mc.getWindow().getScaledHeight() / 2 + 20;
                int x = mc.getWindow().getScaledWidth() / 2 + 12;
                context.drawTextWithShadow(mc.textRenderer, "Glowstone: " + glow, x, y, 0xFFFFD966);

                if (mc.crosshairTarget instanceof BlockHitResult hit) {
                    var state = mc.world.getBlockState(hit.getBlockPos());
                    if (state.isOf(Blocks.RESPAWN_ANCHOR)) {
                        int c = state.get(RespawnAnchorBlock.CHARGES);
                        int color = c == 0 ? 0xFFAAAAAA : (c < 4 ? 0xFFFFAA00 : 0xFF55FF55);
                        context.drawTextWithShadow(mc.textRenderer, "Anchor: " + c + "/4", x, y + 12, color);
                    }
                }
            });
    }
}
