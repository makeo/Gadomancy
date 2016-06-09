package makeo.gadomancy.common.registration;

import com.google.common.collect.Lists;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIEatGrass;
import net.minecraft.entity.passive.EntitySheep;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by HellFirePvP @ 08.06.2016 / 23:12
 */
public class AIShutdownWhitelist {

    private AIShutdownWhitelist() {}

    private static Map<Class<? extends EntityLiving>, List<Class<? extends EntityAIBase>>> whitelistedAI =
            new HashMap<Class<? extends EntityLiving>, List<Class<? extends EntityAIBase>>>();

    public static void whitelistAIClass(Class<? extends EntityLiving> entityClass, Class<? extends EntityAIBase> aiClass) {
        if(!whitelistedAI.containsKey(entityClass)) {
            whitelistedAI.put(entityClass, new LinkedList<Class<? extends EntityAIBase>>());
        }
        whitelistedAI.get(entityClass).add(aiClass);
    }

    public static void whitelistAIClass(EntityLiving entity, Class<? extends EntityAIBase> aiClass) {
        whitelistAIClass(entity.getClass(), aiClass);
    }

    public static List<Class<? extends EntityAIBase>> getWhitelistedAIClasses(EntityLiving entity) {
        if(!whitelistedAI.containsKey(entity.getClass())) return Lists.newArrayList();
        return whitelistedAI.get(entity.getClass());
    }

    static {
        whitelistAIClass(EntitySheep.class, EntityAIEatGrass.class);
    }

}
