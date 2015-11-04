package makeo.gadomancy.common.entities.ai;

import makeo.gadomancy.common.utils.Injector;
import net.minecraft.entity.EntityLivingBase;
import thaumcraft.common.entities.ai.combat.AIDartAttack;
import thaumcraft.common.entities.golems.EntityGolemBase;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by makeo @ 31.10.2015 20:03
 */
public class AIUncheckedDartAttack extends AIDartAttack {
    private static final Injector INJECTOR = new Injector(AIDartAttack.class);
    private EntityGolemBase golem;

    public AIUncheckedDartAttack(EntityGolemBase golem) {
        super(golem);
        this.golem = golem;
    }

    @Override
    public boolean shouldExecute() {
        EntityLivingBase target = golem.getAttackTarget();
        if (target == null) {
            return false;
        }

        double ra = golem.getDistanceSq(target.posX, target.boundingBox.minY, target.posZ);
        if (ra < 9.0D) {
            return false;
        }

        INJECTOR.setObject(this);
        INJECTOR.setField("attackTarget", target);

        return true;

    }
}
