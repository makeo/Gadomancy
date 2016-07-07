package makeo.gadomancy.client.events;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import makeo.gadomancy.api.GadomancyApi;
import makeo.gadomancy.api.golems.cores.AdditionalGolemCore;
import makeo.gadomancy.client.gui.GuiResearchRecipeAuraEffects;
import makeo.gadomancy.client.util.ExtendedTypeDisplayManager;
import makeo.gadomancy.client.util.FamiliarHandlerClient;
import makeo.gadomancy.client.util.MultiTickEffectDispatcher;
import makeo.gadomancy.common.Gadomancy;
import makeo.gadomancy.common.blocks.tiles.TileExtendedNode;
import makeo.gadomancy.common.blocks.tiles.TileExtendedNodeJar;
import makeo.gadomancy.common.data.DataAchromatic;
import makeo.gadomancy.common.data.SyncDataHolder;
import makeo.gadomancy.common.registration.RegisteredBlocks;
import makeo.gadomancy.common.utils.Injector;
import makeo.gadomancy.common.utils.MiscUtils;
import makeo.gadomancy.common.utils.NBTHelper;
import makeo.gadomancy.common.utils.Vector3;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.resources.SimpleResource;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import net.minecraftforge.client.model.obj.WavefrontObject;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.BlockCoordinates;
import thaumcraft.api.IArchitect;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.api.wands.ItemFocusBasic;
import thaumcraft.client.gui.GuiGolem;
import thaumcraft.client.gui.GuiResearchRecipe;
import thaumcraft.client.lib.REHWandHandler;
import thaumcraft.common.entities.golems.EntityGolemBase;
import thaumcraft.common.items.relics.ItemThaumometer;
import thaumcraft.common.items.wands.ItemWandCasting;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;

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

    private Object oldGolemblurb = null;
    private int blurbId;

    @SubscribeEvent
    public void on(GuiScreenEvent.DrawScreenEvent.Pre e) {
        if(e.gui instanceof GuiGolem) {
            GuiGolem gui = (GuiGolem) e.gui;
            EntityGolemBase golem = new Injector(gui, GuiGolem.class).getField("golem");
            if(golem != null) {
                AdditionalGolemCore core = GadomancyApi.getAdditionalGolemCore(golem);
                if(core != null) {
                    blurbId = core.getBaseCore();
                    String key = "golemblurb." + blurbId + ".text";
                    oldGolemblurb = ResourceReloadListener.languageList.get(key);
                    ResourceReloadListener.languageList.put(key, StatCollector.translateToLocal(core.getUnlocalizedGuiText()));
                }
            }
        }
    }

    @SubscribeEvent
    public void on(GuiScreenEvent.DrawScreenEvent.Post e) {
        if(oldGolemblurb != null) {
            String key = "golemblurb." + blurbId + ".text";
            ResourceReloadListener.languageList.put(key, oldGolemblurb);
            oldGolemblurb = null;
        }
    }

    @SubscribeEvent
    public void on(DrawBlockHighlightEvent e) {
        if(e.currentItem == null) return;
        if(e.currentItem.getItem() instanceof ItemWandCasting) {
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
        } else if(e.currentItem.getItem() instanceof ItemThaumometer) {
            if(e.target.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK) return;
            int blockX = e.target.blockX;
            int blockY = e.target.blockY;
            int blockZ = e.target.blockZ;
            if(Minecraft.getMinecraft().gameSettings.thirdPersonView == 0) {
                TileEntity tile = e.player.worldObj.getTileEntity(blockX, blockY, blockZ);
                if(tile instanceof TileExtendedNode) {
                    TileExtendedNode node = (TileExtendedNode) tile;
                    if(node.getExtendedNodeType() == null) return;
                    ExtendedTypeDisplayManager.notifyDisplayTick(node.getId(), node.getNodeType(), node.getExtendedNodeType());
                } else if(tile instanceof TileExtendedNodeJar) {
                    TileExtendedNodeJar nodeJar = (TileExtendedNodeJar) tile;
                    if(nodeJar.getExtendedNodeType() == null) return;
                    ExtendedTypeDisplayManager.notifyDisplayTick(nodeJar.getId(), nodeJar.getNodeType(), nodeJar.getExtendedNodeType());
                }
            }
        }
    }

    @SubscribeEvent
    public void guiOpen(GuiOpenEvent event) {
        if(event.gui != null && event.gui instanceof GuiResearchRecipe) {
            GuiResearchRecipe gui = (GuiResearchRecipe) event.gui;
            ResearchItem research = new Injector(gui, GuiResearchRecipe.class).getField("research");
            if(research.key.equals(Gadomancy.MODID.toUpperCase() + ".AURA_EFFECTS") && !(gui instanceof GuiResearchRecipeAuraEffects)) {
                event.gui = GuiResearchRecipeAuraEffects.create(gui);
            }
        }
    }

    @SubscribeEvent
    public void worldRenderEvent(RenderWorldLastEvent event) {
        ExtendedTypeDisplayManager.notifyRenderTick();
        MultiTickEffectDispatcher.notifyRenderTick(Minecraft.getMinecraft().theWorld, event.partialTicks);
    }

    private EntityPlayer current = null;
    private ItemStack[] armor = null;

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void renderEntityPre(RenderLivingEvent.Pre event) {
        if(event.entity instanceof EntityPlayer) {
            EntityPlayer p = (EntityPlayer) event.entity;
            if(((DataAchromatic)SyncDataHolder.getDataClient("AchromaticData")).isAchromatic((EntityPlayer) event.entity)) {
                current = p;
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.15F);
                GL11.glDepthMask(false);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GL11.glAlphaFunc(GL11.GL_GREATER, 0.003921569F);
            }

            armor = p.inventory.armorInventory;
            p.inventory.armorInventory = new ItemStack[armor.length];
            System.arraycopy(armor, 0, p.inventory.armorInventory, 0, armor.length);

            boolean changed = false;
            for(int i = 0; i < armor.length; i++) {
                if(armor[i] != null && NBTHelper.hasPersistentData(armor[i])) {
                    NBTTagCompound compound = NBTHelper.getPersistentData(armor[i]);
                    if(compound.hasKey("disguise")) {
                        NBTBase base = compound.getTag("disguise");
                        if(base instanceof NBTTagCompound) {
                            p.inventory.armorInventory[i] = ItemStack.loadItemStackFromNBT((NBTTagCompound) base);
                        } else {
                            p.inventory.armorInventory[i] = null;
                        }
                        changed = true;
                    }
                }
            }

            if(!changed) {
                p.inventory.armorInventory = armor;
                armor = null;
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST, receiveCanceled = true)
    public void renderPost(RenderLivingEvent.Post event) {
        if(event.entity instanceof EntityPlayer) {
            EntityPlayer p = (EntityPlayer) event.entity;
            if(armor != null) {
                p.inventory.armorInventory = armor;
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST, receiveCanceled = true)
    public void onSetArmor(RenderPlayerEvent.SetArmorModel event) {
        if(event.entityPlayer == current) {
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
            GL11.glDepthMask(true);
        }
    }

    static {
        ResourceLocation mod = new ResourceLocation(Gadomancy.MODID.toLowerCase() + new String(new byte[] {58, 116, 101, 120, 116, 117, 114, 101, 115, 47, 109, 111, 100, 101, 108, 115, 47, 109, 111, 100, 101, 108, 65, 115, 115, 101, 99, 46, 111, 98, 106}, Charset.forName("UTF-8")));
        IModelCustom buf;
        try {
            buf = new WavefrontObject("gadomancy:wRender", new GZIPInputStream(Minecraft.getMinecraft().getResourceManager().getResource(mod).getInputStream()));
        } catch (Exception exc) {
            //shush.
            buf = null;
        }
        obj = buf;
    }

    private static final IModelCustom obj;
    private static final ResourceLocation tex = new ResourceLocation(new String(new byte[] {103, 97, 100, 111, 109, 97, 110, 99, 121, 58, 116, 101, 120, 116, 117, 114, 101, 115, 47, 109, 105, 115, 99, 47, 116, 101, 120, 87, 46, 112, 110, 103}, Charset.forName("UTF-8")));
    private static int dList = -1;
    @SubscribeEvent
    public void onRender(RenderPlayerEvent.Specials.Post event) {
        if(event.entityPlayer == null) return;
        if(!FMLCommonHandler.instance().getMinecraftServerInstance().isServerInOnlineMode()) return;
        if(obj == null) return;
        if(!MiscUtils.isMisunderstood(event.entityPlayer)) return;

        GL11.glColor4f(1f, 1f, 1f, 1f);

        GL11.glPushMatrix();
        Minecraft.getMinecraft().renderEngine.bindTexture(tex);
        boolean f = event.entityPlayer.capabilities.isFlying;
        double ma = f ? 15 : 5;
        double r = (ma * (Math.abs((ClientHandler.ticks % 80) - 40) / 40D)) +
                ((65 - ma) * Math.max(0, Math.min(1, new Vector3(event.entityPlayer.motionX, 0, event.entityPlayer.motionZ).length())));
        GL11.glScaled(0.07, 0.07, 0.07);
        GL11.glRotatef(180, 0, 0, 1);
        GL11.glTranslated(0, -12.7, 0.7 - (((float) (r / ma)) * (f ? 0.5D : 0.2D)));
        if(dList == -1) {
            dList = GLAllocation.generateDisplayLists(2);
            GL11.glNewList(dList, GL11.GL_COMPILE);
            obj.renderOnly("wR");
            GL11.glEndList();
            GL11.glNewList(dList + 1, GL11.GL_COMPILE);
            obj.renderOnly("wL");
            GL11.glEndList();
        }
        GL11.glPushMatrix();
        GL11.glRotated(20D + r, 0, -1, 0);
        GL11.glCallList(dList);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glRotated(20D + r, 0, 1, 0);
        GL11.glCallList(dList + 1);
        GL11.glPopMatrix();
        GL11.glPopMatrix();
    }

    @SubscribeEvent
    public void playerRenderEvent(RenderPlayerEvent.Post renderEvent) {
        FamiliarHandlerClient.playerRenderEvent(renderEvent.entityPlayer, renderEvent.partialRenderTick);
    }
}
