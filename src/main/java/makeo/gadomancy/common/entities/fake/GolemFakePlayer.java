package makeo.gadomancy.common.entities.fake;

import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.WorldServer;
import thaumcraft.common.entities.golems.EntityGolemBase;

import java.util.Collection;
import java.util.UUID;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 19.09.2015 18:02
 */
public class GolemFakePlayer extends AdvancedFakePlayer {
    private EntityGolemBase golem;

    public GolemFakePlayer(WorldServer world, EntityGolemBase golem, UUID uuid) {
        super(world, uuid);

        this.golem = golem;
    }

    public GolemFakePlayer(WorldServer world, EntityGolemBase golem) {
        this(world, golem, AdvancedFakePlayer.DEFAULT_UUID);
    }

    @Override
    public Collection getActivePotionEffects() {
        return golem.getActivePotionEffects();
    }

    @Override
    public boolean isPotionActive(int p_82165_1_) {
        return golem.isPotionActive(p_82165_1_);
    }

    @Override
    public boolean isPotionActive(Potion p_70644_1_) {
        return golem.isPotionActive(p_70644_1_);
    }

    @Override
    public void clearActivePotions() {
        golem.clearActivePotions();
    }

    @Override
    public void addPotionEffect(PotionEffect p_70690_1_) {
        golem.addPotionEffect(p_70690_1_);
    }

    @Override
    public boolean isPotionApplicable(PotionEffect p_70687_1_) {
        return golem.isPotionApplicable(p_70687_1_);
    }

    @Override
    public void removePotionEffect(int p_82170_1_) {
        golem.removePotionEffect(p_82170_1_);
    }

    @Override
    public void removePotionEffectClient(int p_70618_1_) {
        golem.removePotionEffectClient(p_70618_1_);
    }

    @Override
    public PotionEffect getActivePotionEffect(Potion p_70660_1_) {
        return golem.getActivePotionEffect(p_70660_1_);
    }

    @Override
    public void curePotionEffects(ItemStack curativeItem) {
        golem.curePotionEffects(curativeItem);
    }

    @Override
    protected void updatePotionEffects() {
    }
}
