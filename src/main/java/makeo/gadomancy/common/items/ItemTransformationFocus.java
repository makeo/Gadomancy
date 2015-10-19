package makeo.gadomancy.common.items;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import makeo.gadomancy.common.Gadomancy;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.wands.FocusUpgradeType;
import thaumcraft.api.wands.ItemFocusBasic;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 13.07.2015 02:14
 */
public class ItemTransformationFocus extends ItemFocusBasic {
    private static final AspectList VIS_USAGE = new AspectList().add(Aspect.AIR, 15).add(Aspect.EARTH, 15).add(Aspect.ORDER, 15);

    private IIcon iconOrnament;
    private IIcon iconDepth;

    public ItemTransformationFocus() {
        //setCreativeTab(RegisteredItems.creativeTab);
        setCreativeTab(null);
        setUnlocalizedName("ItemTransformationFocus");
    }

    @Override
    public ItemStack onFocusRightClick(ItemStack wandStack, World world, EntityPlayer player, MovingObjectPosition movingobjectposition) {
        player.setItemInUse(wandStack, 2147483647);
        //WandManager.setCooldown(player, -1);
        return wandStack;
    }

    @Override
    public void onUsingFocusTick(ItemStack wandstack, EntityPlayer player, int count) {

    }

    @Override
    public EnumRarity getRarity(ItemStack focusstack) {
        return EnumRarity.rare;
    }

    @Override
    public boolean isVisCostPerTick(ItemStack item) {
        return false;
    }

    @Override
    public AspectList getVisCost(ItemStack item) {
        return VIS_USAGE;
    }

    @Override
    public IIcon getOrnament(ItemStack item) {
        return iconOrnament;
    }

    @Override
    public int getFocusColor(ItemStack item) {
        return 0xffffff;
    }

    @Override
    public WandFocusAnimation getAnimation(ItemStack item) {
        return WandFocusAnimation.CHARGE;
    }

    @Override
    public IIcon getFocusDepthLayerIcon(ItemStack item) {
        return iconDepth;
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamageForRenderPass(int par1, int renderPass) {
        return renderPass == 1 ? this.icon : this.iconOrnament;
    }

    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses() {
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerIcons(IIconRegister ir) {
        iconOrnament = ir.registerIcon("thaumcraft:focus_whatever_orn");
        iconDepth = ir.registerIcon(Gadomancy.MODID + ":focus_transformation_depth");
        icon = ir.registerIcon(Gadomancy.MODID + ":focus_transformation");
    }

    @Override
    public FocusUpgradeType[] getPossibleUpgradesByRank(ItemStack focusstack, int rank) {
        return null;
    }
}
