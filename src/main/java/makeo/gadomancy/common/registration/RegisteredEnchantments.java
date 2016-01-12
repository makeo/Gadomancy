package makeo.gadomancy.common.registration;

import makeo.gadomancy.common.Gadomancy;
import makeo.gadomancy.common.data.config.ModConfig;
import makeo.gadomancy.common.enchantments.EnchantmentRevealer;
import makeo.gadomancy.common.utils.Injector;
import net.minecraft.enchantment.Enchantment;

import java.lang.reflect.Field;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by makeo @ 28.12.2015 15:52
 */
public class RegisteredEnchantments {
    public static EnchantmentRevealer revealer;

    public static void init() {
        revealer = registerEnchantment(EnchantmentRevealer.class);
    }

    public static void createConfigEntries() {
        for(Field field : RegisteredEnchantments.class.getFields()) {
            if(Enchantment.class.isAssignableFrom(field.getType())) {
                ModConfig.loadEnchantmentId(field.getType().getSimpleName());
            }
        }
    }

    public static <T extends Enchantment> T registerEnchantment(Class<T> enchClass) {
        int id = ModConfig.loadEnchantmentId(enchClass.getSimpleName());
        if(id == -1) {
            id = getUnassignedId(enchClass);
        }
        return new Injector(enchClass).invokeConstructor(int.class, id);
    }

    private static int getUnassignedId(Class<? extends Enchantment> enchClass) {
        for(int i = 0; i < Enchantment.enchantmentsList.length; i++) {
            if(Enchantment.enchantmentsList[i] == null) {
                return i;
            }
        }
        throw new RuntimeException("Failed to find free id for " + Gadomancy.NAME + " enchantment (" + enchClass.getName() + ")! Please consider using the config option to change the enchantment id.");
    }
}
