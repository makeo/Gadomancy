package makeo.gadomancy.common.integration;

import makeo.gadomancy.common.Gadomancy;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;

/**
 * HellFirePvP@Admin
 * Date: 20.04.2016 / 00:43
 * on Gadomancy
 * IntegrationThaumicTinkerer
 */
public class IntegrationThaumicTinkerer extends IntegrationMod {

    private static Class infusedCropBlockClass, infusedCropTile;

    @Override
    public String getModId() {
        return "ThaumicTinkerer";
    }

    @Override
    public void doInit() {
        try {
            infusedCropBlockClass = Class.forName("thaumic.tinkerer.common.block.BlockInfusedGrain");
            infusedCropTile = Class.forName("thaumic.tinkerer.common.block.tile.TileInfusedGrain");
            if(infusedCropBlockClass != null && infusedCropTile != null) {
                Gadomancy.log.info("Hooked TTinkerer magic-crops");
            }
        } catch (Throwable tr) {}
    }

    public static boolean isCropBlock(Block block) {
        if(infusedCropBlockClass == null) return false;
        return block.getClass().equals(infusedCropBlockClass) || infusedCropBlockClass.isAssignableFrom(block.getClass());
    }

    public static boolean isCropTile(TileEntity te) {
        if(infusedCropTile == null) return false;
        return te.getClass().equals(infusedCropTile) || infusedCropTile.isAssignableFrom(te.getClass());
    }

}
