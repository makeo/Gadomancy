package makeo.gadomancy.client.renderers.tile;

import makeo.gadomancy.common.data.config.ModConfig;
import net.minecraft.tileentity.TileEntity;
import thaumcraft.client.renderers.tile.TileEldritchObeliskRenderer;
import thaumcraft.common.config.Config;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by makeo @ 10.11.2015 21:59
 */
public class RenderTileObelisk extends TileEldritchObeliskRenderer {
    @Override
    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float f) {
        if(te.getWorldObj().provider.dimensionId == ModConfig.dimOuterId) {
            int old = Config.dimensionOuterId;
            Config.dimensionOuterId = ModConfig.dimOuterId;
            super.renderTileEntityAt(te, x, y, z, f);
            Config.dimensionOuterId = old;
        } else {
            super.renderTileEntityAt(te, x, y, z, f);
        }
    }
}
