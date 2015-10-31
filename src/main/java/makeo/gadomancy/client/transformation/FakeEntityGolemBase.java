package makeo.gadomancy.client.transformation;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import thaumcraft.common.entities.golems.EntityGolemBase;
import thaumcraft.common.entities.golems.EnumGolemType;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by makeo @ 05.07.2015 15:47
 */
public class FakeEntityGolemBase extends EntityGolemBase {
    private EntityGolemBase golem;
    private EntityPlayer player;

    public FakeEntityGolemBase(EntityGolemBase golem, EntityPlayer player) {
        super(player != null ? player.worldObj : null);

        this.player = player;
        this.golem = golem;

        this.inactive = false;
        this.bootup = 0;
    }

    public void setPlayer(EntityPlayer player) {
        this.player = player;
    }

    //Golem stuff
    @Override
    public String getGolemDecoration() {
        return golem.getGolemDecoration();
    }

    @Override
    public EnumGolemType getGolemType() {
        return golem.getGolemType();
    }

    @Override
    public byte getCore() {
        return golem.getCore();
    }

    @Override
    public int getCarryLimit() {
        return golem.getCarryLimit();
    }

    @Override
    public byte getUpgrade(int slot) {
        return golem.getUpgrade(slot);
    }

    @Override
    public int getUpgradeAmount(int type) {
        return golem.getUpgradeAmount(type);
    }

    //Player stuff
    @Override
    public float getEyeHeight() {
        return player.getEyeHeight();
    }

    @Override
    public ItemStack getCarriedForDisplay() {
        return player.getHeldItem();
    }

    @Override
    public ItemStack getHeldItem() {
        return player.getHeldItem();
    }

    @Override
    public boolean isSneaking() {
        return player.isSneaking();
    }

    @Override
    public boolean isEating() {
        return player.isEating();
    }

    @Override
    public boolean isInvisible() {
        return player.isInvisible();
    }

    @Override
    public boolean isSprinting() {
        return player.isSprinting();
    }

    @Override
    public void onLivingUpdate() {
        super.onLivingUpdate();
    }

    public void syncWithPlayer() {
        setWorld(player.worldObj);
        setPosition(player.posX, player.posY, player.posZ);

        lastTickPosX = player.lastTickPosX;
        lastTickPosY = player.lastTickPosY;
        lastTickPosZ = player.lastTickPosZ;

        motionX = player.motionX;
        motionY = player.motionY;
        motionZ = player.motionZ;

        moveForward = player.moveForward;
        moveStrafing = player.moveStrafing;

        onGround = player.onGround;

        prevPosX = player.prevPosX;
        prevPosY = player.prevPosY;
        prevPosZ = player.prevPosZ;

        rotationPitch = player.rotationPitch;
        rotationYaw = player.rotationYaw;
        rotationYawHead = player.rotationYawHead;

        prevRotationPitch = player.prevRotationPitch;
        prevRotationYaw = player.prevRotationYaw;
        prevRotationYawHead = player.prevRotationYawHead;

        limbSwing = player.limbSwing;

        limbSwingAmount = player.limbSwingAmount;
        prevLimbSwingAmount = player.prevLimbSwingAmount;
        isSwingInProgress = player.isSwingInProgress;

        swingProgress = player.swingProgress;
        prevSwingProgress = player.prevSwingProgress;

        renderYawOffset = player.renderYawOffset;
        prevRenderYawOffset = player.prevRenderYawOffset;

        ticksExisted = player.ticksExisted;
        isDead = false;
        isAirBorne = player.isAirBorne;

        yOffset = 0;

        swingProgress = player.swingProgress;
        prevSwingProgress = player.prevSwingProgress;

        limbSwing = player.limbSwing;
        limbSwingAmount = player.limbSwingAmount;
        prevLimbSwingAmount = player.limbSwingAmount;

        isSwingInProgress = player.isSwingInProgress;

        itemCarried = player.getHeldItem();

        if(player.isSwingInProgress) {
            if(player.getHeldItem() == null) {
                action = 6 - player.swingProgressInt;
            } else {
                rightArm = 5 - (int)(player.swingProgress * 5);
            }
        } else {
            action = 0;
            rightArm = 0;
        }
    }
}
