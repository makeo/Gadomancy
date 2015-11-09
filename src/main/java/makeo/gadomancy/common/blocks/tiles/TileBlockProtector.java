package makeo.gadomancy.common.blocks.tiles;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.IEssentiaTransport;
import thaumcraft.common.tiles.TileJarFillable;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by makeo @ 08.11.2015 17:36
 */
public class TileBlockProtector extends TileJarFillable {
    private static final int UPDATE_TICKS = 20;
    private static final int MAX_RANGE = 10;
    private static final int SATURATION = 0;
    private static final Aspect ASPECT = Aspect.ORDER;

    private int range = 0;
    private int saturation = 0;
    private int count = 0;

    public TileBlockProtector() {
        maxAmount = 16;
        aspectFilter = ASPECT;
    }

    @Override
    public void updateEntity() {
        if (!protectors.contains(this)) {
            protectors.add(this);
        }

        if(!worldObj.isRemote) {
            if (++count % 5 == 0 && amount < maxAmount) {
                fillJar();
            }

            if (count % UPDATE_TICKS == 0) {
                if (saturation > 0) {
                    saturation--;
                    return;
                }

                if(takeFromContainer(ASPECT, 1)) {
                    if(range < MAX_RANGE) {
                        range++;
                    }
                    saturation = SATURATION;
                } else if(range > 0) {
                    range--;
                }
            }
        }
    }

    private void fillJar() {
        TileEntity te = ThaumcraftApiHelper.getConnectableTile(this.worldObj, this.xCoord, this.yCoord, this.zCoord, ForgeDirection.DOWN);
        if (te != null) {
            IEssentiaTransport ic = (IEssentiaTransport) te;
            if (!ic.canOutputTo(ForgeDirection.UP)) {
                return;
            }
            Aspect ta = null;
            if (this.aspectFilter != null) {
                ta = this.aspectFilter;
            } else if ((this.aspect != null) && (this.amount > 0)) {
                ta = this.aspect;
            } else if ((ic.getEssentiaAmount(ForgeDirection.UP) > 0) &&
                    (ic.getSuctionAmount(ForgeDirection.UP) < getSuctionAmount(ForgeDirection.DOWN)) && (getSuctionAmount(ForgeDirection.DOWN) >= ic.getMinimumSuction())) {
                ta = ic.getEssentiaType(ForgeDirection.UP);
            }
            if ((ta != null) && (ic.getSuctionAmount(ForgeDirection.UP) < getSuctionAmount(ForgeDirection.DOWN))) {
                addToContainer(ta, ic.takeEssentia(ta, 1, ForgeDirection.UP));
            }
        }
    }

    @Override
    public int getMinimumSuction() {
        return super.getMinimumSuction() * 2;
    }

    @Override
    public int getSuctionAmount(ForgeDirection loc) {
        return super.getSuctionAmount(loc) * 2;
    }

    @Override
    public boolean isConnectable(ForgeDirection face) {
        return face == ForgeDirection.DOWN;
    }

    @Override
    public boolean canInputFrom(ForgeDirection face) {
        return face == ForgeDirection.DOWN;
    }

    @Override
    public boolean canOutputTo(ForgeDirection face) {
        return face == ForgeDirection.DOWN;
    }

    private static List<TileBlockProtector> protectors = new ArrayList<TileBlockProtector>();

    public static boolean isSpotProtected(World world, double x, double y, double z) {
        for (int i = 0; i < protectors.size(); i++) {
            TileBlockProtector protector = protectors.get(i);
            if (protector.isInvalid()) {
                protectors.remove(i);
                i--;
            } else if (protector.worldObj.isRemote == world.isRemote
                    && isSpotProtected(protector, x, y, z)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isSpotProtected(TileBlockProtector tile, double x, double y, double z) {
        return tile.getDistanceFrom(x, y, z) < tile.range * tile.range;
    }
}
