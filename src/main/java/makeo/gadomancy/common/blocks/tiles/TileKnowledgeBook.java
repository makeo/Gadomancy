package makeo.gadomancy.common.blocks.tiles;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import makeo.gadomancy.client.util.UtilsFX;
import makeo.gadomancy.common.entities.EntityPermNoClipItem;
import makeo.gadomancy.common.network.PacketHandler;
import makeo.gadomancy.common.network.packets.PacketStartAnimation;
import makeo.gadomancy.common.utils.NBTHelper;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.aspects.IAspectContainer;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.common.entities.EntityPermanentItem;
import thaumcraft.common.entities.EntitySpecialItem;
import thaumcraft.common.items.ItemResearchNotes;
import thaumcraft.common.lib.events.EssentiaHandler;
import thaumcraft.common.lib.research.ResearchManager;
import thaumcraft.common.lib.research.ResearchNoteData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * HellFirePvP@Admin
 * Date: 19.04.2016 / 14:53
 * on Gadomancy
 * TileKnowledgeBook
 */
public class TileKnowledgeBook extends SynchronizedTileEntity implements EntityPermNoClipItem.IItemMasterTile, IAspectContainer {

    private static final Random rand = new Random();
    private static final int LOWEST_AMOUNT = 10;
    private static final int COGNITIO_TICKS = 150;
    private static final int MAX_NEEDED_KNOWLEDGE = 200;
    private static final int SURROUNDINGS_SEARCH_XZ = 4;
    private static final int SURROUNDINGS_SEARCH_Y = 3;
    private static final double MULTIPLIER = 4;

    @Deprecated
    public static Map<BlockSnapshot, Integer> knowledgeIncreaseMap = new HashMap<BlockSnapshot, Integer>();

    private FloatingBookAttributes bookAttributes = new FloatingBookAttributes();

    //The itemstack this is connected to.
    private ItemStack storedResearchNote = null;
    private EntityPermNoClipItem.ItemChangeTask scheduledTask = null;

    //Ticks
    private int timeSinceLastItemInfo = 0;
    private int ticksExisted = 0;
    private int ticksCognitio = 0;

    //Sound effect stuff. I didn't like it when it's like changing pages 4 times in a row...
    private boolean turnedPagesLastTick = false;

    //General research stuff
    private boolean researching = false;
    private AspectList workResearchAspects = null;
    private int surroundingKnowledge = 0;

    @Override
    public void updateEntity() {
        bookAttributes.updateFloatingBook();

        ticksExisted++;
        timeSinceLastItemInfo++;

        if (!worldObj.isRemote) {
            if(timeSinceLastItemInfo > 8) {
                informItemRemoval();
            }

            if(updateResearchStatus()) {
                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                markDirty();
            }

            if(researching) {
                doResearchCycle();
            }
        } else {
            if(researching && hasCognitio()) {
                doResearchEffects();
            }
        }

        if (storedResearchNote == null) {
            tryVortexUnfinishedResearchNotes();
        }
    }

    public boolean hasCognitio() {
        return ticksCognitio > 0;
    }

    public boolean isResearching() {
        return researching;
    }

    private void doResearchCycle() {
        drainCognitio();

        if(!hasCognitio()) {
            return;
        }
        checkSurroundings();

        int chance = Math.max(0, MAX_NEEDED_KNOWLEDGE - this.surroundingKnowledge) + 100;
        if(rand.nextInt(chance) == 0) {
            doResearchProgress();
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            markDirty();
        }
    }

    private void doResearchProgress() {
        List<Aspect> aspects = new ArrayList<Aspect>(this.workResearchAspects.aspects.keySet());
        if(aspects.isEmpty()) {
            finishResearch();
            return;
        }
        Aspect a = aspects.get(rand.nextInt(aspects.size()));
        int value = this.workResearchAspects.aspects.get(a);
        value--;
        if(value <= 0) {
            this.workResearchAspects.aspects.remove(a);
        } else {
            this.workResearchAspects.aspects.put(a, value);
        }
        if(this.workResearchAspects.aspects.isEmpty()) {
            finishResearch();
        }
    }

    private void checkSurroundings() {
        this.surroundingKnowledge = 0;
        for (int xx = -SURROUNDINGS_SEARCH_XZ; xx <= SURROUNDINGS_SEARCH_XZ; xx++) {
            for (int zz = -SURROUNDINGS_SEARCH_XZ;  zz <= SURROUNDINGS_SEARCH_XZ;  zz++) {

                lblYLoop:
                for (int yy = -SURROUNDINGS_SEARCH_Y; yy <= SURROUNDINGS_SEARCH_Y; yy++) {
                    int absX = xx + xCoord;
                    int absY = yy + yCoord;
                    int absZ = zz + zCoord;
                    Block at = worldObj.getBlock(absX, absY, absZ);
                    int meta = worldObj.getBlockMetadata(absX, absY, absZ);
                    TileEntity te = worldObj.getTileEntity(absX, absY, absZ);
                    if(at.equals(Blocks.bookshelf)) {
                        this.surroundingKnowledge += 1;
                    } else if(te != null && te instanceof IKnowledgeProvider) {
                        this.surroundingKnowledge += ((IKnowledgeProvider) te).getProvidedKnowledge(worldObj, absX, absY, absZ);
                    } else if(at instanceof IKnowledgeProvider) {
                        this.surroundingKnowledge += ((IKnowledgeProvider) at).getProvidedKnowledge(worldObj, absX, absY, absZ);
                    } else {
                        for (BlockSnapshot sn : knowledgeIncreaseMap.keySet()) {
                            if(sn.block.equals(at) && sn.metadata == meta) {
                                this.surroundingKnowledge += knowledgeIncreaseMap.get(sn);
                                continue lblYLoop;
                            }
                        }
                    }
                }
            }
        }
    }

    private void drainCognitio() {
        if(ticksCognitio <= 0) {
            searchForCognitio();
        } else {
            ticksCognitio--;
            if(ticksCognitio <= 40) {
                searchForCognitio();
            }
            if(ticksCognitio <= 0) {
                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                markDirty();
            }
        }
    }

    private void searchForCognitio() {
        if((ticksExisted & 31) == 0) {
            int drainRange = 4;
            ForgeDirection[] toTry = ForgeDirection.VALID_DIRECTIONS;
            for (ForgeDirection dir : toTry) {
                if(dir == null) continue; //LUL should not happen...
                if(EssentiaHandler.drainEssentia(this, Aspect.MIND, dir, drainRange)) {
                    ticksCognitio += COGNITIO_TICKS;
                    worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                    markDirty();
                    break;
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    private void doResearchEffects() {
        if((ticksExisted & 15) == 0) {
            UtilsFX.doRuneEffects(Minecraft.getMinecraft().theWorld, xCoord, yCoord - 1, zCoord, 0);
        }
        if((ticksExisted & 31) == 0) {
            switch (rand.nextInt(5)) {
                case 0:
                    if(!turnedPagesLastTick) {
                        worldObj.playSound(xCoord + 0.5, yCoord + 0.3, zCoord + 0.5, "thaumcraft:page", 0.4F, 1.0F, false);
                        turnedPagesLastTick = true;
                        break;
                    }
                case 1:
                case 2:
                case 3:
                    worldObj.playSound(xCoord + 0.5, yCoord + 0.3, zCoord + 0.5, "thaumcraft:write", 0.2F, 1.0F, false);
                    turnedPagesLastTick = false;
                    break;
            }
        }
    }

    private void finishResearch() {
        stopResearch();
        scheduleFinishItemChange();
        PacketStartAnimation packet = new PacketStartAnimation(PacketStartAnimation.ID_SPARKLE_SPREAD, xCoord, yCoord, zCoord);
        PacketHandler.INSTANCE.sendToAllAround(packet, getTargetPoint(32));
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        markDirty();
    }

    private NetworkRegistry.TargetPoint getTargetPoint(double radius) {
        return new NetworkRegistry.TargetPoint(worldObj.provider.dimensionId, xCoord, yCoord, zCoord, radius);
    }

    private void scheduleFinishItemChange() {
        scheduledTask = new EntityPermNoClipItem.ItemChangeTask() {
            @Override
            public void changeItem(EntityPermNoClipItem item) {
                ItemStack stack = item.getEntityItem();
                stack.stackTagCompound.setBoolean("complete", true);
                stack.setItemDamage(64);
            }
        };
    }

    //Returns true, if updating/sync is required.
    private boolean updateResearchStatus() {
        if(researching) {
            if(storedResearchNote != null) {
                ResearchNoteData nd = ResearchManager.getData(storedResearchNote);
                if(nd == null || nd.isComplete()) {
                    stopResearch();
                    return true;
                }
            } else {
                stopResearch();
                return true;
            }
        } else {
            if(storedResearchNote != null) {
                ResearchNoteData nd = ResearchManager.getData(storedResearchNote);
                if(nd != null && !nd.isComplete()) {
                    ResearchItem ri = ResearchCategories.getResearch(nd.key);
                    if(ri != null) {
                        beginResearch(ri.tags);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void beginResearch(AspectList researchTags) {
        AspectList workResearchList = new AspectList();
        for (Aspect a : researchTags.aspects.keySet()) {
            int value = researchTags.aspects.get(a);
            int newVal = (int) Math.max(LOWEST_AMOUNT, ((double) value) * MULTIPLIER);
            workResearchList.add(a, newVal);
        }
        this.workResearchAspects = workResearchList;
        this.researching = true;
    }

    private void stopResearch() {
        this.storedResearchNote = null;
        this.workResearchAspects = null;
        this.researching = false;
    }

    @Override
    public boolean canStillHoldItem() {
        return true;
    }

    @Override
    public void informMaster() {
        this.timeSinceLastItemInfo = 0;
    }

    @Override
    public void informItemRemoval() {
        this.storedResearchNote = null;
        stopResearch();
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        markDirty();
    }

    @Override
    public EntityPermNoClipItem.ItemChangeTask getAndRemoveScheduledChangeTask() {
        if(scheduledTask != null) {
            EntityPermNoClipItem.ItemChangeTask buffer = scheduledTask;
            scheduledTask = null;
            return buffer;
        }
        return null;
    }

    @Override
    public void broadcastItemStack(ItemStack itemStack) {
        this.storedResearchNote = itemStack;
    }

    private void tryVortexUnfinishedResearchNotes() {
        float centerY = yCoord + 0.4F;
        List entityItems = worldObj.selectEntitiesWithinAABB(EntityItem.class,
                AxisAlignedBB.getBoundingBox(xCoord - 0.5, centerY - 0.5, zCoord - 0.5, xCoord + 0.5, centerY + 0.5, zCoord + 0.5).expand(8, 8, 8), new IEntitySelector() {
                    @Override
                    public boolean isEntityApplicable(Entity e) {
                        return !(e instanceof EntityPermanentItem) && !(e instanceof EntitySpecialItem) &&
                                e instanceof EntityItem && ((EntityItem) e).getEntityItem() != null &&
                                ((EntityItem) e).getEntityItem().getItem() instanceof ItemResearchNotes &&
                                shouldVortexResearchNote(((EntityItem) e).getEntityItem());
                    }
                });

        Entity dummy = new EntityItem(worldObj);
        dummy.posX = xCoord + 0.5;
        dummy.posY = centerY + 0.5;
        dummy.posZ = zCoord + 0.5;

        //MC code.
        EntityItem entity = null;
        double d0 = Double.MAX_VALUE;
        for (Object entityItem : entityItems) {
            EntityItem entityIt = (EntityItem) entityItem;
            if (entityIt != dummy) {
                double d1 = dummy.getDistanceSqToEntity(entityIt);
                if (d1 <= d0) {
                    entity = entityIt;
                    d0 = d1;
                }
            }
        }
        if(entity == null) return;
        if(dummy.getDistanceToEntity(entity) < 1 && !worldObj.isRemote) {
            ItemStack inter = entity.getEntityItem();
            inter.stackSize--;
            this.storedResearchNote = inter.copy();
            this.storedResearchNote.stackSize = 1;

            EntityPermNoClipItem item = new EntityPermNoClipItem(entity.worldObj, xCoord + 0.5F, centerY + 0.3F, zCoord + 0.5F, storedResearchNote, xCoord, yCoord, zCoord);
            entity.worldObj.spawnEntityInWorld(item);
            item.motionX = 0;
            item.motionY = 0;
            item.motionZ = 0;
            item.hoverStart = entity.hoverStart;
            item.age = entity.age;
            item.noClip = true;

            timeSinceLastItemInfo = 0;

            if(inter.stackSize <= 0) entity.setDead();
            entity.noClip = false;
            item.delayBeforeCanPickup = 60;
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            markDirty();
        } else {
            entity.noClip = true;
            applyMovementVectors(entity);
        }
    }

    //Special to masterTile only!
    private void applyMovementVectors(EntityItem entity) {
        double var3 = (this.xCoord + 0.5D - entity.posX) / 15.0D;
        double var5 = (this.yCoord + 0.5D - entity.posY) / 15.0D;
        double var7 = (this.zCoord + 0.5D - entity.posZ) / 15.0D;
        double var9 = Math.sqrt(var3 * var3 + var5 * var5 + var7 * var7);
        double var11 = 1.0D - var9;
        if (var11 > 0.0D) {
            var11 *= var11;
            entity.motionX += var3 / var9 * var11 * 0.15D;
            entity.motionY += var5 / var9 * var11 * 0.25D;
            entity.motionZ += var7 / var9 * var11 * 0.15D;
        }
    }

    private boolean shouldVortexResearchNote(ItemStack stack) {
        ResearchNoteData nd = ResearchManager.getData(stack);
        if(nd == null) return false;
        if(nd.isComplete()) return false;
        ResearchItem ri = ResearchCategories.getResearch(nd.key);
        return ri != null;
    }

    public FloatingBookAttributes getBookAttributes() {
        return bookAttributes;
    }

    @Override
    public void readCustomNBT(NBTTagCompound compound) {
        this.ticksCognitio = compound.getInteger("cognitio");
        this.workResearchAspects = NBTHelper.getAspectList(compound, "workAspects");
        this.researching = compound.getBoolean("researching");
        this.storedResearchNote = NBTHelper.getStack(compound, "crystalStack");
    }

    @Override
    public void writeCustomNBT(NBTTagCompound compound) {
        if(workResearchAspects != null) {
            NBTHelper.setAspectList(compound, "workAspects", workResearchAspects);
        }
        compound.setInteger("cognitio", ticksCognitio);
        compound.setBoolean("researching", researching);
        if(storedResearchNote != null)
            NBTHelper.setStack(compound, "crystalStack", storedResearchNote);
    }

    @Override
    public AspectList getAspects() {
        return workResearchAspects;
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
        return false;
    }

    @Override
    public boolean takeFromContainer(AspectList list) {
        return false;
    }

    @Override
    public boolean doesContainerContainAmount(Aspect aspect, int i) {
        return false;
    }

    @Override
    public boolean doesContainerContain(AspectList list) {
        return false;
    }

    @Override
    public int containerContains(Aspect aspect) {
        return 0;
    }

    public static class BlockSnapshot {

        public final Block block;
        public final int metadata;

        public BlockSnapshot(Block block, int metadata) {
            this.block = block;
            this.metadata = metadata;
        }
    }

    public static interface IKnowledgeProvider {

        public int getProvidedKnowledge(World world, int blockX, int blockY, int blockZ);

    }

    public class FloatingBookAttributes {

        //Ugh. EnchantmentTable stuff... I DON'T UNDERSTAND THIS FLOATING BOOK
        //Well not that i tried... copy pasta please.
        public int field_145926_a;
        public float field_145933_i;
        public float field_145931_j;
        public float field_145932_k;
        public float field_145929_l;
        public float field_145930_m;
        public float field_145927_n;
        public float field_145928_o;
        public float field_145925_p;
        public float field_145924_q;


        private void updateFloatingBook() {
            this.field_145927_n = this.field_145930_m;
            this.field_145925_p = this.field_145928_o;
            EntityPlayer entityplayer = TileKnowledgeBook.this.worldObj.getClosestPlayer((double)((float)TileKnowledgeBook.this.xCoord + 0.5F), (double)((float)TileKnowledgeBook.this.yCoord + 0.5F), (double)((float)TileKnowledgeBook.this.zCoord + 0.5F), 3.0D);

            if (entityplayer != null)
            {
                double d0 = entityplayer.posX - (double)((float)TileKnowledgeBook.this.xCoord + 0.5F);
                double d1 = entityplayer.posZ - (double)((float)TileKnowledgeBook.this.zCoord + 0.5F);
                this.field_145924_q = (float)Math.atan2(d1, d0);
                this.field_145930_m += 0.1F;

                if (this.field_145930_m < 0.5F || rand.nextInt(40) == 0)
                {
                    float f1 = this.field_145932_k;

                    do
                    {
                        this.field_145932_k += (float)(rand.nextInt(4) - rand.nextInt(4));
                    }
                    while (f1 == this.field_145932_k);
                }
            }
            else
            {
                this.field_145924_q += 0.02F;
                this.field_145930_m -= 0.1F;
            }

            while (this.field_145928_o >= (float)Math.PI)
            {
                this.field_145928_o -= ((float)Math.PI * 2F);
            }

            while (this.field_145928_o < -(float)Math.PI)
            {
                this.field_145928_o += ((float)Math.PI * 2F);
            }

            while (this.field_145924_q >= (float)Math.PI)
            {
                this.field_145924_q -= ((float)Math.PI * 2F);
            }

            while (this.field_145924_q < -(float)Math.PI)
            {
                this.field_145924_q += ((float)Math.PI * 2F);
            }

            float f2;

            for (f2 = this.field_145924_q - this.field_145928_o; f2 >= (float)Math.PI; f2 -= ((float)Math.PI * 2F)) {}

            while (f2 < -(float)Math.PI)
            {
                f2 += ((float)Math.PI * 2F);
            }

            this.field_145928_o += f2 * 0.4F;

            if (this.field_145930_m < 0.0F)
            {
                this.field_145930_m = 0.0F;
            }

            if (this.field_145930_m > 1.0F)
            {
                this.field_145930_m = 1.0F;
            }

            ++this.field_145926_a;
            this.field_145931_j = this.field_145933_i;
            float f = (this.field_145932_k - this.field_145933_i) * 0.4F;
            float f3 = 0.2F;

            if (f < -f3)
            {
                f = -f3;
            }

            if (f > f3)
            {
                f = f3;
            }

            this.field_145929_l += (f - this.field_145929_l) * 0.9F;
            this.field_145933_i += this.field_145929_l;
        }
    }

}
