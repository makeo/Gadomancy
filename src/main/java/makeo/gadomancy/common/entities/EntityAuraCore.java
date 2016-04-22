package makeo.gadomancy.common.entities;

import io.netty.buffer.ByteBuf;
import makeo.gadomancy.client.effect.EffectHandler;
import makeo.gadomancy.client.effect.fx.EntityFXFlowPolicy;
import makeo.gadomancy.client.effect.fx.FXFlow;
import makeo.gadomancy.client.effect.fx.Orbital;
import makeo.gadomancy.common.data.config.ModConfig;
import makeo.gadomancy.common.items.ItemAuraCore;
import makeo.gadomancy.common.network.PacketHandler;
import makeo.gadomancy.common.network.packets.PacketStartAnimation;
import makeo.gadomancy.common.registration.RegisteredItems;
import makeo.gadomancy.common.utils.MiscUtils;
import makeo.gadomancy.common.utils.PrimalAspectList;
import makeo.gadomancy.common.utils.Vector3;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.util.WeightedRandom;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.lib.utils.BlockUtils;
import thaumcraft.common.lib.world.biomes.BiomeHandler;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by HellFirePvP @ 16.11.2015 14:42
 */
public class EntityAuraCore extends EntityItem implements IEntityAdditionalSpawnData {

    private static final String SPLIT = ";";

    private static final int PRE_GATHER_EFFECT_LENGTH = 50;
    private static final int GATHER_EFFECT_LENGTH = 500; //20 + 430
    private static final int GATHER_RANGE = 4;
    private static final int CLUSTER_WEIGHT = 10; //Counts as 10 'blocks' 'scanned' with the given aspect.
    public static final int CLUSTER_RANGE = 10; //Defines how close the clicked cluster has to be.
    private static final int REQUIRED_BLOCKS = (int) Math.round(Math.pow(GATHER_RANGE*2+1, 3) * 0.15);

    public PrimalAspectList internalAuraList = new PrimalAspectList();
    public Orbital auraOrbital = null;

    //Effect stuff ResidentSleeper
    private Aspect[] effectAspects = new Aspect[6];
    private Orbital.OrbitalRenderProperties[] effectProperties = new Orbital.OrbitalRenderProperties[6];
    private FXFlow[] flows = new FXFlow[6];

    private String oldAspectDataSent = null;
    public BlockPos activationLocation = null;

    private int blockCount;

    public EntityAuraCore(World world) {
        super(world);
    }

    public EntityAuraCore(World world, double x, double y, double z, ItemStack stack, BlockPos startingCoords, Aspect[] aspects) {
        super(world, x, y, z, stack);
        this.activationLocation = startingCoords;
        if(aspects.length == 1) {
            this.internalAuraList.add(aspects[0], CLUSTER_WEIGHT * 6);
        } else {
            for(Aspect a : aspects) {
                if(a == null) continue;
                this.internalAuraList.add(a, CLUSTER_WEIGHT);
            }
        }
        sendAspectData(electParliament());

        initGathering();
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        getDataWatcher().addObjectByDataType(ModConfig.entityAuraCoreDatawatcherAspectsId, 4);

        getDataWatcher().updateObject(ModConfig.entityAuraCoreDatawatcherAspectsId, "");
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if(auraOrbital == null && !MiscUtils.getPositionVector(this).equals(Vector3.ZERO)
                && worldObj.isRemote) {
            auraOrbital = new Orbital(MiscUtils.getPositionVector(this), worldObj);
        }
        if(auraOrbital != null && worldObj.isRemote) {
            if(!auraOrbital.registered) {
                EffectHandler.getInstance().registerOrbital(auraOrbital);
            }
            auraOrbital.updateCenter(MiscUtils.getPositionVector(this));
            if(ticksExisted > PRE_GATHER_EFFECT_LENGTH) {
                int part = ticksExisted - PRE_GATHER_EFFECT_LENGTH;
                float perc = ((float) GATHER_EFFECT_LENGTH - part) / ((float) GATHER_EFFECT_LENGTH);
                auraOrbital.reduceAllOffsets(perc);
            }
        }

        this.setNoDespawn();

        if(!worldObj.isRemote) {
            if(ticksExisted > GATHER_EFFECT_LENGTH) {
                finishCore();
            } else if(ticksExisted > PRE_GATHER_EFFECT_LENGTH) {
                auraGatherCycle();
            }
        } else {
            boolean changed = recieveAspectData();
            for (int i = 0; i < effectProperties.length; i++) {
                Orbital.OrbitalRenderProperties node = effectProperties[i];
                if(node == null) {
                    if(effectAspects[i] == null) {
                        continue;
                    }

                    node = new Orbital.OrbitalRenderProperties(Orbital.Axis.persisentRandomAxis(), 1D);//rand.nextDouble()
                    node.setColor(new Color(effectAspects[i].getColor())).setTicksForFullCircle(120 + rand.nextInt(40));
                    node.setOffsetTicks(rand.nextInt(80));
                    Color c = getSubParticleColor(effectAspects[i]);
                    node.setSubParticleColor(c);
                    node.setParticleSize(0.1f);
                    node.setSubSizeRunnable(new Orbital.OrbitalSubSizeRunnable() {
                        @Override
                        public float getSubParticleSize(Random rand, int orbitalExisted) {
                            return 0.05F + (rand.nextBoolean() ? 0.0F : 0.025F);
                        }
                    });
                    //node.setMultiplier()
                    effectProperties[i] = node;
                }

                if(flows[i] == null && ticksExisted < PRE_GATHER_EFFECT_LENGTH) {
                    Vector3 v = new Vector3(activationLocation.getX() + 0.5D, activationLocation.getY() + 0.5D, activationLocation.getZ() + 0.5D);
                    flows[i] = EffectHandler.getInstance().effectFlow(worldObj,
                            v, new FXFlow.EntityFlowProperties().setPolicy(EntityFXFlowPolicy.Policies.DEFAULT)
                                    .setTarget(auraOrbital.getOrbitalStartPoints(node)[0])
                                    .setColor(new Color(effectAspects[i].getColor())).setFading(getSubParticleColor(effectAspects[i])));
                    flows[i].setLivingTicks(PRE_GATHER_EFFECT_LENGTH - ticksExisted);
                }

                if(flows[i] != null) flows[i].applyTarget(auraOrbital.getOrbitalStartPoints(node)[0]);

                if(changed) {
                    if(effectProperties[i] != null) {
                        effectProperties[i].setColor(new Color(effectAspects[i].getColor()));
                        Color c = getSubParticleColor(effectAspects[i]);
                        node.setSubParticleColor(c);
                    }

                    if(flows[i] != null) {
                        flows[i].setColor(getSubParticleColor(effectAspects[i]));
                        flows[i].setColor(new Color(effectAspects[i].getColor()));
                    }
                }
            }
            if(ticksExisted >= (PRE_GATHER_EFFECT_LENGTH - 1)) {
                if(auraOrbital.orbitalsSize() == 0) {
                    for (int i = 0; i < 6; i++) {
                        Orbital.OrbitalRenderProperties node = effectProperties[i];
                        auraOrbital.addOrbitalPoint(node);
                    }
                }
            } else {
                for (int i = 0; i < 6; i++) {
                    if(flows[i] != null) {
                        flows[i].lastUpdateCall = System.currentTimeMillis();
                    }
                }
            }
        }
    }

    private void finishCore() {
        ItemStack auraCore = new ItemStack(RegisteredItems.itemAuraCore, 1, 0);
        boolean success = false;
        if(blockCount >= REQUIRED_BLOCKS) {
            double avg = ((double) this.internalAuraList.visSize()) / ((double) this.internalAuraList.size());
            Aspect[] sortedHtL = this.internalAuraList.getAspectsSortedByAmount();
            AspectList al = new AspectList();
            for(Aspect a : sortedHtL) {
                if(a == null) return;
                int am = this.internalAuraList.getAmount(a);
                if(am >= avg) {
                    al.add(a, am);
                }
            }

            List<AspectWRItem> rand = new ArrayList<AspectWRItem>();
            for(Aspect a : al.getAspects()) {
                if(a == null) continue;
                rand.add(new AspectWRItem(al.getAmount(a), a));
            }

            Aspect aura = WeightedRandom.getRandomItem(worldObj.rand, rand).getAspect();
            for(ItemAuraCore.AuraCoreType type : ItemAuraCore.AuraCoreType.values()) {
                if(type.isAspect() && type.getAspect().equals(aura)) {
                    RegisteredItems.itemAuraCore.setCoreType(auraCore, type);
                    success = true;
                }
            }
        }

        PacketStartAnimation animationPacket;
        if(!success) {
            animationPacket = new PacketStartAnimation(PacketStartAnimation.ID_SMOKE_SPREAD, (int) posX, (int) posY, (int) posZ, Float.floatToIntBits(2F));
        } else {
            animationPacket = new PacketStartAnimation(PacketStartAnimation.ID_SPARKLE_SPREAD, (int) posX, (int) posY, (int) posZ, (byte) 0);
        }
        PacketHandler.INSTANCE.sendToAllAround(animationPacket, MiscUtils.getTargetPoint(worldObj, this, 32));

        EntityItem ei = new EntityItem(worldObj, posX, posY, posZ, auraCore);
        ei.motionX = 0;
        ei.motionY = 0;
        ei.motionZ = 0;
        worldObj.spawnEntityInWorld(ei);
        setDead();
    }

    private List<BlockPos> markedLocations;

    private void initGathering() {
        markedLocations = new ArrayList<BlockPos>((int) Math.pow(GATHER_RANGE*2+1, 3));
        for(int x = -GATHER_RANGE; x <= GATHER_RANGE; x++) {
            for(int y = -GATHER_RANGE; y <= GATHER_RANGE; y++) {
                for(int z = -GATHER_RANGE; z <= GATHER_RANGE; z++) {
                    markedLocations.add(new BlockPos(x, y, z));
                }
            }
        }
        Collections.shuffle(markedLocations, new Random(activationLocation.hashCode()));
    }

    private void auraGatherCycle() {
        if(ticksExisted >= PRE_GATHER_EFFECT_LENGTH) {
            int elapsed = ticksExisted - PRE_GATHER_EFFECT_LENGTH - 1;
            float dist = (GATHER_EFFECT_LENGTH - PRE_GATHER_EFFECT_LENGTH) / (float)markedLocations.size();

            int index = (int) (elapsed / dist);
            int lastIndex = (int)((elapsed - 1) / dist);
            if(index < markedLocations.size() && index > lastIndex) {
                int diff = index - lastIndex;
                for(int i = 0; i < diff; i++) {
                    BlockPos coord = markedLocations.get(index + i);

                    int x = (int) (coord.getX() + posX);
                    int y = (int) (coord.getY() + posY);
                    int z = (int) (coord.getZ() + posZ);

                    BlockPos pos = new BlockPos(x, y, z);
                    if(!worldObj.isAirBlock(pos)) {
                        BiomeGenBase biome = worldObj.getBiomeGenForCoords(pos);
                        /*IBlockState state = worldObj.getBlockState(pos);
                        Block block = state.getBlock();
                        //int meta = worldObj.getBlockMetadata(x, y, z);
                        ScanResult result = null;
                        MovingObjectPosition mov = new MovingObjectPosition(MovingObjectPosition.MovingObjectType.BLOCK,
                                new Vec3(0, 0, 0), EnumFacing.UP, pos);
                        ItemStack is = null;
                        try {
                            is = block.getPickBlock(mov, worldObj, pos);
                        } catch (Throwable tr) {}
                        try {
                            if(is == null) {
                                is = BlockUtils.createStackedBlock(state);
                            }
                        }
                        catch (Exception e) {}
                        try {
                            if (is == null) {
                                result = new ScanResult((byte)1, Block.getIdFromBlock(block), meta, null, "");
                            } else {
                                result = new ScanResult((byte)1, Item.getIdFromItem(is.getItem()), is.getItemDamage(), null, "");
                            }
                        }
                        catch (Exception e) {}*/

                        //if(result == null) continue; //We can't scan it BibleThump

                        //AspectList aspects = ScanManager.getScanAspects(result, worldObj);

                        Aspect aspect = BiomeHandler.getRandomBiomeTag(biome.biomeID, worldObj.rand);

                        if(aspect == null) continue;

                        internalAuraList.add(aspect, 1);
                        blockCount++;
                        /*if(aspects.size() > 0) {
                            internalAuraList.add(aspects);
                            blockCount++;
                        }*/
                    }
                }
            }
            sendAspectData(electParliament());
        }
    }

    private Aspect[] electParliament() {
        Aspect[] colors = new Aspect[6];

        Aspect[] aspects = internalAuraList.getAspectsSortedByAmount();

        int totalSize = internalAuraList.visSize();

        int availableSeats = 6;
        for(Aspect aspect : aspects) {
            float percent = internalAuraList.getAmount(aspect) / (float)totalSize;

            int seats = (int) Math.ceil(availableSeats * percent);
            seats = Math.min(seats, availableSeats);
            availableSeats -= seats;

            for(int i = 0; i < colors.length && seats > 0; i++) {
                if(colors[i] == null) {
                    colors[i] = aspect;
                    seats--;
                }
            }

            if(availableSeats <= 0) {
                break;
            }
        }

        for(int i = 0; i < colors.length; i++) {
            if(colors[i] == null) {
                colors[i] = aspects[0];
            }
        }
        return colors;
    }

    private Color getSubParticleColor(Aspect a) {
        Color c = null;
        if(a.equals(Aspect.AIR)) {
            c = new Color(0xFEFFC9);
        } else if(a.equals(Aspect.FIRE)) {
            c = new Color(0xFFAA3C);
        } else if(a.equals(Aspect.EARTH)) {
            c = new Color(0x7EE35F);
        } else if(a.equals(Aspect.WATER)) {
            c = new Color(0x78CFCC);
        } else if(a.equals(Aspect.ORDER)) {
            c = new Color(0xFFFFFF);
        } else if(a.equals(Aspect.ENTROPY)) {
            c = new Color(0x000000);
        }
        return c;
    }

    //Client side only.
    private boolean recieveAspectData() {
        String rec = getDataWatcher().getWatchableObjectString(ModConfig.entityAuraCoreDatawatcherAspectsId);
        if(oldAspectDataSent == null || !this.oldAspectDataSent.equals(rec)) {
            oldAspectDataSent = rec;
        } else {
            return false;
        }

        if(rec.equals("")) return false;

        String[] arr = rec.split(SPLIT);
        if(arr.length != 6) throw new IllegalStateException("Server sent wrong Aura Data! '"
                + rec + "' Please report this error to the mod authors!");
        for (int i = 0; i < arr.length; i++) {
            String s = arr[i];
            Aspect a = Aspect.getAspect(s);
            effectAspects[i] = a;
        }
        return true;
    }

    private void sendAspectData(Aspect[] aspects) {
        StringBuilder sb = new StringBuilder();
        for (Aspect aspect : aspects) {
            if (sb.length() > 0) {
                sb.append(SPLIT);
            }
            sb.append(aspect.getTag());

        }
        String toSend = sb.toString();

        if(this.oldAspectDataSent == null || !this.oldAspectDataSent.equals(toSend)) {
            this.oldAspectDataSent = toSend;
            getDataWatcher().updateObject(ModConfig.entityAuraCoreDatawatcherAspectsId, toSend);
        }
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);

        this.internalAuraList = new PrimalAspectList();

        ticksExisted = compound.getInteger("ticksExisted");
        NBTTagList list = compound.getTagList("auraList", compound.getId());
        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound cmp = list.getCompoundTagAt(i);
            if(cmp.hasKey("tag") && cmp.hasKey("amt")) {
                internalAuraList.add(Aspect.getAspect(cmp.getString("tag")), cmp.getInteger("amt"));
            }
        }
        if(compound.hasKey("activationVecX") && compound.hasKey("activationVecY") && compound.hasKey("activationVecZ")) {
            int x = compound.getInteger("activationVecX");
            int y = compound.getInteger("activationVecY");
            int z = compound.getInteger("activationVecZ");
            activationLocation = new BlockPos(x, y, z);
        }
        sendAspectData(electParliament());

        initGathering();

        blockCount = compound.getInteger("blockCount");
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);

        compound.setInteger("ticksExisted", ticksExisted);
        NBTTagList list = new NBTTagList();
        for(Aspect a : internalAuraList.getAspects()) {
            if(a == null) continue;
            NBTTagCompound aspectCompound = new NBTTagCompound();
            aspectCompound.setString("tag", a.getTag());
            aspectCompound.setInteger("amt", internalAuraList.getAmount(a));
            list.appendTag(aspectCompound);
        }
        compound.setTag("auraList", list);
        if(activationLocation != null) {
            compound.setInteger("activationVecX", activationLocation.getX());
            compound.setInteger("activationVecY", activationLocation.getY());
            compound.setInteger("activationVecZ", activationLocation.getZ());
        }

        compound.setInteger("blockCount", blockCount);
    }

    @Override
    public void writeSpawnData(ByteBuf buffer) {
        buffer.writeInt(ticksExisted);

        buffer.writeInt(activationLocation.getX());
        buffer.writeInt(activationLocation.getY());
        buffer.writeInt(activationLocation.getZ());
    }

    @Override
    public void readSpawnData(ByteBuf buffer) {
        ticksExisted = buffer.readInt();

        activationLocation = new BlockPos(buffer.readInt(), buffer.readInt(), buffer.readInt());
    }

    public static final class AspectWRItem extends WeightedRandom.Item {

        private final Aspect aspect;

        public AspectWRItem(int weight, Aspect aspect) {
            super(weight);
            this.aspect = aspect;
        }

        public Aspect getAspect() {
            return aspect;
        }
    }

}
