package makeo.gadomancy.common.registration;

import cpw.mods.fml.common.registry.EntityRegistry;
import makeo.gadomancy.common.Gadomancy;
import makeo.gadomancy.common.entities.EntityAuraCore;
import makeo.gadomancy.common.entities.EntityItemElement;
import makeo.gadomancy.common.entities.EntityPermNoClipItem;
import makeo.gadomancy.common.entities.player.FakePlayerEntity;
import net.minecraft.entity.Entity;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by HellFirePvP @ 01.12.2015 13:59
 */
public class RegisteredEntities {

    public static void init() {
        registerEntity(EntityPermNoClipItem.class, "EntityPermItem", 32, 40, false);
        registerEntity(EntityAuraCore.class, "EntityAuraCore", 32, 10, true);
        registerEntity(FakePlayerEntity.class, "FakePlayerEntity", 32, 20, true);
        registerEntity(EntityItemElement.class, "EntityItemElement", 32, 40, true);
    }

    private static void registerEntity(Class<? extends Entity> entityClass, String name, int trackingRange, int updateFreq, boolean sendVelUpdates) {
        int id = EntityRegistry.findGlobalUniqueEntityId();
        EntityRegistry.registerGlobalEntityID(entityClass, name, id);
        EntityRegistry.registerModEntity(entityClass, name, id, Gadomancy.instance, trackingRange, updateFreq, sendVelUpdates);
    }
}
