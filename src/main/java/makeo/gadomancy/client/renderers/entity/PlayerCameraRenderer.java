package makeo.gadomancy.client.renderers.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.entity.player.EntityPlayer;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by makeo @ 09.07.2015 02:34
 */
public class PlayerCameraRenderer extends EntityRenderer {
    private final Minecraft minecraft;

    private boolean isMarkedForRemoval;
    private boolean isRemoved;
    private EntityRenderer prevRenderer;

    public float xOffset = 0;
    public float yOffset = 0;
    public float zOffset = 0;

    public PlayerCameraRenderer(Minecraft minecraft, EntityRenderer prevRenderer) {
        super(minecraft, minecraft.getResourceManager());
        this.minecraft = minecraft;

        this.isMarkedForRemoval = false;
        this.isRemoved = false;
        this.prevRenderer = prevRenderer;
    }

    public EntityRenderer getPrevRenderer() {
        return prevRenderer;
    }

    public boolean isMarkedForRemoval() {
        return isMarkedForRemoval;
    }

    public boolean isRemoved() {
        return isRemoved;
    }

    public void markForRemoval() {
        this.isMarkedForRemoval = true;
    }

    private boolean canChangeView() {
        return !isMarkedForRemoval && minecraft.thePlayer != null && !minecraft.thePlayer.isRiding() && !minecraft.thePlayer.isPlayerSleeping();
    }

    private void removeIfMarked() {
        if(isMarkedForRemoval && this.equals(Minecraft.getMinecraft().entityRenderer)) {
            Minecraft.getMinecraft().entityRenderer = prevRenderer;
            isRemoved = true;
        }
    }

    @Override
    public void updateCameraAndRender(float partialTicks) {
        removeIfMarked();

        if(canChangeView()) {
            moveCam(-1);
            //float tempEyeHeight = minecraft.thePlayer.eyeHeight;
            //minecraft.thePlayer.eyeHeight = minecraft.thePlayer.getDefaultEyeHeight();
            super.updateCameraAndRender(partialTicks);
            //minecraft.thePlayer.eyeHeight = tempEyeHeight;
            moveCam(1);

            return;
        }

        super.updateCameraAndRender(partialTicks);
    }

    private void moveCam(int mod) {
        EntityPlayer player = minecraft.thePlayer;

        player.posX += this.xOffset * mod;
        player.lastTickPosX += this.xOffset * mod;
        player.prevPosX += this.xOffset * mod;

        player.posY += this.yOffset * mod;
        player.lastTickPosY += this.yOffset * mod;
        player.prevPosY += this.yOffset * mod;

        player.posZ += this.zOffset * mod;
        player.lastTickPosZ += this.zOffset * mod;
        player.prevPosZ += this.zOffset * mod;
    }

    @Override
    public void getMouseOver(float partialTicks) {
        removeIfMarked();

        if(canChangeView()) {
            moveCam(-1);
            super.getMouseOver(partialTicks);
            moveCam(1);

            return;
        }

        super.getMouseOver(partialTicks);
    }
}
