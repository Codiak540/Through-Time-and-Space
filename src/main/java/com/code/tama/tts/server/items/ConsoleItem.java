/* (C) TAMA Studios 2025 */
package com.code.tama.tts.server.items;

import com.code.tama.tts.client.renderers.items.ConsoleItemRenderer;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

public class ConsoleItem extends BlockItem {
    public ConsoleItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return new ConsoleItemRenderer(
                        Minecraft.getInstance().getBlockEntityRenderDispatcher(),
                        Minecraft.getInstance().getEntityModels());
            }
        });
    }
}
