package makeo.gadomancy.client.renderers.tile;

import makeo.gadomancy.client.events.ClientTickHandler;
import makeo.gadomancy.client.models.ModelAuraPylon;
import makeo.gadomancy.client.models.ModelAuraPylonTop;
import makeo.gadomancy.common.blocks.tiles.TileAuraPylon;
import makeo.gadomancy.common.blocks.tiles.TileAuraPylonTop;
import makeo.gadomancy.common.utils.SimpleResourceLocation;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.renderers.entity.RenderPrimalOrb;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by HellFirePvP @ 12.11.2015 21:33
 */
public class RenderTileAuraPylon extends TileEntitySpecialRenderer {

    public static final ModelBase MODEL_AURA_PYLON = new ModelAuraPylon();
    public static final ModelBase MODEL_AURA_PYLON_TOP = new ModelAuraPylonTop();
    public static final SimpleResourceLocation PYLON_TEXTURE = new SimpleResourceLocation("models/aurapylon.png");

    public static RenderPrimalOrb primalOrbRender;
    public static TileAuraPylonTop.DummyEntity dummyEntity = new TileAuraPylonTop.DummyEntity();

    public void renderTileEntity(TileEntity tile, double x, double y, double z, float partTicks) {
        GL11.glPushMatrix();
        GL11.glTranslated(x + 0.5, y - 0.5, z + 0.5);

        bindTexture(PYLON_TEXTURE);

        GL11.glPushMatrix();
        for(int i = 0; i < 4; i++) {
            if(tile instanceof TileAuraPylonTop) {
                GL11.glPushMatrix();
                GL11.glRotatef(180, 1, 0, 0);
                GL11.glTranslatef(0, -2F, 0);
                MODEL_AURA_PYLON_TOP.render(null, 0, 0, 0, 0, 0, 0.0625f);
                GL11.glPopMatrix();
            } else {
                MODEL_AURA_PYLON.render(null, 0, 0, 0, 0, 0, 0.0625f);
            }

            GL11.glRotatef(90, 0, 1, 0);
        }
        GL11.glPopMatrix();
        GL11.glPopMatrix();

        if(tile instanceof TileAuraPylonTop && ((TileAuraPylonTop) tile).shouldRenderEffect()) {
            dummyEntity.ticksExisted = ClientTickHandler.ticks;
            primalOrbRender.renderEntityAt(dummyEntity, x + 0.5, y + 0.7, z + 0.5, 0, partTicks);
        }
    }

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float partTicks) {
        renderTileEntity(tileEntity, x, y, z, partTicks);
    }

    static {
        primalOrbRender = new RenderPrimalOrb();
        primalOrbRender.setRenderManager(RenderManager.instance);
    }

}
