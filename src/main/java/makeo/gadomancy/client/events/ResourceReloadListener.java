package makeo.gadomancy.client.events;

import makeo.gadomancy.common.utils.Injector;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.util.StringTranslate;

import java.util.Map;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 03.10.2015 12:09
 */
public class ResourceReloadListener {

    public static Map languageList;

    private ResourceReloadListener() {}

    public static void loadLanguageList() {
        Injector instance = new Injector(new Injector(StringTranslate.class)
                .getField(Injector.findField(StringTranslate.class, StringTranslate.class)));
        languageList = instance.getField(instance.findField(Map.class));
    }
}
