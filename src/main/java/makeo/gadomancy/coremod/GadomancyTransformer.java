package makeo.gadomancy.coremod;

import cpw.mods.fml.common.asm.transformers.AccessTransformer;

import net.minecraft.entity.EntityLivingBase;
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
    public GadomancyTransformer() throws IOException {
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] bytes) {
        if(name.equalsIgnoreCase("net.minecraft.enchantment.EnchantmentHelper")) {
            ClassNode node = new ClassNode();
            ClassReader reader = new ClassReader(bytes);
            reader.accept(node, 0);

            for (MethodNode mn : node.methods) {
                if(mn.name.equals("getFortuneModifier") || mn.name.equals("func_77517_e")) {
                    mn.instructions = new InsnList();

                    mn.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    mn.instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "makeo/gadomancy/common/events/EventHandlerEntity",
                            "onGetFortuneLevel", "(Lnet/minecraft/entity/EntityLivingBase;)I", false));
                    mn.instructions.add(new InsnNode(Opcodes.IRETURN));
                }
            }

            ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            node.accept(writer);
            bytes = writer.toByteArray();
        }
        return super.transform(name, transformedName, bytes);
    }
}
