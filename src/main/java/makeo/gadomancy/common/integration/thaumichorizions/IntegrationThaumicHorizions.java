package makeo.gadomancy.common.integration.thaumichorizions;

import com.kentington.thaumichorizons.client.renderer.entity.RenderGolemTH;
import com.kentington.thaumichorizons.common.entities.EntityGolemTH;
import com.kentington.thaumichorizons.common.tiles.TileVat;
import com.kentington.thaumichorizons.common.tiles.TileVatMatrix;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import makeo.gadomancy.api.ClickBehavior;
import makeo.gadomancy.client.ClientProxy;
import makeo.gadomancy.common.CommonProxy;
import makeo.gadomancy.common.Gadomancy;
import makeo.gadomancy.common.integration.IntegrationMod;
import makeo.gadomancy.common.registration.RegisteredBlocks;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.PlaySoundAtEntityEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 07.10.2015 13:10
 */
public class IntegrationThaumicHorizions extends IntegrationMod {
    private static Block modMatrix;

    @Override
    public String getModId() {
        return "ThaumicHorizons";
    }

    @Override
    protected void doInit() {
        if(Gadomancy.proxy.getSide() == Side.CLIENT) {
            RendererLivingEntity render = ClientProxy.unregisterRenderer(EntityGolemTH.class, RenderGolemTH.class);
            if(render != null) {
                RenderingRegistry.registerEntityRenderingHandler(EntityGolemTH.class, new RenderAdditionalGolemTH(render.mainModel));
            }

            modMatrix = Block.getBlockFromName("ThaumicHorizons:modMatrix");

            RegisteredBlocks.registerClawClickBehavior(new ClickBehavior(true) {
                private TileVat vat;

                @Override
                public boolean isValidForBlock() {
                    if (block == modMatrix && metadata == 0) {
                        this.vat = ((TileVatMatrix) world.getTileEntity(x, y, z)).getVat();
                        return vat != null;
                    }
                    return false;
                }

                @Override
                public int getComparatorOutput() {
                    return (vat.mode != 0 && vat.mode != 4) ? 15 : 0;
                }

                @Override
                public void addInstability(int instability) {
                    vat.instability += Math.ceil(instability * 0.5);
                }
            });
        }

        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void on(AttackEntityEvent e) {
        EntityPlayer player = e.entityPlayer;
        if(!e.target.worldObj.isRemote && e.target instanceof EntityGolemTH
                && player.isSneaking()) {
            e.setCanceled(true);

            ItemStack stack = player.getCurrentEquippedItem();
            if (stack != null && stack.getItem().onLeftClickEntity(stack, player, e.target)
                    && e.target.isDead) {
                CommonProxy.EVENT_HANDLER_GOLEM.on(new PlaySoundAtEntityEvent(e.target, "thaumcraft:zap", 0.5f, 1.0f));
            }
        }
    }
}
