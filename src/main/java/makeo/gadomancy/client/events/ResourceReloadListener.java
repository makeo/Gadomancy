package makeo.gadomancy.client.events;

import makeo.gadomancy.api.GadomancyApi;
import makeo.gadomancy.api.golems.AdditionalGolemType;
import makeo.gadomancy.client.textures.GolemGuiTexture;
import makeo.gadomancy.common.Gadomancy;
import makeo.gadomancy.common.utils.Injector;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
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
public class ResourceReloadListener implements IResourceManagerReloadListener {

    public static final ResourceReloadListener instance = new ResourceReloadListener();
    public static ResourceReloadListener getInstance() {
        return instance;
    }

    public static Map languageList;

    private ResourceReloadListener() {
        Injector instance = new Injector(new Injector(StringTranslate.class)
                .getField(Injector.findField(StringTranslate.class, StringTranslate.class)));
        languageList = instance.getField(instance.findField(Map.class));
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        reloadGolemResources();
    }

    public void reloadGolemResources() {
        manipulateLanguageFile();

        Gadomancy.proxy.runDelayedClientSide(new Runnable() {
            @Override
            public void run() {
                registerTextureOverride();
            }
        });
    }

    private void manipulateLanguageFile() {
        if(languageList != null) {
            for(AdditionalGolemType type : GadomancyApi.getAdditionalGolemTypes()) {
                languageList.put("item.ItemGolemPlacer." + type.getEnumEntry().ordinal() + ".name",
                        StatCollector.translateToLocal(type.getUnlocalizedName() + ".name"));
            }
        }
    }

    private static final ResourceLocation TC_GOLEM_GUI_TEXTURE = new ResourceLocation("thaumcraft", "textures/gui/guigolem.png");

    private void registerTextureOverride() {
        Minecraft.getMinecraft().renderEngine.loadTexture(TC_GOLEM_GUI_TEXTURE, new GolemGuiTexture(TC_GOLEM_GUI_TEXTURE));
    }
}
