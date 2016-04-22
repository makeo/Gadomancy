package makeo.gadomancy.client.fx;

import makeo.gadomancy.common.utils.Vector3;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import org.lwjgl.opengl.GL11;

import java.util.Iterator;

public class FXLightningBolt extends EntityFX {

    public FXLightningBolt(World world, Vector3 jammervec, Vector3 targetvec, long seed) {
        super(world, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
        this.main = new FXLightningBoltCommon(world, jammervec, targetvec, seed);
        setupFromMain();
    }

    public FXLightningBolt(World world, Entity detonator, Entity target, long seed) {
        super(world, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
        this.main = new FXLightningBoltCommon(world, detonator, target, seed);
        setupFromMain();
    }

    public FXLightningBolt(World world, Entity detonator, Entity target, long seed, int speed) {
        super(world, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
        this.main = new FXLightningBoltCommon(world, detonator, target, seed, speed);
        setupFromMain();
    }

    public FXLightningBolt(World world, TileEntity detonator, Entity target, long seed) {
        super(world, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
        this.main = new FXLightningBoltCommon(world, detonator, target, seed);
        setupFromMain();
    }

    public FXLightningBolt(World world, double x1, double y1, double z1, double x, double y, double z, long seed, int duration, float multi) {
        super(world, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
        this.main = new FXLightningBoltCommon(world, x1, y1, z1, x, y, z, seed, duration, multi);
        setupFromMain();
    }

    public FXLightningBolt(World world, double x1, double y1, double z1, double x, double y, double z, long seed, int duration, float multi, int speed) {
        super(world, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
        this.main = new FXLightningBoltCommon(world, x1, y1, z1, x, y, z, seed, duration, multi, speed);
        setupFromMain();
    }

    public FXLightningBolt(World world, double x1, double y1, double z1, double x, double y, double z, long seed, int duration) {
        super(world, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
        this.main = new FXLightningBoltCommon(world, x1, y1, z1, x, y, z, seed, duration, 1.0F);
        setupFromMain();
    }

    public FXLightningBolt(World world, TileEntity detonator, double x, double y, double z, long seed) {
        super(world, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
        this.main = new FXLightningBoltCommon(world, detonator, x, y, z, seed);
        setupFromMain();
    }

    private void setupFromMain() {
        this.particleAge = this.main.particleMaxAge;
        setPosition(this.main.start.getX(), this.main.start.getY(), this.main.start.getZ());
        setVelocity(0.0D, 0.0D, 0.0D);
    }

    public void defaultFractal() {
        this.main.defaultFractal();
    }

    public void fractal(int splits, float amount, float splitchance, float splitlength, float splitangle) {
        this.main.fractal(splits, amount, splitchance, splitlength, splitangle);
    }

    public void finalizeBolt() {
        this.main.finalizeBolt();
        FMLClientHandler.instance().getClient().effectRenderer.addEffect(this);
    }

    public void setType(int type) {
        this.type = type;
        this.main.type = type;
    }

    public void setMultiplier(float m) {
        this.main.multiplier = m;
    }

    public void setWidth(float m) {
        this.width = m;
    }

    private int type = 0;
    private float width = 0.03F;
    private FXLightningBoltCommon main;

    public void onUpdate() {
        this.main.onUpdate();
        if (this.main.particleAge >= this.main.particleMaxAge) {
            setDead();
        }
    }

    private static Vector3 getRelativeViewVector(Vector3 pos) {
        EntityPlayer renderentity = FMLClientHandler.instance().getClient().thePlayer;
        return new Vector3((float) renderentity.posX - pos.getX(), (float) renderentity.posY - pos.getY(), (float) renderentity.posZ - pos.getZ());
    }

    private void renderBolt(WorldRenderer worldRenderer, float partialframe, float cosyaw, float cospitch, float sinyaw, float cossinpitch, int pass, float mainalpha) {
        Vector3 playervec = new Vector3(sinyaw * -cospitch, -cossinpitch / cosyaw, cosyaw * cospitch);
        float boltage = this.main.particleAge >= 0 ? this.main.particleAge / this.main.particleMaxAge : 0.0F;
        if (pass == 0) {
            mainalpha = (1.0F - boltage) * 0.4F;
        } else {
            mainalpha = 1.0F - boltage * 0.5F;
        }
        int renderlength = (int) ((this.main.particleAge + partialframe + (int) (this.main.length * 3.0F)) / (int) (this.main.length * 3.0F) * this.main.numsegments0);
        for (Iterator iterator = this.main.segments.iterator(); iterator.hasNext(); ) {
            FXLightningBoltCommon.Segment rendersegment = (FXLightningBoltCommon.Segment) iterator.next();
            if (rendersegment.segmentno <= renderlength) {
                float width = (float) (this.width * (getRelativeViewVector(rendersegment.startpoint.point).length() / 5.0F + 1.0F) * (1.0F + rendersegment.light) * 0.5F);
                Vector3 diff1 = Vector3.crossProduct(playervec, rendersegment.prevdiff).multiply(width / rendersegment.sinprev);
                Vector3 diff2 = Vector3.crossProduct(playervec, rendersegment.nextdiff).multiply(width / rendersegment.sinnext);
                Vector3 startvec = rendersegment.startpoint.point;
                Vector3 endvec = rendersegment.endpoint.point;
                float rx1 = (float) (startvec.getX() - interpPosX);
                float ry1 = (float) (startvec.getY() - interpPosY);
                float rz1 = (float) (startvec.getZ() - interpPosZ);
                float rx2 = (float) (endvec.getX() - interpPosX);
                float ry2 = (float) (endvec.getY() - interpPosY);
                float rz2 = (float) (endvec.getZ() - interpPosZ);
                int index = worldRenderer.getColorIndex(2);
                worldRenderer.putColorRGBA(index, (int) (this.particleRed * 255), (int) (this.particleGreen * 255), (int) (this.particleBlue * 255), (int) ((mainalpha * rendersegment.light) * 255));
                worldRenderer.pos(rx2 - diff2.getX(), ry2 - diff2.getY(), rz2 - diff2.getZ()).tex(0.5D, 0.0D);
                worldRenderer.pos(rx1 - diff1.getX(), ry1 - diff1.getY(), rz1 - diff1.getZ()).tex(0.5D, 0.0D);
                worldRenderer.pos(rx1 + diff1.getX(), ry1 + diff1.getY(), rz1 + diff1.getZ()).tex(0.5D, 1.0D);
                worldRenderer.pos(rx2 + diff2.getX(), ry2 + diff2.getY(), rz2 + diff2.getZ()).tex(0.5D, 1.0D);
                if (rendersegment.next == null) {
                    Vector3 roundend = rendersegment.endpoint.point.clone().add(rendersegment.diff.clone().normalize().multiply(width));
                    float rx3 = (float) (roundend.getX() - interpPosX);
                    float ry3 = (float) (roundend.getY() - interpPosY);
                    float rz3 = (float) (roundend.getZ() - interpPosZ);
                    worldRenderer.pos(rx3 - diff2.getX(), ry3 - diff2.getY(), rz3 - diff2.getZ()).tex(0.0D, 0.0D);
                    worldRenderer.pos(rx2 - diff2.getX(), ry2 - diff2.getY(), rz2 - diff2.getZ()).tex(0.5D, 0.0D);
                    worldRenderer.pos(rx2 + diff2.getX(), ry2 + diff2.getY(), rz2 + diff2.getZ()).tex(0.5D, 1.0D);
                    worldRenderer.pos(rx3 + diff2.getX(), ry3 + diff2.getY(), rz3 + diff2.getZ()).tex(0.0D, 1.0D);
                }
                if (rendersegment.prev == null) {
                    Vector3 roundend = rendersegment.startpoint.point.clone().subtract(rendersegment.diff.clone().normalize().multiply(width));
                    float rx3 = (float) (roundend.getX() - interpPosX);
                    float ry3 = (float) (roundend.getY() - interpPosY);
                    float rz3 = (float) (roundend.getZ() - interpPosZ);
                    worldRenderer.pos(rx1 - diff1.getX(), ry1 - diff1.getY(), rz1 - diff1.getZ()).tex(0.5D, 0.0D);
                    worldRenderer.pos(rx3 - diff1.getX(), ry3 - diff1.getY(), rz3 - diff1.getZ()).tex(0.0D, 0.0D);
                    worldRenderer.pos(rx3 + diff1.getX(), ry3 + diff1.getY(), rz3 + diff1.getZ()).tex(0.0D, 1.0D);
                    worldRenderer.pos(rx1 + diff1.getX(), ry1 + diff1.getY(), rz1 + diff1.getZ()).tex(0.5D, 1.0D);
                }
            }
        }
    }

    @Override
    public void renderParticle(WorldRenderer worldRendererIn, Entity entityIn, float partialframe, float cosyaw, float cospitch, float sinyaw, float sinsinpitch, float cossinpitch) {
        EntityPlayer renderentity = FMLClientHandler.instance().getClient().thePlayer;
        int visibleDistance = 100;
        if (!FMLClientHandler.instance().getClient().gameSettings.fancyGraphics) {
            visibleDistance = 50;
        }
        if (renderentity.getDistance(this.posX, this.posY, this.posZ) > visibleDistance) {
            return;
        }
        VertexFormat oldFormat = worldRendererIn.getVertexFormat();
        int oldMode = worldRendererIn.getDrawMode();
        Tessellator tessellator = Tessellator.getInstance();
        tessellator.draw();
        GL11.glPushMatrix();

        GL11.glDepthMask(false);
        GL11.glEnable(3042);

        this.particleRed = (this.particleGreen = this.particleBlue = 1.0F);
        float ma = 1.0F;
        switch (this.type) {
            case 0:
                this.particleRed = 0.6F;
                this.particleGreen = 0.3F;
                this.particleBlue = 0.6F;
                GL11.glBlendFunc(770, 1);
                break;
            case 1:
                this.particleRed = 0.6F;
                this.particleGreen = 0.6F;
                this.particleBlue = 0.1F;
                GL11.glBlendFunc(770, 1);
                break;
            case 2:
                this.particleRed = 0.1F;
                this.particleGreen = 0.1F;
                this.particleBlue = 0.6F;
                GL11.glBlendFunc(770, 1);
                break;
            case 3:
                this.particleRed = 0.1F;
                this.particleGreen = 1.0F;
                this.particleBlue = 0.1F;
                GL11.glBlendFunc(770, 1);
                break;
            case 4:
                this.particleRed = 0.6F;
                this.particleGreen = 0.1F;
                this.particleBlue = 0.1F;
                GL11.glBlendFunc(770, 1);
                break;
            case 5:
                this.particleRed = 0.6F;
                this.particleGreen = 0.2F;
                this.particleBlue = 0.6F;
                GL11.glBlendFunc(770, 771);
                break;
            case 6:
                this.particleRed = 0.75F;
                this.particleGreen = 1.0F;
                this.particleBlue = 1.0F;
                ma = 0.2F;
                GL11.glBlendFunc(770, 771);
        }
        Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation("thaumcraft", "textures/misc/p_large.png"));
        worldRendererIn.begin(7, DefaultVertexFormats.POSITION_TEX);
        //worldRendererIn.putBrightness4();
        //tessellator.setBrightness(15728880);
        renderBolt(worldRendererIn, partialframe, cosyaw, cospitch, sinyaw, cossinpitch, 0, ma);
        tessellator.draw();
        switch (this.type) {
            case 0:
                this.particleRed = 1.0F;
                this.particleGreen = 0.6F;
                this.particleBlue = 1.0F;
                break;
            case 1:
                this.particleRed = 1.0F;
                this.particleGreen = 1.0F;
                this.particleBlue = 0.1F;
                break;
            case 2:
                this.particleRed = 0.1F;
                this.particleGreen = 0.1F;
                this.particleBlue = 1.0F;
                break;
            case 3:
                this.particleRed = 0.1F;
                this.particleGreen = 0.6F;
                this.particleBlue = 0.1F;
                break;
            case 4:
                this.particleRed = 1.0F;
                this.particleGreen = 0.1F;
                this.particleBlue = 0.1F;
                break;
            case 5:
                this.particleRed = 0.0F;
                this.particleGreen = 0.0F;
                this.particleBlue = 0.0F;
                GL11.glBlendFunc(770, 771);
                break;
            case 6:
                this.particleRed = 0.75F;
                this.particleGreen = 1.0F;
                this.particleBlue = 1.0F;
                ma = 0.2F;
                GL11.glBlendFunc(770, 771);
        }
        Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation("thaumcraft", "textures/misc/p_small.png"));
        worldRendererIn.begin(7, DefaultVertexFormats.POSITION_TEX);
        //tessellator.setBrightness(15728880);
        renderBolt(worldRendererIn, partialframe, cosyaw, cospitch, sinyaw, cossinpitch, 1, ma);
        tessellator.draw();

        GL11.glDisable(3042);
        GL11.glDepthMask(true);
        GL11.glPopMatrix();

        Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation("minecraft", "textures/particle/particles.png"));

        worldRendererIn.begin(oldMode, oldFormat);
        //tessellator.startDrawingQuads();
    }
}