package makeo.gadomancy.common.integration.thaumichorizions;

import com.google.common.collect.BiMap;
import com.kentington.thaumichorizons.client.renderer.entity.RenderGolemTH;
import com.kentington.thaumichorizons.common.entities.EntityGolemTH;
import com.kentington.thaumichorizons.common.items.ItemGolemBellTH;
import com.kentington.thaumichorizons.common.tiles.TileVat;
import com.kentington.thaumichorizons.common.tiles.TileVatMatrix;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.ExistingSubstitutionException;
import cpw.mods.fml.common.registry.FMLControlledNamespacedRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import makeo.gadomancy.api.ClickBehavior;
import makeo.gadomancy.client.ClientProxy;
import makeo.gadomancy.common.CommonProxy;
import makeo.gadomancy.common.Gadomancy;
import makeo.gadomancy.common.integration.IntegrationMod;
import makeo.gadomancy.common.registration.RegisteredBlocks;
import makeo.gadomancy.common.utils.Injector;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.PlaySoundAtEntityEvent;
import thaumcraft.common.config.ConfigItems;

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

        undoSubstitution("Thaumcraft:GolemBell");

        ItemGolemBellOverride override = new ItemGolemBellOverride();
        try {
            GameRegistry.addSubstitutionAlias("Thaumcraft:GolemBell", GameRegistry.Type.ITEM, override);
        } catch (ExistingSubstitutionException ignored) { }
        ConfigItems.itemGolemBell = override;

    }

    private void undoSubstitution(String name) {
        Injector registry = new Injector(GameRegistry.Type.ITEM.getRegistry(), FMLControlledNamespacedRegistry.class);
        BiMap map = registry.invokeMethod("getPersistentSubstitutions");
        map.remove(name);
    }

    public static class ItemGolemBellOverride extends ItemGolemBellTH {
        @Override
        public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
            boolean result = super.onLeftClickEntity(stack, player, entity);
            if(result && !entity.worldObj.isRemote && entity instanceof EntityGolemTH
                    && entity.isDead && player.isSneaking()) {
                CommonProxy.EVENT_HANDLER_GOLEM.on(new PlaySoundAtEntityEvent(entity, "thaumcraft:zap", 0.5f, 1.0f));
            }
            return result;
        }
    }
}
