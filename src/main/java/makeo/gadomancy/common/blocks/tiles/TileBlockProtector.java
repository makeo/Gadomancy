package makeo.gadomancy.common.blocks.tiles;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.IEssentiaTransport;
import thaumcraft.common.Thaumcraft;
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
    private static final int UPDATE_TICKS = 15;
    private static final int MAX_RANGE = 15;
    private static final Aspect ASPECT = Aspect.ORDER;

    private int range = 0;
    private int saturation = 0;
    private int count = 0;

    public TileBlockProtector() {
        maxAmount = 8;
        aspectFilter = ASPECT;
    }

    public int getCurrentRange() {
        return range;
    }

    public int getPowerLevel() {
        return Math.min(worldObj.getStrongestIndirectPower(xCoord, yCoord, zCoord), MAX_RANGE);
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("ProtectSaturation", saturation);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        saturation = compound.getInteger("ProtectSaturation");
    }

    @Override
    public void readCustomNBT(NBTTagCompound compound) {
        super.readCustomNBT(compound);
        aspectFilter = ASPECT;

        int oldRange = range;
        range = compound.getInteger("ProtectRange");

        if(worldObj != null && worldObj.isRemote && oldRange != range) {
            worldObj.updateLightByType(EnumSkyBlock.Block, xCoord, yCoord, zCoord);
        }
    }

    @Override
    public void writeCustomNBT(NBTTagCompound compound) {
        super.writeCustomNBT(compound);
        compound.removeTag("AspectFilter");
        compound.setInteger("ProtectRange", range);
    }

    @Override
    public void updateEntity() {
        if (!protectors.contains(this)) {
            protectors.add(this);
        }

        if (!worldObj.isRemote) {
            if (++count % 5 == 0 && amount < maxAmount) {
                fillJar();
            }

            if (count % UPDATE_TICKS == 0) {
                if(range == 0) {
                    saturation = 0;
                }

                if (saturation > 0) {
                    saturation--;
                    return;
                }

                int powerLevel = getPowerLevel();
                boolean executeDecrease = range > powerLevel;

                if(range <= powerLevel && powerLevel > 0) {
                    executeDecrease = true;
                    if (takeFromContainer(ASPECT, 1)) {

                        if (range < powerLevel) {
                            range++;
                            markDirty();
                            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);

                            saturation = 16 - range;
                        }
                        executeDecrease = false;
                    }
                }

                if (executeDecrease && range > 0) {
                    range--;

                    markDirty();
                    worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                }
            }
        } else if(range > 0) {
            float sizeMod = 1 - (range / 15f);
            if (this.worldObj.rand.nextInt(9 - Thaumcraft.proxy.particleCount(2)) == 0) {
                Thaumcraft.proxy.wispFX3(this.worldObj, this.xCoord + 0.5F, this.yCoord + 0.68F, this.zCoord + 0.5F, this.xCoord + 0.3F + this.worldObj.rand.nextFloat() * 0.4F, this.yCoord + 0.68F, this.zCoord + 0.3F + this.worldObj.rand.nextFloat() * 0.4F, 0.3F - (0.15f*sizeMod), 6, true, -0.025F);
            }
            if (this.worldObj.rand.nextInt(15 - Thaumcraft.proxy.particleCount(4)) == 0) {
                Thaumcraft.proxy.wispFX3(this.worldObj, this.xCoord + 0.5F, this.yCoord + 0.68F, this.zCoord + 0.5F, this.xCoord + 0.4F + this.worldObj.rand.nextFloat() * 0.2F, this.yCoord + 0.68F, this.zCoord + 0.4F + this.worldObj.rand.nextFloat() * 0.2F, 0.2F - (0.15f*sizeMod), 6, true, -0.02F);
            }
        }

        if(range > 0) {
            for(EntityLivingBase entity : (List<EntityCreeper>)worldObj.getEntitiesWithinAABB(EntityLivingBase.class, getProtectedAABB())) {
                if(entity instanceof EntityCreeper) {
                    ((EntityCreeper) entity).timeSinceIgnited = 0;
                }

                if(worldObj.isRemote && !(entity instanceof EntityPlayer)) {
                    spawnEntityParticles(entity);
                }
            }
        }
    }

    private void spawnEntityParticles(EntityLivingBase entity) {
        AxisAlignedBB cube = entity.boundingBox;
        if(cube != null && worldObj.rand.nextInt(20) == 0) {
            double posX = worldObj.rand.nextDouble() * (cube.maxX - cube.minX) + cube.minX;
            double posY = worldObj.rand.nextDouble() * (cube.maxX - cube.minX) + cube.minY;
            double posZ = worldObj.rand.nextDouble() * (cube.maxX - cube.minX) + cube.minZ;

            switch (worldObj.rand.nextInt(5)) {
                case 0: posX = cube.maxX; break;
                case 1: posY = cube.maxY; break;
                case 2: posZ = cube.maxZ; break;
                case 3: posX = cube.minX; break;
                case 4: posZ = cube.minZ; break;
            }

            Thaumcraft.proxy.wispFX3(this.worldObj, posX, posY, posZ, posX + this.worldObj.rand.nextFloat() * 0.2F, posY, posZ + this.worldObj.rand.nextFloat() * 0.2F, 0.2F, 6, true, -0.02F);
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
        return false;
    }

    private static List<TileBlockProtector> protectors = new ArrayList<TileBlockProtector>();

    public static boolean isSpotProtected(World world, final double x, final double y, final double z) {
        return isSpotProtected(world, new ProtectionHelper() {
            @Override
            public boolean checkProtection(TileBlockProtector tile) {
                return isSpotProtected(tile, x, y, z);
            }
        });
    }

    public static boolean isSpotProtected(World world, final Entity entity) {
        return isSpotProtected(world, new ProtectionHelper() {
            @Override
            public boolean checkProtection(TileBlockProtector tile) {
                return isSpotProtected(tile, entity);
            }
        });
    }

    public static boolean isSpotProtected(World world, ProtectionHelper helper) {
        for (int i = 0; i < protectors.size(); i++) {
            TileBlockProtector protector = protectors.get(i);
            if (protector.isInvalid()) {
                protectors.remove(i);
                i--;
            } else if (protector.worldObj.isRemote == world.isRemote
                    && helper.checkProtection(protector)) {
                return true;
            }
        }
        return false;
    }

    private static interface ProtectionHelper {
        boolean checkProtection(TileBlockProtector tile);
    }

    private static boolean isSpotProtected(TileBlockProtector tile, Entity entity) {
        AxisAlignedBB entityAABB = entity.boundingBox;
        if (entityAABB != null) {
            return tile.getProtectedAABB().intersectsWith(entityAABB.addCoord(entity.posX, entity.posY, entity.posZ));
        }
        return isSpotProtected(tile, entity.posX, entity.posY, entity.posZ);
    }

    private AxisAlignedBB getProtectedAABB() {
        return AxisAlignedBB.getBoundingBox(xCoord - range, yCoord - range, zCoord - range, xCoord + range, yCoord + range, zCoord + range);
    }

    private static boolean isSpotProtected(TileBlockProtector tile, double x, double y, double z) {
        return tile.getProtectedAABB().isVecInside(Vec3.createVectorHelper(x, y, z));
    }
}
