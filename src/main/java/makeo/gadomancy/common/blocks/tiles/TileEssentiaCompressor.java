package makeo.gadomancy.common.blocks.tiles;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import makeo.gadomancy.client.events.ClientHandler;
import makeo.gadomancy.common.Gadomancy;
import makeo.gadomancy.common.data.config.ModConfig;
import makeo.gadomancy.common.network.packets.PacketTCNodeBolt;
import makeo.gadomancy.common.registration.RegisteredBlocks;
import makeo.gadomancy.common.utils.ExplosionHelper;
import makeo.gadomancy.common.utils.Injector;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.WorldCoordinates;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectContainer;
import thaumcraft.api.aspects.IAspectSource;
import thaumcraft.api.aspects.IEssentiaContainerItem;
import thaumcraft.api.aspects.IEssentiaTransport;
import thaumcraft.client.fx.ParticleEngine;
import thaumcraft.client.fx.bolt.FXLightningBolt;
import thaumcraft.client.fx.particles.FXEssentiaTrail;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.events.EssentiaHandler;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.fx.PacketFXEssentiaSource;
import thaumcraft.common.tiles.TileJarFillable;
import thaumcraft.common.tiles.TileMirrorEssentia;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * HellFirePvP@Admin
 * Date: 22.04.2016 / 21:42
 * on Gadomancy
 * TileEssentiaCompressor
 */
public class TileEssentiaCompressor extends SynchronizedTileEntity implements IAspectSource, IEssentiaTransport {

    private static Injector injEssentiaHandler = new Injector(EssentiaHandler.class);
    private static int multiblockIDCounter = 0;

    //Standard multiblock stuff
    private boolean isMasterTile = true; //For debugging
    private int multiblockYIndex = -1, multiblockId = -1;

    private boolean isMultiblockPresent = false;

    private AspectList al = new AspectList();
    private int ticksExisted = 0;
    private boolean prevFound = false;

    @Override
    public void updateEntity() {
        super.updateEntity();
        ticksExisted++;

        if(!worldObj.isRemote) {
            if(isMultiblockFormed()) {
                checkMultiblock();
            }

            if(!isMultiblockFormed()) return;

            if (isMasterTile() && (prevFound || (ticksExisted % 100) == 0)) {
                List<WorldCoordinates> coords = searchAndGetSources();
                if (coords == null || coords.isEmpty()) {
                    prevFound = false;
                } else {
                    prevFound = searchForEssentia(coords);
                }
            }
        } else {
            if(isMasterTile() && isMultiblockFormed()) {
                playLightningEffects();
                playVortexEffects();
                if(al.visSize() > 0) {
                    playEssentiaEffects();
                }
            }
        }

        if(isMasterTile() && isMultiblockFormed()) {
            vortexEntities();
        }
    }

    private boolean searchForEssentia(List<WorldCoordinates> coordinates) {
        if(ticksExisted % 10 != 0) return prevFound;
        for (Aspect a : Aspect.aspects.values()) {
            if (doDrain(a, coordinates)) {
                al.add(a, 1);
                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                markDirty();
                return true;
            }
        }
        return false;
    }

    private boolean doDrain(Aspect a, List<WorldCoordinates> coordinates) {
        for (WorldCoordinates coordinate : coordinates) {
            TileEntity sourceTile = worldObj.getTileEntity(coordinate.x, coordinate.y, coordinate.z);
            if ((sourceTile == null) || (!(sourceTile instanceof IAspectSource))) {
                continue;
            }
            if(sourceTile instanceof TileEssentiaCompressor) continue;
            IAspectSource as = (IAspectSource)sourceTile;
            if (as.takeFromContainer(a, 1)) {
                PacketHandler.INSTANCE.sendToAllAround(new PacketFXEssentiaSource(xCoord, yCoord + 1, zCoord,
                        (byte)(xCoord - coordinate.x), (byte)(yCoord - coordinate.y + 1), (byte)(zCoord - coordinate.z),
                        a.getColor()), new NetworkRegistry.TargetPoint(worldObj.provider.dimensionId, xCoord, yCoord + 1, zCoord, 32.0D));
                return true;
            }
        }
        return false;
    }

    float tr = 1.0F;
    float tri = 0.0F;
    float tg = 1.0F;
    float tgi = 0.0F;
    float tb = 1.0F;
    float tbi = 0.0F;
    public float cr = 1.0F;
    public float cg = 1.0F;
    public float cb = 1.0F;
    public Aspect displayAspect = null;

    //Thanks @TileEssentiaReservoir :P
    @SideOnly(Side.CLIENT)
    private void playEssentiaEffects() {
        if ((ClientHandler.ticks % 20 == 0) && (al.size() > 0)) {
            this.displayAspect = al.getAspects()[(ClientHandler.ticks / 20 % al.size())];
            Color c = new Color(this.displayAspect.getColor());
            this.tr = (c.getRed() / 255.0F);
            this.tg = (c.getGreen() / 255.0F);
            this.tb = (c.getBlue() / 255.0F);
            this.tri = ((this.cr - this.tr) / 20.0F);
            this.tgi = ((this.cg - this.tg) / 20.0F);
            this.tbi = ((this.cb - this.tb) / 20.0F);
        }
        if (this.displayAspect == null) {
            this.tr = (this.tg = this.tb = 1.0F);
            this.tri = (this.tgi = this.tbi = 0.0F);
        } else {
            this.cr -= this.tri;
            this.cg -= this.tgi;
            this.cb -= this.tbi;
        }
        int count = 1;
        if(cr < 0) cr = 0;
        if(cg < 0) cg = 0;
        if(cb < 0) cb = 0;
        FXEssentiaTrail essentiaTrail = new FXEssentiaTrail(worldObj, xCoord + 0.5, yCoord + 0.4, zCoord + 0.5, xCoord + 0.5, yCoord + 1.5, zCoord + 0.5, count, new Color(cr, cg, cb).getRGB(), 0.8F);
        essentiaTrail.noClip = true;
        essentiaTrail.motionY = (0.01F + MathHelper.sin(count / 3.0F) * 0.001F);
        essentiaTrail.motionX = (MathHelper.sin(count / 10.0F) * 0.01F + worldObj.rand.nextGaussian() * 0.01D);
        essentiaTrail.motionZ = (MathHelper.sin(count / 10.0F) * 0.01F + worldObj.rand.nextGaussian() * 0.01D);
        ParticleEngine.instance.addEffect(worldObj, essentiaTrail);

        essentiaTrail = new FXEssentiaTrail(worldObj, xCoord + 0.5, yCoord + 2.6, zCoord + 0.5, xCoord + 0.5, yCoord + 1.5, zCoord + 0.5, count, new Color(cr, cg, cb).getRGB(), 0.8F);
        essentiaTrail.noClip = true;
        essentiaTrail.motionY = -(0.01F + MathHelper.sin(count / 3.0F) * 0.001F);
        essentiaTrail.motionX = (MathHelper.sin(count / 10.0F) * 0.01F + worldObj.rand.nextGaussian() * 0.01D);
        essentiaTrail.motionZ = (MathHelper.sin(count / 10.0F) * 0.01F + worldObj.rand.nextGaussian() * 0.01D);
        ParticleEngine.instance.addEffect(worldObj, essentiaTrail);
    }

    private List<WorldCoordinates> searchAndGetSources() {
        WorldCoordinates thisCoordinates = new WorldCoordinates(this);
        Map<WorldCoordinates, List<WorldCoordinates>> teSources = new HashMap<WorldCoordinates, List<WorldCoordinates>>();
        getSourcesField(teSources);
        if(!teSources.containsKey(thisCoordinates)) {
            searchSources();
            getSourcesField(teSources);
            if(teSources.containsKey(thisCoordinates)) {
                return searchAndGetSources();
            }
            return new ArrayList<WorldCoordinates>();
        }
        List<WorldCoordinates> result = teSources.get(thisCoordinates);
        ((Map<WorldCoordinates, List<WorldCoordinates>>) injEssentiaHandler.getField("sources")).remove(thisCoordinates);
        return result;
    }

    private void searchSources() {
        WorldCoordinates thisCoord = new WorldCoordinates(xCoord, yCoord, zCoord, worldObj.provider.dimensionId);
        List<WorldCoordinates> coords = new LinkedList<WorldCoordinates>();
        for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
            unsafe_search(thisCoord, direction, coords);
        }

        ((Map<WorldCoordinates, List<WorldCoordinates>>) injEssentiaHandler.getField("sources")).put(thisCoord, coords);
    }

    private void unsafe_search(WorldCoordinates coord, ForgeDirection direction, List<WorldCoordinates> out) {
        ((HashMap<WorldCoordinates, Long>) injEssentiaHandler.getField("sourcesDelay")).remove(coord);
        injEssentiaHandler.invokeMethod("getSources", new Class[]{ World.class, WorldCoordinates.class, ForgeDirection.class, int.class }, worldObj, coord, direction, 5);
        List<WorldCoordinates> coords = ((Map<WorldCoordinates, List<WorldCoordinates>>) injEssentiaHandler.getField("sources")).get(coord);
        if(coords != null) {
            out.addAll(((Map<WorldCoordinates, List<WorldCoordinates>>) injEssentiaHandler.getField("sources")).get(coord));
            ((Map<WorldCoordinates, List<WorldCoordinates>>) injEssentiaHandler.getField("sources")).remove(coord);
        }
    }

    private void getSourcesField(Map<WorldCoordinates, List<WorldCoordinates>> out) {
        out.clear();
        out.putAll((Map<? extends WorldCoordinates, ? extends List<WorldCoordinates>>) injEssentiaHandler.getField("sources"));
    }

    private void playVortexEffects() {
        for (int a = 0; a < Thaumcraft.proxy.particleCount(1); a++) {
            int tx = this.xCoord + this.worldObj.rand.nextInt(4) - this.worldObj.rand.nextInt(4);
            int ty = this.yCoord + 1 + this.worldObj.rand.nextInt(4) - this.worldObj.rand.nextInt(4);
            int tz = this.zCoord + this.worldObj.rand.nextInt(4) - this.worldObj.rand.nextInt(4);
            if (ty > this.worldObj.getHeightValue(tx, tz)) {
                ty = this.worldObj.getHeightValue(tx, tz);
            }
            Vec3 v1 = Vec3.createVectorHelper(this.xCoord + 0.5D, this.yCoord + 1.5D, this.zCoord + 0.5D);
            Vec3 v2 = Vec3.createVectorHelper(tx + 0.5D, ty + 0.5D, tz + 0.5D);

            MovingObjectPosition mop = ThaumcraftApiHelper.rayTraceIgnoringSource(this.worldObj, v1, v2, true, false, false);
            if ((mop != null) && (getDistanceFrom(mop.blockX, mop.blockY, mop.blockZ) < 16.0D)) {
                tx = mop.blockX;
                ty = mop.blockY;
                tz = mop.blockZ;
                Block bi = this.worldObj.getBlock(tx, ty, tz);
                int md = this.worldObj.getBlockMetadata(tx, ty, tz);
                if (!bi.isAir(this.worldObj, tx, ty, tz)) {
                    Thaumcraft.proxy.hungryNodeFX(this.worldObj, tx, ty, tz, this.xCoord, this.yCoord + 1, this.zCoord, bi, md);
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    private void playLightningEffects() {
        if(worldObj.rand.nextBoolean()) {
            double originX = xCoord + 0.5;
            double originY = yCoord + 1.5;
            double originZ = zCoord + 0.5;
            double targetX = xCoord + 0.4 + worldObj.rand.nextFloat() * 0.2;
            double targetY = yCoord + 0.4 + (worldObj.rand.nextBoolean() ? 2.2 : 0);
            double targetZ = zCoord + 0.4 + worldObj.rand.nextFloat() * 0.2;
            FXLightningBolt bolt = new FXLightningBolt(Minecraft.getMinecraft().theWorld, originX, originY, originZ,
                    targetX, targetY, targetZ, Minecraft.getMinecraft().theWorld.rand.nextLong(), 10, 4.0F, 5);
            bolt.defaultFractal();
            bolt.setType(5);
            bolt.finalizeBolt();
        }
    }

    private void vortexEntities() {
        List entities = worldObj.getEntitiesWithinAABB(Entity.class,
                AxisAlignedBB.getBoundingBox(xCoord - 0.5, yCoord - 0.5, zCoord - 0.5,
                        xCoord + 0.5, yCoord + 0.5, zCoord + 0.5).expand(4, 4, 4));
        for (Object o : entities) {
            if(o == null ||
                    !(o instanceof Entity) ||
                    ((Entity) o).isDead) continue;
            applyMovementVectors((Entity) o);
        }
    }

    private void applyMovementVectors(Entity entity) {
        double var3 = (xCoord + 0.5D - entity.posX) / 8.0D;
        double var5 = (yCoord + 1.5D - entity.posY) / 8.0D;
        double var7 = (zCoord + 0.5D - entity.posZ) / 8.0D;
        double var9 = Math.sqrt(var3 * var3 + var5 * var5 + var7 * var7);
        double var11 = 1.0D - var9;
        if (var11 > 0.0D) {
            var11 *= var11;
            entity.motionX += var3 / var9 * var11 * 0.05D;
            entity.motionY += var5 / var9 * var11 * 0.08D;
            entity.motionZ += var7 / var9 * var11 * 0.05D;
        }
    }

    public void checkMultiblock() {
        if(isMasterTile) {
            boolean canSustain = isMultiblockableBlock(1);
            if(!isMultiblockableBlock(2)) canSustain = false;
            if(!canSustain) {
                breakMultiblock();
                isMultiblockPresent = false;
            }
        } else {
            TileEssentiaCompressor master = tryFindMasterTile();
            if(master != null) {
                if(!master.isMultiblockPresent) {
                    breakMultiblock();
                }
            } else {
                breakMultiblock();
            }
        }
    }

    public TileEssentiaCompressor tryFindMasterTile() {
        if(!isMultiblockFormed()) return null;
        if(isMasterTile()) return this; //lul.. check before plz.
        Block down = worldObj.getBlock(xCoord, yCoord - multiblockYIndex, zCoord);
        if(!down.equals(RegisteredBlocks.blockEssentiaCompressor)) return null;
        TileEntity te = worldObj.getTileEntity(xCoord, yCoord - multiblockYIndex, zCoord);
        if(te == null || !(te instanceof TileEssentiaCompressor)) return null;
        TileEssentiaCompressor compressor = (TileEssentiaCompressor) te;
        if(compressor.multiblockId != multiblockId || !compressor.isMasterTile()) return null;
        return compressor;
    }

    private boolean isMultiblockableBlock(int yOffset) {
        Block block = worldObj.getBlock(xCoord, yCoord + yOffset, zCoord);
        TileEntity te = worldObj.getTileEntity(xCoord, yCoord + yOffset, zCoord);
        if(!block.equals(RegisteredBlocks.blockEssentiaCompressor)) return false;
        if(te == null || !(te instanceof TileEssentiaCompressor)) return false;
        TileEssentiaCompressor compressor = (TileEssentiaCompressor) te;
        return compressor.multiblockId == multiblockId;
    }

    public void breakMultiblock() {
        doExplosion();
        this.multiblockId = -1;
        this.isMasterTile = false;
        this.multiblockYIndex = -1;
        this.al = new AspectList();
        markDirty();
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    private void doExplosion() {
        AspectList al = getAspects();
        if(al.visSize() > 0) {
            ExplosionHelper.taintplosion(worldObj, xCoord, yCoord, zCoord, true, 2, 2.0F, 4, 20);
            worldObj.setBlockToAir(xCoord, yCoord, zCoord);
        } else {
            ExplosionHelper.taintplosion(worldObj, xCoord, yCoord, zCoord, false, 0, 2.0F, 4, 20);
            worldObj.setBlockToAir(xCoord, yCoord, zCoord);
        }
    }

    public static int getAndIncrementNewMultiblockId() {
        multiblockIDCounter++;
        return multiblockIDCounter;
    }

    public void setInMultiblock(boolean isMaster, int yIndex, int multiblockId) {
        this.multiblockId = multiblockId;
        this.isMasterTile = isMaster;
        this.multiblockYIndex = yIndex;
        if(isMaster) {
            this.isMultiblockPresent = true; //Initial state.
        }
        this.al = new AspectList();
        markDirty();
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    public boolean isMultiblockFormed() {
        return multiblockId != -1;
    }

    public boolean isMasterTile() {
        return isMasterTile;
    }

    public int getMultiblockYIndex() {
        return multiblockYIndex;
    }

    @Override
    public void readCustomNBT(NBTTagCompound compound) {
        super.readCustomNBT(compound);

        this.multiblockYIndex = compound.getInteger("multiblockYIndex");
        this.isMasterTile = compound.getBoolean("isMasterTile");
        this.multiblockId = compound.getInteger("multiblockId");
        this.isMultiblockPresent = compound.getBoolean("multiblockPresent");
        AspectList al = new AspectList();
        NBTTagCompound cmp = compound.getCompoundTag("aspects");
        for (Object tag : cmp.func_150296_c()) {
            String strTag = (String) tag;
            int amt = cmp.getInteger(strTag);
            al.add(Aspect.getAspect(strTag), amt);
        }
        this.al = al;
    }

    @Override
    public void writeCustomNBT(NBTTagCompound compound) {
        super.writeCustomNBT(compound);

        compound.setBoolean("isMasterTile", this.isMasterTile);
        compound.setInteger("multiblockId", this.multiblockId);
        compound.setInteger("multiblockYIndex", this.multiblockYIndex);
        compound.setBoolean("multiblockPresent", this.isMasterTile);
        NBTTagCompound aspects = new NBTTagCompound();
        for (Aspect a : al.aspects.keySet()) {
            aspects.setInteger(a.getTag(), al.aspects.get(a));
        }
        compound.setTag("aspects", aspects);
    }

    @Override
    public AspectList getAspects() {
        if(!isMultiblockFormed()) {
            return new AspectList();
        } else {
            if(isMasterTile()) {
                return al;
            } else {
                TileEssentiaCompressor master = tryFindMasterTile();
                if(master == null) return new AspectList();
                return master.getAspects();
            }
        }
    }

    @Override
    public void setAspects(AspectList list) {}

    @Override
    public boolean doesContainerAccept(Aspect aspect) {
        return false;
    }

    @Override
    public int addToContainer(Aspect aspect, int i) {
        return 0;
    }


    @Override
    public boolean takeFromContainer(Aspect aspect, int i) {
        if(isMultiblockFormed() && multiblockYIndex == 1) {
            TileEssentiaCompressor master = tryFindMasterTile();
            if(master == null) return false;
            boolean couldTake = master.al.reduce(aspect, i);
            master.al.remove(aspect, 0);
            worldObj.markBlockForUpdate(master.xCoord, master.yCoord, master.zCoord);
            markDirty();
            return couldTake;
        }
        return false;
    }

    @Override
    public boolean takeFromContainer(AspectList list) {
        return false;
    }

    @Override
    public boolean doesContainerContainAmount(Aspect aspect, int i) {
        if(isMultiblockFormed() && multiblockYIndex == 1) {
            TileEssentiaCompressor master = tryFindMasterTile();
            if(master == null) return false;
            return master.al.getAmount(aspect) >= i;
        }
        return false;
    }

    @Override
    public boolean doesContainerContain(AspectList list) {
        return false;
    }

    @Override
    public int containerContains(Aspect aspect) {
        if(isMultiblockFormed() && multiblockYIndex == 1) {
            TileEssentiaCompressor master = tryFindMasterTile();
            if(master == null) return 0;
            return master.al.getAmount(aspect);
        }
        return 0;
    }



    @Override
    public boolean isConnectable(ForgeDirection direction) {
        return false;
    }

    @Override
    public boolean canInputFrom(ForgeDirection direction) {
        return false;
    }

    @Override
    public boolean canOutputTo(ForgeDirection direction) {
        if(isMultiblockFormed() && multiblockYIndex == 1) { //The middle one
            return direction == ForgeDirection.SOUTH ||
                    direction == ForgeDirection.NORTH ||
                    direction == ForgeDirection.EAST ||
                    direction == ForgeDirection.WEST;
        }
        return false;
    }

    @Override
    public void setSuction(Aspect aspect, int i) {}

    @Override
    public Aspect getSuctionType(ForgeDirection direction) {
        return null;
    }

    @Override
    public int getSuctionAmount(ForgeDirection direction) {
        return 0;
    }

    @Override
    public int takeEssentia(Aspect aspect, int i, ForgeDirection direction) {
        if(isMultiblockFormed() && multiblockYIndex == 1) {
            if(!canOutputTo(direction)) return 0;
            TileEssentiaCompressor master = tryFindMasterTile();
            if(master == null) return 0;
            int amt = master.al.getAmount(aspect);
            int taken = amt - master.al.remove(aspect, i).getAmount(aspect);
            worldObj.markBlockForUpdate(master.xCoord, master.yCoord, master.zCoord);
            markDirty();
            return taken;
        }
        return 0;
    }

    @Override
    public int addEssentia(Aspect aspect, int i, ForgeDirection direction) {
        return 0;
    }

    @Override
    public Aspect getEssentiaType(ForgeDirection direction) {
        if(isMultiblockFormed() && multiblockYIndex == 1) {
            TileEssentiaCompressor master = tryFindMasterTile();
            if(master == null) return null;
            return new ArrayList<Aspect>(master.al.aspects.keySet()).get(worldObj.rand.nextInt(master.al.size()));
        }
        return null;
    }

    @Override
    public int getEssentiaAmount(ForgeDirection direction) {
        if(isMultiblockFormed() && multiblockYIndex == 1) {
            TileEssentiaCompressor master = tryFindMasterTile();
            if(master == null) return 0;
            return master.al.visSize();
        }
        return 0;
    }

    @Override
    public int getMinimumSuction() {
        return 0;
    }

    @Override
    public boolean renderExtendedTube() {
        return false;
    }
}
