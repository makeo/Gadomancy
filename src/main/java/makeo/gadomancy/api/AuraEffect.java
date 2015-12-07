package makeo.gadomancy.api;

import net.minecraft.entity.Entity;
import thaumcraft.api.aspects.Aspect;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by HellFirePvP @ 17.11.2015 14:36
 */
public abstract class AuraEffect {

    public abstract boolean isEntityApplicable(Entity e);

    public abstract void applyEffect(Entity e);

    public int getTickInterval() {
        return 8;
    }

    public AuraEffect register(Aspect a) {
        GadomancyApi.registerAdditionalAuraEffect(a, this);
        return this;
    }

}
