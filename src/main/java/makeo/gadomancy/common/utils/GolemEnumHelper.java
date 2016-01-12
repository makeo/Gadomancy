package makeo.gadomancy.common.utils;

import cpw.mods.fml.relauncher.Side;
import makeo.gadomancy.api.GadomancyApi;
import makeo.gadomancy.api.golems.AdditionalGolemType;
import makeo.gadomancy.client.events.ResourceReloadListener;
import makeo.gadomancy.common.Gadomancy;
import makeo.gadomancy.common.entities.golems.types.RemovedGolemType;
import net.minecraftforge.common.util.EnumHelper;
import thaumcraft.common.entities.golems.EnumGolemType;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 29.07.2015 01:07
 */
public class GolemEnumHelper {
    private static final RemovedGolemType REMOVED_GOLEM_TYPE = new RemovedGolemType();
    private static final Injector INJECTOR = new Injector(EnumGolemType.class);
    private static final Injector ENUM_INJECTOR = new Injector(Enum.class);
    private static final Injector HELPER_INJECTOR = new Injector(EnumHelper.class);

    private static Field valuesField = null;
    private static Field getValuesField() {
        if(valuesField == null) {
            for(Field field : EnumGolemType.class.getDeclaredFields()) {
                String name = field.getName();
                if (name.equals("$VALUES") || name.equals("ENUM$VALUES")) //Added 'ENUM$VALUES' because Eclipse's internal compiler doesn't follow standards
                {
                    valuesField = field;
                    valuesField.setAccessible(true);
                    break;
                }
            }
        }
        return valuesField;
    }

    private static final Class[] ENUM_PARAMS = {int.class, int.class, float.class, boolean.class, int.class,
            int.class, int.class, int.class};

    private static Field ordinalField = null;
    public static Field getOrdinalField() {
        if(ordinalField == null) {
            try {
                ordinalField = Enum.class.getDeclaredField("ordinal");
            } catch (NoSuchFieldException ignored) { }
        }
        return ordinalField;
    }

    private static final Class[] MAKE_ENUM_PARAMS = new Class[]{ Class.class, String.class, int.class, Class[].class, Object[].class };

    private static EnumGolemType createEnum(String name, int ordinal, AdditionalGolemType type) {
        return HELPER_INJECTOR.invokeMethod("makeEnum", MAKE_ENUM_PARAMS,
                EnumGolemType.class, name, ordinal, ENUM_PARAMS, new Object[]{ type.maxHealth, type.armor, type.movementSpeed,
                        type.fireResist, type.upgradeAmount, type.carryLimit, type.regenDelay, type.strength });
    }

    private static void addEnum(int ordinal, EnumGolemType type) {
        EnumGolemType[] values = resizeValues(ordinal + 1);;
        values[ordinal] = type;
    }

    private static EnumGolemType addEnum(String name, int ordinal, AdditionalGolemType type) {
        EnumGolemType enumEntry = createEnum(name, ordinal, type);
        addEnum(ordinal, enumEntry);
        type.setEnumEntry(enumEntry);
        return enumEntry;
    }

    private static EnumGolemType[] resizeValues(int size) {
        EnumGolemType[] values = INJECTOR.getField(getValuesField());
        if(size > values.length) {
            EnumGolemType[] newValues = new EnumGolemType[size];
            System.arraycopy(values, 0, newValues, 0, values.length);

            for(int i = values.length; i < newValues.length; i++) {
                newValues[i] = createEnum("REMOVED", i, REMOVED_GOLEM_TYPE);
            }
            setValues(newValues);
            return newValues;
        }
        return values;
    }

    private static void setValues(EnumGolemType[] values) {
        try {
            EnumHelper.setFailsafeFieldValue(getValuesField(), null, values);
        } catch (Exception ignored) { }
    }

    public static EnumGolemType addGolemType(String name, AdditionalGolemType type) {
        return addEnum(name, getOrdinal(name), type);
    }

    private static int getOrdinal(String name) {
        Map<String, Integer> map = getCurrentMapping();

        if(map.containsKey(name)) {
            return map.get(name);
        }

        int returnVal = -1;
        int i = calcDefaultGolemCount();
        do {
            boolean contains = false;
            for(Map.Entry<String, Integer> entry : map.entrySet()) {
                if(entry.getValue() == i) {
                    contains = true;
                }
            }

            if(!contains) {
                returnVal = i;
            }
            i++;
        } while (returnVal < 0);

        map.put(name, returnVal);
        saveCurrentMapping(map);

        return returnVal;
    }

    public static void reorderEnum() {
        reorderEnum(getCurrentMapping());
    }

    public static void reorderEnum(Map<String, Integer> mapping) {
        EnumGolemType[] oldValues = EnumGolemType.values();
        resetEnum();
        for(Map.Entry<String, Integer> entry : mapping.entrySet()) {
            for(EnumGolemType type : oldValues) {
                if(type.name().equals(entry.getKey())) {
                    ENUM_INJECTOR.setObject(type);
                    ENUM_INJECTOR.setField(getOrdinalField(), entry.getValue());
                    addEnum(entry.getValue(), type);
                }
            }
        }

        new Injector(EnumGolemType.class).setField("codeToTypeMapping", null);
        if(Gadomancy.proxy.getSide() == Side.CLIENT) {
            ResourceReloadListener.getInstance().reloadGolemResources();
        }
    }

    private static void resetEnum() {
        EnumGolemType[] newValues = Arrays.copyOfRange(EnumGolemType.values(), 0, calcDefaultGolemCount());
        setValues(newValues);
    }

    private static int calcDefaultGolemCount() {
        EnumGolemType[] values = EnumGolemType.values();
        for(int i = 0; i < values.length; i++) {
            if(GadomancyApi.isAdditionalGolemType(values[i])) {
                return i;
            }
        }
        return values.length;
    }

    public static void validateSavedMapping() {
        if(hasCurrentMapping()) {
            Map<String, Integer> mapping = getCurrentMapping();
            for(Map.Entry<String, Integer> entry : defaultMapping.entrySet()) {
                if(!mapping.containsKey(entry.getKey())) {
                    mapping.put(entry.getKey(), getOrdinal(entry.getKey()));
                }
            }
        }
    }

    private static Map<String, Integer> defaultMapping = new HashMap<String, Integer>();

    public static Map<String, Integer> getCurrentMapping() {
        if(hasCurrentMapping()) {
            return Gadomancy.getModData().get("GolemTypeMapping", new HashMap<String, Integer>());
        } else if(Gadomancy.getModData() != null) {
            Gadomancy.getModData().set("GolemTypeMapping", defaultMapping);
        }
        return defaultMapping;
    }

    public static boolean hasCurrentMapping() {
        return Gadomancy.getModData() != null && Gadomancy.getModData().contains("GolemTypeMapping");
    }

    private static void saveCurrentMapping(Map<String, Integer> map) {
        if(Gadomancy.getModData() != null) {
            Gadomancy.getModData().set("GolemTypeMapping", map);
        }
    }
}
