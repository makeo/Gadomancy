package makeo.gadomancy.client.renderers.tile;

import makeo.gadomancy.client.effect.EffectHandler;
import makeo.gadomancy.client.effect.fx.FXVortex;
import makeo.gadomancy.client.events.ClientHandler;
import makeo.gadomancy.client.models.ModelPackedCompressorBlock;
import makeo.gadomancy.client.models.ModelEssentiaCompressor;
import makeo.gadomancy.common.blocks.tiles.TileEssentiaCompressor;
import makeo.gadomancy.common.utils.MiscUtils;
import makeo.gadomancy.common.utils.SimpleResourceLocation;
import makeo.gadomancy.common.utils.Vector3;
import makeo.gadomancy.common.utils.world.fake.FakeWorld;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.fx.ParticleEngine;
import thaumcraft.client.lib.QuadHelper;
import thaumcraft.client.lib.UtilsFX;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

/**
 * HellFirePvP@Admin
 * Date: 22.04.2016 / 21:39
 * on Gadomancy
 * RenderTileEssentiaCompressor
 */
public class RenderTileEssentiaCompressor extends TileEntitySpecialRenderer {

    public static final ModelBase MODEL_ESSENTIA_COMPRESSOR = new ModelEssentiaCompressor();
    public static final ModelBase MODEL_PACKED_COMPRESSOR = new ModelPackedCompressorBlock();
    public static final SimpleResourceLocation COMPRESSOR_TEXTURE = new SimpleResourceLocation("models/essentia_compressor.png");
    public static final SimpleResourceLocation PACKED_COMPRESSOR_TEXTURE = new SimpleResourceLocation("blocks/block_packed_compressor.png");

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float partialTicks) {
        if(tileEntity == null || !(tileEntity instanceof TileEssentiaCompressor)) return;

        boolean isRenderedAsItem = false;
        if(tileEntity.getWorldObj() instanceof FakeWorld) { //LUL
            isRenderedAsItem = true;
        }

        if(!isRenderedAsItem && ((TileEssentiaCompressor) tileEntity).isMultiblockFormed()) {
            int yOffset = ((TileEssentiaCompressor) tileEntity).getMultiblockYIndex();
            GL11.glPushMatrix();
            GL11.glTranslated(x + 0.5, y - yOffset - 0.5, z + 0.5);
            bindTexture(COMPRESSOR_TEXTURE);
            GL11.glRotatef(180, 1, 0, 0);
            GL11.glTranslatef(0, -2F, 0);
            MODEL_ESSENTIA_COMPRESSOR.render(null, 0, 0, 0, 0, 0, 0.0625f);
            GL11.glRotatef(180, 1, 0, 0);
            MODEL_ESSENTIA_COMPRESSOR.render(null, 0, 0, 0, 0, 0, 0.0625f);
            GL11.glPopMatrix();

            GL11.glPushMatrix();
            if(yOffset == 1) { //The middle one. - well. the one where the blackhole is.
                renderBlackHoleEffect(tileEntity.xCoord + 0.5, tileEntity.yCoord + 0.5, tileEntity.zCoord + 0.5,
                        (TileEssentiaCompressor) tileEntity.getWorldObj().getTileEntity(tileEntity.xCoord, tileEntity.yCoord - 1, tileEntity.zCoord));
            }
            GL11.glPopMatrix();
        } else {
            GL11.glPushMatrix();
            GL11.glTranslated(x + 0.5, y - 0.5, z + 0.5);
            GL11.glRotatef(180, 1, 0, 0);
            if(isRenderedAsItem) {
                GL11.glTranslatef(0, -2.9F, 0);
            } else {
                GL11.glTranslatef(0, -2.877F, 0);
            }
            bindTexture(PACKED_COMPRESSOR_TEXTURE);
            MODEL_PACKED_COMPRESSOR.render(null, 0, 0, 0, 0, 0, 0.0625f);
            GL11.glPopMatrix();
        }

    }

    public static Map<ChunkCoordinates, FXVortex> ownedVortex = new HashMap<ChunkCoordinates, FXVortex>();

    private void renderBlackHoleEffect(double x, double y, double z, TileEssentiaCompressor te) {
        ChunkCoordinates cc = new ChunkCoordinates((int) x, (int) y, (int) z);
        FXVortex v;
        if (ownedVortex.containsKey(cc)) {
            v = ownedVortex.get(cc);
        } else {
            v = new FXVortex(x, y, z, te);
            ownedVortex.put(cc, v);
            v.registered = true;
            EffectHandler.getInstance().registerVortex(v);
        }
        if(!v.registered) {
            EffectHandler.getInstance().registerVortex(v);
        }
        v.notify(System.currentTimeMillis());
    }

}
