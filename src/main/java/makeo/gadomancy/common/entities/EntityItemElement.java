package makeo.gadomancy.common.entities;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import makeo.gadomancy.common.items.ItemElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFlameFX;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import thaumcraft.client.fx.bolt.FXLightningBolt;
import thaumcraft.common.Thaumcraft;

import java.util.List;

/**
 * HellFirePvP@Admin
 * Date: 23.04.2016 / 13:45
 * on Gadomancy
 * EntityItemElement
 */
public class EntityItemElement extends EntityItem {

    public EntityItemElement(World world, double x, double y, double z) {
        super(world, x, y, z);
    }

    public EntityItemElement(World world, double x, double y, double z, ItemStack itemStack) {
        super(world, x, y, z, itemStack);
    }

    public EntityItemElement(World world) {
        super(world);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        ItemStack stack = getEntityItem();
        if(stack == null || !(stack.getItem() instanceof ItemElement)) return;
        int meta = stack.getItemDamage();
        ItemElement.EnumElementType element = ItemElement.EnumElementType.values()[meta % ItemElement.EnumElementType.values().length];

        if(!worldObj.isRemote && (ticksExisted & 15) == 0) {
            doElementServerEffects(element, worldObj, posX, posY, posZ);
        }

        if (this.age + 5 >= this.lifespan) {
            this.age = 0;
        }

        if(worldObj.isRemote && worldObj.rand.nextInt(4) == 0) {
            playClientElementEffect(element, worldObj, posX, posY, posZ);
        }
    }

    public static void doElementServerEffects(ItemElement.EnumElementType type, World world, double posX, double posY, double posZ) {
        List entities = world.getEntitiesWithinAABB(EntityLivingBase.class,
                AxisAlignedBB.getBoundingBox(posX - 0.5, posY - 0.5, posZ - 0.5,
                                             posX + 0.5, posY + 0.5, posZ + 0.5).expand(8, 8, 8));
        for (Object o : entities) {
            if(o == null ||
                    !(o instanceof EntityLivingBase) ||
                    ((EntityLivingBase) o).isDead) continue;
            type.getRunnable().affectEntity((EntityLivingBase) o);
        }
    }

    @SideOnly(Side.CLIENT)
    public static void playClientElementEffect(ItemElement.EnumElementType element, World worldObj, double posX, double posY, double posZ) {
        float randOffsetX = worldObj.rand.nextFloat() * (worldObj.rand.nextBoolean() ? 1 : -1);
        float randOffsetY = worldObj.rand.nextFloat() * (worldObj.rand.nextBoolean() ? 1 : -1);
        float randOffsetZ = worldObj.rand.nextFloat() * (worldObj.rand.nextBoolean() ? 1 : -1);
        switch (element) {
            case DARKNESS:
                FXLightningBolt bolt = new FXLightningBolt(Minecraft.getMinecraft().theWorld, posX, posY + 0.2F, posZ,
                        posX + randOffsetX / 2, posY + randOffsetY / 2, posZ + randOffsetZ / 2, Minecraft.getMinecraft().theWorld.rand.nextLong(), 10, 4.0F, 5);
                bolt.defaultFractal();
                bolt.setType(5);
                bolt.finalizeBolt();
                break;
            /*case ORDER:
                Thaumcraft.proxy.wispFX(worldObj, posX + randOffsetX / 4, posY + 0.2F + randOffsetY / 4, posZ + randOffsetZ / 4, 0.1F, 0xFF, 0xFF, 0xFF);
                break;
            case FIRE:
                Minecraft.getMinecraft().effectRenderer.addEffect(
                        new EntityFlameFX(worldObj, posX + randOffsetX / 4, posY + 0.2F + randOffsetY / 4, posZ + randOffsetZ / 4, 0, 0, 0));
                break;*/
        }
    }

}
