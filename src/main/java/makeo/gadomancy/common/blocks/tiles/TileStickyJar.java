package makeo.gadomancy.common.blocks.tiles;

import cpw.mods.fml.common.registry.GameData;
import makeo.gadomancy.common.utils.Injector;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IEssentiaTransport;
import thaumcraft.common.tiles.TileJarFillable;
import thaumcraft.common.tiles.TileJarFillableVoid;

import java.lang.reflect.Field;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 13.07.2015 15:48
 */
public class TileStickyJar extends TileJarFillable {

    private Block parentBlock;
    private Integer parentMetadata = 0;

    private TileJarFillable parent = null;

    public ForgeDirection placedOn;

    private final Injector injector;
    private Field fieldCount;

    public TileStickyJar() {
        injector = new Injector(null, TileJarFillable.class);
        fieldCount = Injector.getField("count", TileJarFillable.class);
    }

    public Block getParentBlock() {
        return parentBlock;
    }

    public TileJarFillable getParent() {
        return parent;
    }

    public boolean isValid() {
        return parentBlock != null && parent != null;
    }

    private boolean needsRenderUpdate = false;

    public void init(TileJarFillable parent, Block parentBlock, int parentMetadata, ForgeDirection placedOn) {
        this.parent = parent;

        this.placedOn = placedOn;

        parent.xCoord = xCoord;
        parent.yCoord = yCoord;
        parent.zCoord = zCoord;

        syncFromParent();

        this.parent.setWorldObj(getWorldObj());
        this.parentBlock = parentBlock;
        this.parentMetadata = parentMetadata;
        this.injector.setObject(this.parent);

        markDirty();
        needsRenderUpdate = true;
    }

    private void sync(TileJarFillable from, TileJarFillable to) {
        to.aspect = from.aspect;
        to.aspectFilter = from.aspectFilter;
        to.amount = from.amount;
        to.maxAmount = from.maxAmount;
        to.facing = from.facing;
        to.forgeLiquid = from.forgeLiquid;
        to.lid = from.lid;
    }
    
    public void syncToParent() {
        sync(this, parent);
    }
    
    public void syncFromParent() {
        sync(parent, this);
    }

    public Integer getParentMetadata() {
        return parentMetadata;
    }

    private int count;

    @Override
    public void updateEntity() {
        if(!isValid()) {
            if(!getWorldObj().isRemote) {
                getWorldObj().setBlock(this.xCoord, this.yCoord, this.zCoord, Blocks.air);
            }
            return;
        }

        if(getWorldObj().isRemote && needsRenderUpdate) {
            needsRenderUpdate = false;
            Minecraft.getMinecraft().renderGlobal.markBlockForUpdate(xCoord, yCoord, zCoord);
        }

        syncToParent();

        boolean canTakeEssentia = this.amount < this.maxAmount;
        if(parent instanceof TileJarFillableVoid) canTakeEssentia = true;

        if ((!this.worldObj.isRemote) && (++this.count % 5 == 0) && canTakeEssentia) {
            fillJar();
        }

        injector.setField(fieldCount, 1);

        parent.updateEntity();

        syncFromParent();
    }

    @Override
    public void setWorldObj(World world) {
        super.setWorldObj(world);
        if(parent != null)
            parent.setWorldObj(worldObj);
    }

    private void fillJar() {
        ForgeDirection inputDir = placedOn.getOpposite();

        TileEntity te = ThaumcraftApiHelper.getConnectableTile(parent.getWorldObj(), parent.xCoord, parent.yCoord, parent.zCoord, inputDir);
        if (te != null)
        {
            IEssentiaTransport ic = (IEssentiaTransport)te;
            if (!ic.canOutputTo(ForgeDirection.DOWN)) {
                return;
            }
            Aspect ta = null;
            if (parent.aspectFilter != null) {
                ta = parent.aspectFilter;
            } else if ((parent.aspect != null) && (parent.amount > 0)) {
                ta = parent.aspect;
            } else if ((ic.getEssentiaAmount(inputDir.getOpposite()) > 0) &&
                    (ic.getSuctionAmount(inputDir.getOpposite()) < getSuctionAmount(ForgeDirection.UP)) && (getSuctionAmount(ForgeDirection.UP) >= ic.getMinimumSuction())) {
                ta = ic.getEssentiaType(inputDir.getOpposite());
            }
            if ((ta != null) && (ic.getSuctionAmount(inputDir.getOpposite()) < getSuctionAmount(ForgeDirection.UP))) {
                addToContainer(ta, ic.takeEssentia(ta, 1, inputDir.getOpposite()));
            }
        }
    }

    @Override
    public void readCustomNBT(NBTTagCompound compound) {
        String parentType = compound.getString("parentType");
        if(parentType.length() > 0) {
            Block block = GameData.getBlockRegistry().getObject(parentType);
            if(block != null && compound.hasKey("parent") && compound.hasKey("parentMetadata")) {
                NBTTagCompound data = compound.getCompoundTag("parent");
                int metadata = compound.getInteger("parentMetadata");
                TileEntity tile = block.createTileEntity(getWorldObj(), metadata);
                if(tile instanceof TileJarFillable) {
                    placedOn = ForgeDirection.getOrientation(compound.getInteger("placedOn"));
                    tile.readFromNBT(data);
                    init((TileJarFillable) tile, block, metadata, placedOn);
                }
            }
        }

        if(!isValid() && !getWorldObj().isRemote) {
            getWorldObj().setBlockToAir(xCoord, yCoord, zCoord);
        }
    }

    @Override
    public void writeCustomNBT(NBTTagCompound compound) {
        if(isValid()) {
            compound.setString("parentType", GameData.getBlockRegistry().getNameForObject(parentBlock));
            compound.setInteger("parentMetadata", parentMetadata);

            syncToParent();

            NBTTagCompound data = new NBTTagCompound();
            parent.writeToNBT(data);
            compound.setTag("parent", data);

            compound.setInteger("placedOn", placedOn.ordinal());
        }
    }

    @Override
    public AspectList getAspects() {
        if(isValid()) {
            syncToParent();
            AspectList result = parent.getAspects();
            syncFromParent();
            return result;
        }
        return new AspectList();
    }

    @Override
    public void setAspects(AspectList paramAspectList) {
        if(isValid()) {
            syncToParent();
            parent.getAspects();
            syncFromParent();
        }
    }

    @Override
    public boolean doesContainerAccept(Aspect paramAspect) {
        if(isValid()) {
            syncToParent();
            boolean result = parent.doesContainerAccept(paramAspect);
            syncFromParent();
            return result;
        }
        return false;
    }

    @Override
    public int addToContainer(Aspect paramAspect, int paramInt) {
        if(isValid()) {
            syncToParent();
            int result = parent.addToContainer(paramAspect, paramInt);
            syncFromParent();
            return result;
        }
        return paramInt;
    }

    @Override
    public boolean takeFromContainer(Aspect paramAspect, int paramInt) {
        if(isValid()) {
            syncToParent();
            boolean result = parent.takeFromContainer(paramAspect, paramInt);
            syncFromParent();
            return result;
        }
        return false;
    }

    @Override
    public boolean takeFromContainer(AspectList paramAspectList) {
        if(isValid()) {
            syncToParent();
            boolean result = parent.takeFromContainer(paramAspectList);
            syncFromParent();
            return result;
        }
        return false;
    }

    @Override
    public boolean doesContainerContainAmount(Aspect paramAspect, int paramInt) {
        if(isValid()) {
            syncToParent();
            boolean result = parent.doesContainerContainAmount(paramAspect, paramInt);
            syncFromParent();
            return result;
        }
        return false;
    }

    @Override
    public boolean doesContainerContain(AspectList paramAspectList) {
        if(isValid()) {
            syncToParent();
            boolean result = parent.doesContainerContain(paramAspectList);
            syncFromParent();
            return result;
        }
        return false;
    }

    @Override
    public int containerContains(Aspect paramAspect) {
        if(isValid()) {
            syncToParent();
            int result = parent.containerContains(paramAspect);
            syncFromParent();
            return result;
        }
        return 0;
    }

    @Override
    public boolean isConnectable(ForgeDirection face) {
        if(isValid()) {
            syncToParent();
            return parent.isConnectable(changeDirection(face));
        }
        return false;
    }

    @Override
    public boolean canInputFrom(ForgeDirection face) {
        if(isValid()) {
            syncToParent();
            boolean result = parent.canInputFrom(changeDirection(face));
            syncFromParent();
            return result;
        }
        return false;
    }

    @Override
    public boolean canOutputTo(ForgeDirection face) {
        if(isValid()) {
            syncToParent();
            boolean result = parent.canOutputTo(changeDirection(face));
            syncFromParent();
            return result;
        }
        return false;
    }

    public ForgeDirection changeDirection(ForgeDirection face) {
        if(placedOn == ForgeDirection.UP) {
            if(face == ForgeDirection.UP || face == ForgeDirection.DOWN) {
                return face.getOpposite();
            }
            return face;
        }

        if(placedOn == ForgeDirection.DOWN) {
            return face;
        }


        if(face == ForgeDirection.UP) {
            return ForgeDirection.NORTH;
        }
        if(face == ForgeDirection.DOWN) {
            return ForgeDirection.SOUTH;
        }
        if(face == placedOn) {
            return ForgeDirection.DOWN;
        }
        if(face == placedOn.getOpposite()) {
            return ForgeDirection.UP;
        }


        switch (placedOn) {
            case EAST: return face == ForgeDirection.NORTH ? ForgeDirection.WEST : ForgeDirection.EAST;
            case SOUTH: return face.getOpposite();
            case WEST: return face == ForgeDirection.SOUTH ? ForgeDirection.WEST : ForgeDirection.EAST;
        }

        return face;
    }

    @Override
    public void setSuction(Aspect paramAspect, int paramInt) {
        if(isValid()) {
            syncToParent();
            parent.setSuction(paramAspect, paramInt);
            syncFromParent();
        }
    }

    @Override
    public Aspect getSuctionType(ForgeDirection paramForgeDirection) {
        if(isValid()) {
            syncToParent();
            Aspect result = parent.getSuctionType(changeDirection(paramForgeDirection));
            syncFromParent();
            return result;
        }
        return null;
    }

    @Override
    public int getSuctionAmount(ForgeDirection paramForgeDirection) {
        if(isValid()) {
            syncToParent();
            int result = parent.getSuctionAmount(changeDirection(paramForgeDirection));
            syncFromParent();
            return result;
        }
        return 0;
    }

    @Override
    public int takeEssentia(Aspect paramAspect, int paramInt, ForgeDirection paramForgeDirection) {
        if(isValid()) {
            syncToParent();
            int result = parent.takeEssentia(paramAspect, paramInt, changeDirection(paramForgeDirection));
            syncFromParent();
            return result;
        }
        return paramInt;
    }

    @Override
    public int addEssentia(Aspect paramAspect, int paramInt, ForgeDirection paramForgeDirection) {
        if(isValid()) {
            syncToParent();
            int result = parent.addEssentia(paramAspect, paramInt, changeDirection(paramForgeDirection));
            syncFromParent();
            return result;
        }
        return paramInt;
    }

    @Override
    public Aspect getEssentiaType(ForgeDirection paramForgeDirection) {
        if(isValid()) {
            syncToParent();
            Aspect result = parent.getEssentiaType(changeDirection(paramForgeDirection));
            syncFromParent();
            return result;
        }
        return null;
    }

    @Override
    public int getEssentiaAmount(ForgeDirection paramForgeDirection) {
        if(isValid()) {
            syncToParent();
            int result = parent.getEssentiaAmount(changeDirection(paramForgeDirection));
            syncFromParent();
            return result;
        }
        return 0;
    }

    @Override
    public int getMinimumSuction() {
        if(isValid()) {
            syncToParent();
            int result = parent.getMinimumSuction();
            syncFromParent();
            return result;
        }
        return 0;
    }

    @Override
    public boolean renderExtendedTube() {
        if(isValid()) {
            syncToParent();
            boolean result = parent.renderExtendedTube();
            syncFromParent();
            return result;
        }
        return false;
    }
}
