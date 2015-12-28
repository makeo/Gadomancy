package makeo.gadomancy.coremod;

import cpw.mods.fml.common.asm.transformers.AccessTransformer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

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
    public static final String NAME_NODE_RENDERER = "thaumcraft.client.renderers.tile.TileNodeRenderer";

    public GadomancyTransformer() throws IOException {}

    @Override
    public byte[] transform(String name, String transformedName, byte[] bytes) {
        boolean needsTransform = name.equalsIgnoreCase(NAME_ENCHANTMENT_HELPER) ||
                name.equalsIgnoreCase(NAME_WANDMANAGER) || name.equalsIgnoreCase(NAME_NODE_RENDERER);
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
        } else if(name.equalsIgnoreCase(NAME_NODE_RENDERER)) {
            for(MethodNode mn : node.methods) {
                if(mn.name.equals("renderTileEntityAt")) {
                    InsnList setBefore = new InsnList();
                    setBefore.add(new VarInsnNode(Opcodes.ALOAD, 1));
                    setBefore.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "makeo/gadomancy/common/events/EventHandlerRedirect",
                            "preNodeRender", "(Lnet/minecraft/tileentity/TileEntity;)V", false));

                    mn.instructions.insertBefore(mn.instructions.get(0), setBefore);

                    InsnList setAfter = new InsnList();
                    setBefore.add(new VarInsnNode(Opcodes.ALOAD, 1));
                    setBefore.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "makeo/gadomancy/common/events/EventHandlerRedirect",
                            "postNodeRender", "(Lnet/minecraft/tileentity/TileEntity;)V", false));

                    for(int i = 0; i < mn.instructions.size(); i++) {
                        AbstractInsnNode insnNode = mn.instructions.get(i);
                        if(insnNode.getOpcode() == Opcodes.RETURN) {
                            mn.instructions.insertBefore(insnNode, setAfter);
                        }
                    }
                }
            }
        }

        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        node.accept(writer);
        bytes = writer.toByteArray();
        return super.transform(name, transformedName, bytes);
    }
}
