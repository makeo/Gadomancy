package makeo.gadomancy.client.textures;

import makeo.gadomancy.api.GadomancyApi;
import makeo.gadomancy.api.golems.AdditionalGolemType;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.data.TextureMetadataSection;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import thaumcraft.common.entities.golems.EnumGolemType;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 07.09.2015 18:11
 */
public class GolemGuiTexture extends BaseTexture {
    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public int getGlTextureId() {
        return super.getGlTextureId();
    }

    public GolemGuiTexture(ResourceLocation textureLocation) {
        super(textureLocation);
    }

    @Override
    public void loadTexture(IResourceManager resourceManager) throws IOException {
        this.deleteGlTexture();
        InputStream inputstream = null;

        try
        {
            IResource iresource = resourceManager.getResource(this.textureLocation);
            inputstream = iresource.getInputStream();
            BufferedImage bufferedimage = manipulateImage(resourceManager, ImageIO.read(inputstream));

            boolean flag = false;
            boolean flag1 = false;

            if (iresource.hasMetadata())
            {
                try
                {
                    TextureMetadataSection texturemetadatasection = (TextureMetadataSection)iresource.getMetadata("texture");

                    if (texturemetadatasection != null)
                    {
                        flag = texturemetadatasection.getTextureBlur();
                        flag1 = texturemetadatasection.getTextureClamp();
                    }
                }
                catch (RuntimeException runtimeexception)
                {
                    LOGGER.warn("Failed reading metadata of: " + this.textureLocation, runtimeexception);
                }
            }

            TextureUtil.uploadTextureImageAllocate(this.getGlTextureId(), bufferedimage, flag, flag1);
        }
        finally
        {
            if (inputstream != null)
            {
                inputstream.close();
            }
        }
    }

    private BufferedImage manipulateImage(IResourceManager resourceManager, BufferedImage image) throws IOException {
        int scale = image.getWidth() / 256;

        List<AdditionalGolemType> types = GadomancyApi.getAdditionalGolemTypes();

        int newHeight = (24*scale) * (getMaxOrdinal() + 1);
        BufferedImage newImg = new BufferedImage(image.getWidth(), newHeight < image.getHeight() ? image.getHeight() : newHeight, image.getType());

        for(int x = 0; x < image.getWidth(); x++) {
            for(int y = 0; y < image.getHeight(); y++) {
                newImg.setRGB(x, y, image.getRGB(x, y));
            }
        }

        for(AdditionalGolemType type : types) {
            try {
                int ordinal = type.getEnumEntry().ordinal();
                BufferedImage slotImg = loadImage(resourceManager, type.getInvSlotTexture());

                float slotScale = (24*scale) / (float)slotImg.getWidth();

                if(slotScale > 1) {
                    slotImg = scaleImage(slotImg, slotScale, slotScale);
                }

                for(int x = 0; x < slotImg.getWidth(); x++) {
                    for(int y = 0; y < slotImg.getHeight(); y++) {
                        newImg.setRGB((184*scale) + x, ordinal * (24*scale) + y, slotImg.getRGB(x, y));
                    }
                }
            } catch (IOException e) {
                LOGGER.warn("Failed loading golem inventory slot texture of: " + type.getModId() + ":" + type.getName());
            }
        }
        return newImg;
    }

    private BufferedImage scaleImage(BufferedImage input, float xScale, float yScale) {
        int newX = (int) (input.getWidth()*xScale);
        int newY = (int) (input.getHeight()*yScale);

        Image temp = input.getScaledInstance(newX, newY, Image.SCALE_REPLICATE);

        BufferedImage img = new BufferedImage(newX, newY, input.getType());

        Graphics2D graphics = img.createGraphics();
        graphics.drawImage(temp, 0, 0, null);
        graphics.dispose();

        return img;
    }

    private int getMaxOrdinal() {
        int max = 0;
        for(EnumGolemType type : EnumGolemType.values()) {
            if(type.ordinal() > max) {
                max = type.ordinal();
            }
        }
        return max;
    }
}






















