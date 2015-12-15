package makeo.gadomancy.coremod;

import cpw.mods.fml.common.asm.transformers.AccessTransformer;

import net.minecraft.entity.player.EntityPlayer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;
import thaumcraft.api.aspects.Aspect;

import java.io.IOException;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by makeo @ 07.12.2015 21:48
 */
public class GadomancyTransformer extends AccessTransformer {

    public static final String NAME_ENCHANTMENT_HELPER = "net.minecraft.enchantment.EnchantmentHelper";
    public static final String NAME_WANDMANAGER = "thaumcraft.common.items.wands.WandManager";

    public GadomancyTransformer() throws IOException {}

    @Override
    public byte[] transform(String name, String transformedName, byte[] bytes) {
        boolean needsTransform = name.equalsIgnoreCase(NAME_ENCHANTMENT_HELPER) ||
                name.equalsIgnoreCase(NAME_WANDMANAGER);
        if(!needsTransform) return super.transform(name, transformedName, bytes);

        ClassNode node = new ClassNode();
        ClassReader reader = new ClassReader(bytes);
        reader.accept(node, 0);

        if(name.equalsIgnoreCase(NAME_ENCHANTMENT_HELPER)) {
            for (MethodNode mn : node.methods) {
                if(mn.name.equals("getFortuneModifier") || mn.name.equals("func_77517_e")) {
                    mn.instructions = new InsnList();

                    mn.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    mn.instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "makeo/gadomancy/common/events/EventHandlerRedirect",
                            "getFortuneLevel", "(Lnet/minecraft/entity/EntityLivingBase;)I", false));
                    mn.instructions.add(new InsnNode(Opcodes.IRETURN));

                } else if(mn.name.equals("getEnchantmentLevel") || mn.name.equals("func_77506_a")) {
                    mn.instructions = new InsnList();

                    mn.instructions.add(new VarInsnNode(Opcodes.ILOAD, 0));
                    mn.instructions.add(new VarInsnNode(Opcodes.ALOAD, 1));
                    mn.instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "makeo/gadomancy/common/events/EventHandlerRedirect",
                            "onGetEnchantmentLevel", "(ILnet/minecraft/item/ItemStack;)I", false));
                    mn.instructions.add(new InsnNode(Opcodes.IRETURN));
                }
            }
        } else if(name.equalsIgnoreCase(NAME_WANDMANAGER)) {
            for(MethodNode mn : node.methods) {
                if(mn.name.equals("getTotalVisDiscount")) {
                    InsnList updateTotal = new InsnList();

                    updateTotal.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    updateTotal.add(new VarInsnNode(Opcodes.ALOAD, 1));
                    updateTotal.add(new VarInsnNode(Opcodes.ILOAD, 2));
                    updateTotal.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "makeo/gadomancy/common/events/EventHandlerRedirect",
                            "getAdditionalVisDiscount", "(Lnet/minecraft/entity/player/EntityPlayer;Lthaumcraft/api/aspects/Aspect;I)I", false));

                    mn.instructions.insertBefore(mn.instructions.get(mn.instructions.size() - 5), updateTotal);
                }
            }
        }

        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        node.accept(writer);
        bytes = writer.toByteArray();
        return super.transform(name, transformedName, bytes);
    }
}
