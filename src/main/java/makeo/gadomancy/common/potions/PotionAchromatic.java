package makeo.gadomancy.common.potions;

import makeo.gadomancy.common.data.DataAchromatic;
import makeo.gadomancy.common.data.SyncDataHolder;
import makeo.gadomancy.common.utils.Vector3;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.ai.attributes.BaseAttributeMap;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import thaumcraft.api.aspects.Aspect;

import java.util.List;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by makeo @ 11.12.2015 17:32
 */
public class PotionAchromatic extends PotionCustomTexture {
    public PotionAchromatic(int id) {
        super(id, false, Aspect.CRYSTAL.getColor(), Aspect.CRYSTAL.getImage());
        setPotionName("potion.achromatic");
    }

    @Override
    public void applyAttributesModifiersToEntity(EntityLivingBase entity, BaseAttributeMap p_111185_2_, int p_111185_3_) {
        super.applyAttributesModifiersToEntity(entity, p_111185_2_, p_111185_3_);

        ((DataAchromatic) SyncDataHolder.getDataServer("AchromaticData")).handleApplication(entity);
    }

    @Override
    public void removeAttributesModifiersFromEntity(EntityLivingBase entity, BaseAttributeMap p_111187_2_, int p_111187_3_) {
        super.removeAttributesModifiersFromEntity(entity, p_111187_2_, p_111187_3_);

        ((DataAchromatic) SyncDataHolder.getDataServer("AchromaticData")).handleRemoval(entity);
    }

    @Override
    public boolean isReady(int p_76397_1_, int p_76397_2_) {
        return true;
    }

    @Override
    public void performEffect(EntityLivingBase entity, int p_76394_2_) {
        /*if(entity.boundingBox != null) {
            List<Entity> projectiles = entity.worldObj.getEntitiesWithinAABB(IProjectile.class, entity.boundingBox.expand(3, 3, 3));

            for(Entity e : projectiles) {
                AxisAlignedBB boundingBox = e.boundingBox != null ? e.boundingBox : AxisAlignedBB.getBoundingBox(e.posX, e.posY, e.posZ, e.posX, e.posY, e.posZ);
                if(entity.boundingBox.expand(1.5, 1.5, 1.5).intersectsWith(boundingBox)) {
                    double pMaxDist = new Vector3(entity.boundingBox.minX, entity.boundingBox.minY, entity.boundingBox.minZ).distance(new Vector3(entity.boundingBox.maxX, entity.boundingBox.maxY, entity.boundingBox.maxZ));
                    double eMaxDist = new Vector3(boundingBox.minX, boundingBox.minY, boundingBox.minZ).distance(new Vector3(boundingBox.maxX, boundingBox.maxY, boundingBox.maxZ));
                    double timeoutDist = pMaxDist + eMaxDist*2 + 4.5;

                    Vector3 vec = new Vector3(e.motionX, e.motionY, e.motionZ).normalize();
                    float dist = 0;

                    boolean success = false;
                    boolean state = true;

                    Vector3 temp;
                    do {
                        temp = vec.clone().multiply(dist);
                        if(state) {
                            if(entity.boundingBox.intersectsWith(boundingBox.copy().offset(temp.getX(), temp.getY(), temp.getZ()))) {
                                state = false;
                            }
                        } else {
                            if(!entity.boundingBox.intersectsWith(boundingBox.copy().offset(temp.getX(), temp.getY(), temp.getZ()))) {
                                success = true;
                                break;
                            }
                        }
                        dist += 0.1;
                    } while(dist < timeoutDist);

                    temp = vec.clone().multiply(dist + 1.25);
                    if(success) {
                        e.posX += temp.getX();
                        e.posY += temp.getY();
                        e.posZ += temp.getZ();
                    }
                }
            }
        }*/

        List<Entity> projectiles = entity.worldObj.getEntitiesWithinAABB(IProjectile.class, AxisAlignedBB.getBoundingBox(entity.posX - 4.0D, entity.posY - 4.0D, entity.posZ - 4.0D, entity.posX + 3.0D, entity.posY + 3.0D, entity.posZ + 3.0D));
        for (Entity e : projectiles) {
            Vector3 motionVec = new Vector3(e.motionX, e.motionY, e.motionZ).normalize().multiply(Math.sqrt((e.posX - entity.posX) * (e.posX - entity.posX) + (e.posY - entity.posY) * (e.posY - entity.posY) + (e.posZ - entity.posZ) * (e.posZ - entity.posZ)) * 2.0D);
            e.posX += motionVec.getX();
            e.posY += motionVec.getY();
            e.posZ += motionVec.getZ();
        }
    }
}
