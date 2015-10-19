package makeo.gadomancy.common.entities.golems;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import makeo.gadomancy.api.golems.AdditionalGolemType;
import makeo.gadomancy.common.registration.RegisteredItems;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import thaumcraft.common.entities.golems.ItemGolemPlacer;

import java.util.List;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 02.06.2015 23:05
 */
public class ItemAdditionalGolemPlacer extends Item {
    private final ItemGolemPlacer placer = new ItemGolemPlacer();
    private final AdditionalGolemType type;

    public ItemAdditionalGolemPlacer(AdditionalGolemType type) {
        setCreativeTab(RegisteredItems.creativeTab);
        setMaxStackSize(1);
        this.type = type;
    }

    @Override
    public void registerIcons(IIconRegister ir) {
        placer.registerIcons(ir);
        type.registerIcons(ir);
    }

    @Override
    public boolean getHasSubtypes() {
        return true;
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        list.add(new ItemStack(type.getPlacerItem(), 1, type.getEnumEntry().ordinal()));
    }

    public IIcon getIcon(ItemStack stack, int pass) {
        int typePasses = type.getRenderPasses();
        if(pass < typePasses) {
            return type.getIcon(stack, pass);
        }
        return placer.getIcon(stack, pass - typePasses + 1);
    }

    @SideOnly(Side.CLIENT)
    public int getRenderPasses(int metadata) {
        return type.getRenderPasses() + placer.getRenderPasses(metadata) - 1;
    }

    @Override
    public IIcon getIconFromDamage(int damage) {
        return type.getIcon(new ItemStack(this, 1, damage), 0);
    }

    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses() {
        return true;
    }

    public String getUnlocalizedName(ItemStack stack) {
        return getUnlocalizedName();
    }

    @Override
    public String getUnlocalizedName() {
        return type.getUnlocalizedName();
    }

    public boolean getShareTag() {
        return placer.getShareTag();
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
        placer.addInformation(stack, player, list, par4);
    }

    public boolean doesSneakBypassUse(World world, int x, int y, int z, EntityPlayer player) {
        return placer.doesSneakBypassUse(world, x, y, z, player);
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        return placer.onItemUseFirst(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
    }
}