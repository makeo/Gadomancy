package makeo.gadomancy.client.util;

import baubles.api.BaublesApi;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import makeo.gadomancy.common.items.baubles.ItemFamiliar;
import makeo.gadomancy.common.network.packets.PacketFamiliar;
import makeo.gadomancy.common.utils.world.fake.FakeWorld;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.fx.bolt.FXLightningBolt;
import thaumcraft.client.renderers.entity.RenderWisp;
import thaumcraft.common.entities.monster.EntityWisp;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by HellFirePvP @ 31.10.2015 12:13
 */
public class FamiliarHandlerClient {
    private static final EntityWisp ENTITY_WISP;

    private static RenderWisp fallbackRenderer;

    private static Map<String, PartialEntityFamiliar> clientFamiliars = new HashMap<String, PartialEntityFamiliar>();
    private static List<String> familiarPlayers = new ArrayList<String>();

    @SideOnly(Side.CLIENT)
    public static void processPacket(PacketFamiliar packet) {
        if(packet instanceof PacketFamiliar.PacketFamiliarBolt) {
            PacketFamiliar.PacketFamiliarBolt pkt = (PacketFamiliar.PacketFamiliarBolt) packet;
            EntityPlayer p = Minecraft.getMinecraft().theWorld.getPlayerEntityByName(pkt.owner);
            if(p == null || p.getDistanceSqToEntity(Minecraft.getMinecraft().thePlayer) > 1024) return; //32^2
            PartialEntityFamiliar fam = clientFamiliars.get(p.getCommandSenderName());
            if(fam == null) return;
            FXLightningBolt bolt = new FXLightningBolt(Minecraft.getMinecraft().theWorld, (float) fam.posX, (float) fam.posY, (float) fam.posZ, pkt.targetX, pkt.targetY, pkt.targetZ, Minecraft.getMinecraft().theWorld.rand.nextLong(), 10, 4.0F, 5);
            bolt.defaultFractal();
            bolt.setType(pkt.type);
            bolt.finalizeBolt();
        } else if(packet instanceof PacketFamiliar.PacketFamiliarSyncCompletely) {
            PacketFamiliar.PacketFamiliarSyncCompletely sync = (PacketFamiliar.PacketFamiliarSyncCompletely) packet;
            familiarPlayers = sync.playerNamesWithFamiliars;
        } else if(packet instanceof PacketFamiliar.PacketFamiliarSyncSingle) {
            PacketFamiliar.PacketFamiliarSyncSingle sync = (PacketFamiliar.PacketFamiliarSyncSingle) packet;
            String value = sync.name;
            if(sync.status) {
                if(!familiarPlayers.contains(value)) {
                    familiarPlayers.add(value);
                    clientFamiliars.put(value, new PartialEntityFamiliar(Minecraft.getMinecraft().theWorld.getPlayerEntityByName(value), value));
                }
            } else {
                clientFamiliars.remove(value);
                familiarPlayers.remove(value);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public static void playerRenderEvent(EntityPlayer player, float partialTicks) {
        for(String ownerName : clientFamiliars.keySet()) {
            PartialEntityFamiliar fam = clientFamiliars.get(ownerName);

            if(!ownerName.equals(player.getCommandSenderName())) continue;

            ItemStack stack = BaublesApi.getBaubles(player).getStackInSlot(0);
            if(stack == null || !(stack.getItem() instanceof ItemFamiliar)) return;
            if(((ItemFamiliar) stack.getItem()).hasAspect(stack)) {
                ENTITY_WISP.setType(((ItemFamiliar) stack.getItem()).getAspect(stack).getTag());
                ENTITY_WISP.ticksExisted = fam.dummyEntity.ticksExisted;
                GL11.glPushMatrix();
                fallbackRenderer.doRender(ENTITY_WISP, fam.renderX, fam.renderY, fam.renderZ, 0, partialTicks);
                GL11.glPopMatrix();
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public static void playerTickEvent() {
        if(Minecraft.getMinecraft().theWorld == null) return;
        for(PartialEntityFamiliar fam : clientFamiliars.values()) fam.tick();
        PartialEntityFamiliar.DUMMY_FAMILIAR.tick();
    }

    static {
        fallbackRenderer = new RenderWisp();
        fallbackRenderer.setRenderManager(RenderManager.instance);
        ENTITY_WISP = new EntityWisp(new FakeWorld());
    }

    public static class DummyEntityFamiliar extends Entity {

        public DummyEntityFamiliar(World p_i1582_1_) {
            super(p_i1582_1_);
        }

        @Override
        protected void entityInit() {}

        @Override
        protected void readEntityFromNBT(NBTTagCompound p_70037_1_) {}

        @Override
        protected void writeEntityToNBT(NBTTagCompound p_70014_1_) {}
    }

    public static class PartialEntityFamiliar {

        public static final PartialEntityFamiliar DUMMY_FAMILIAR = new PartialEntityFamiliar(null, "§Invalid§");

        private static final double RAD_CAP = 2 * Math.PI;
        private static final int CRICLE_TIME_TICKS = 160;
        private static final double RADIUS = 1;

        public DummyEntityFamiliar dummyEntity = new DummyEntityFamiliar(null);
        public WeakReference<EntityPlayer> owner;
        public String potentialOwnerName;
        public double posX, posY, posZ;
        public double renderX, renderY, renderZ;
        public double prevPosX, prevPosY, prevPosZ;
        public int ticksExisted = 0;

        public PartialEntityFamiliar(EntityPlayer owner, String ownerNameToSearch) {
            this.owner = new WeakReference<EntityPlayer>(owner);
            this.potentialOwnerName = ownerNameToSearch;
        }

        public void tick() {
            ticksExisted++;
            dummyEntity.ticksExisted = ticksExisted;

            if(owner.get() == null) {
                if(potentialOwnerName != null) {
                    EntityPlayer player = Minecraft.getMinecraft().theWorld.getPlayerEntityByName(potentialOwnerName);
                    if(player != null) {
                        owner = new WeakReference<EntityPlayer>(player);
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            } else {
                if(owner.get().worldObj.provider.dimensionId != Minecraft.getMinecraft().renderViewEntity.worldObj.provider.dimensionId) {
                    owner = new WeakReference<EntityPlayer>(null);
                    return;
                }
            }

            this.prevPosX = this.posX;
            this.prevPosY = this.posY;
            this.prevPosZ = this.posZ;

            double part = ((double) ticksExisted) / ((double) CRICLE_TIME_TICKS);
            double theta = RAD_CAP * part;
            this.renderX = RADIUS * Math.cos(theta);
            this.renderZ = RADIUS * Math.sin(theta);
            this.renderY = 0.7 + Math.sin(theta * 2) / 2D;

            this.posX = owner.get().posX + renderX;
            this.posZ = owner.get().posZ + renderZ;
            this.posY = owner.get().posY + renderY;
        }
    }

}
