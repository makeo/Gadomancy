package makeo.gadomancy.client.events;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import makeo.gadomancy.common.registration.RegisteredBlocks;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.BlockCoordinates;
import thaumcraft.api.IArchitect;
import thaumcraft.api.wands.ItemFocusBasic;
import thaumcraft.client.lib.REHWandHandler;
import thaumcraft.common.items.wands.ItemWandCasting;

import java.util.ArrayList;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 13.10.2015 16:11
 */
public class RenderEventHandler {
    private static final REHWandHandler WAND_HANDLER = new REHWandHandler();
    private static final FakeArchitectItem ARCHITECT_ITEM = new FakeArchitectItem();

    @SubscribeEvent
    public void on(DrawBlockHighlightEvent e) {
        if(e.currentItem != null && e.currentItem.getItem() instanceof ItemWandCasting) {
            ItemFocusBasic focus = ((ItemWandCasting) e.currentItem.getItem()).getFocus(e.currentItem);
            if(focus == null || !(focus instanceof IArchitect)) {
                Block block = e.player.worldObj.getBlock(e.target.blockX, e.target.blockY, e.target.blockZ);
                if(block != null && block == RegisteredBlocks.blockArcaneDropper) {
                    ForgeDirection dir = ForgeDirection.getOrientation(e.player.worldObj.getBlockMetadata(e.target.blockX, e.target.blockY, e.target.blockZ) & 7);

                    ArrayList<BlockCoordinates> coords = new ArrayList<BlockCoordinates>();
                    for(int x = -1; x < 2; x++) {
                        for(int y = -1; y < 2; y++) {
                            for(int z = -1; z < 2; z++) {
                                coords.add(new BlockCoordinates(e.target.blockX + 2*dir.offsetX + x, e.target.blockY + 2*dir.offsetY + y, e.target.blockZ + 2*dir.offsetZ + z));
                            }
                        }
                    }
                    coords.add(new BlockCoordinates(e.target.blockX + dir.offsetX, e.target.blockY + dir.offsetY, e.target.blockZ + dir.offsetZ));

                    ARCHITECT_ITEM.setCoords(coords);

                    GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
                    WAND_HANDLER.handleArchitectOverlay(new ItemStack(ARCHITECT_ITEM), e, e.player.ticksExisted, e.target);
                    GL11.glPopAttrib();
                }
            }
        }
    }
}
