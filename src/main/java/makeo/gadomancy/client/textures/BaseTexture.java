package makeo.gadomancy.client.textures;

import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 04.10.2015 17:56
 */
public abstract class BaseTexture extends SimpleTexture {
    public BaseTexture(ResourceLocation resourceLocation) {
        super(resourceLocation);
    }

    protected BufferedImage loadImage(IResourceManager resourceManager, ResourceLocation location) throws IOException {
        InputStream inputstream = null;

        try {
            IResource iresource = resourceManager.getResource(location);
            inputstream = iresource.getInputStream();
            return ImageIO.read(inputstream);
        }
        finally {
            if (inputstream != null) {
                inputstream.close();
            }
        }
    }
}
