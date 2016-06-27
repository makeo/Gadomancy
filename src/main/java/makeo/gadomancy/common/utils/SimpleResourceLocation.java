package makeo.gadomancy.common.utils;

import makeo.gadomancy.common.Gadomancy;
import net.minecraft.util.ResourceLocation;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 21.05.2015 15:13
 */
public class SimpleResourceLocation extends ResourceLocation {

    public SimpleResourceLocation(String file) {
        super(Gadomancy.MODID.toLowerCase(), "textures/" + (file.startsWith("/") ? file.substring(1) : file));
    }

}
