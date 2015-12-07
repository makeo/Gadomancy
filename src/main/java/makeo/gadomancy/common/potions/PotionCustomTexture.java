package makeo.gadomancy.common.potions;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;

import java.awt.Color;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by HellFirePvP @ 07.12.2015 20:31
 */
public abstract class PotionCustomTexture extends Potion {

    private ResourceLocation texture;

    protected PotionCustomTexture(int id, boolean isBad, int color, ResourceLocation texture) {
        super(id, isBad, color);
        this.texture = texture;
    }

    @Override
    public boolean hasStatusIcon() {
        return false;
    }

    @Override
    public void renderInventoryEffect(int x, int y, PotionEffect effect, Minecraft mc) {
        Tessellator tes = Tessellator.instance;
        mc.renderEngine.bindTexture(texture);
        tes.startDrawingQuads();
        Color c = new Color(getLiquidColor());
        tes.setColorRGBA(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
        tes.addVertexWithUV(x + 6, y + 7, 0, 0, 0);
        tes.addVertexWithUV(x + 6,      y + 7 + 18, 0, 0, 1);
        tes.addVertexWithUV(x + 6 + 18, y + 7 + 18, 0, 1, 1);
        tes.addVertexWithUV(x + 6 + 18, y + 7, 0, 1, 0);
        tes.draw();
    }

}
