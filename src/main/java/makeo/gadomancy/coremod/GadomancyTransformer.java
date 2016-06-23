package makeo.gadomancy.coremod;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.asm.transformers.AccessTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
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
    public static final String NAME_RENDER_EVENT_HANDLER = "thaumcraft.client.lib.RenderEventHandler";
    public static final String NAME_NEI_ITEMPANEL = "codechicken.nei.ItemPanel";
    //public static final String NAME_ENTITY_LIVING_BASE = "net.minecraft.entity.EntityLivingBase";

    public GadomancyTransformer() throws IOException {}

    @Override
    public byte[] transform(String name, String transformedName, byte[] bytes) {
        boolean needsTransform = transformedName.equalsIgnoreCase(NAME_ENCHANTMENT_HELPER) ||
                transformedName.equalsIgnoreCase(NAME_WANDMANAGER) || transformedName.equalsIgnoreCase(NAME_NODE_RENDERER)
                || transformedName.equalsIgnoreCase(NAME_RENDER_EVENT_HANDLER) || transformedName.equals(NAME_NEI_ITEMPANEL)/* ||
                transformedName.equalsIgnoreCase(NAME_ENTITY_LIVING_BASE)*/;
        if(!needsTransform) return super.transform(name, transformedName, bytes);

        FMLLog.info("[GadomancyTransformer] Transforming " + name + ": " + transformedName);

        ClassNode node = new ClassNode();
        ClassReader reader = new ClassReader(bytes);
        reader.accept(node, 0);

        if(transformedName.equalsIgnoreCase(NAME_ENCHANTMENT_HELPER)) {
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
        } else if(transformedName.equalsIgnoreCase(NAME_WANDMANAGER)) {
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
        } else if(transformedName.equalsIgnoreCase(NAME_NODE_RENDERER)) {
            for (MethodNode mn : node.methods) {
                if (mn.name.equals("renderTileEntityAt")) {
                    InsnList setBefore = new InsnList();
                    setBefore.add(new VarInsnNode(Opcodes.ALOAD, 1));
                    setBefore.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "makeo/gadomancy/common/events/EventHandlerRedirect",
                            "preNodeRender", "(Lnet/minecraft/tileentity/TileEntity;)V", false));

                    mn.instructions.insertBefore(mn.instructions.get(0), setBefore);

                    AbstractInsnNode next = mn.instructions.get(0);
                    while (next != null) {
                        AbstractInsnNode insnNode = next;
                        next = insnNode.getNext();

                        if (insnNode.getOpcode() == Opcodes.RETURN) {
                            InsnList setAfter = new InsnList();
                            setAfter.add(new VarInsnNode(Opcodes.ALOAD, 1));
                            setAfter.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "makeo/gadomancy/common/events/EventHandlerRedirect",
                                    "postNodeRender", "(Lnet/minecraft/tileentity/TileEntity;)V", false));
                            mn.instructions.insertBefore(insnNode, setAfter);
                        }
                    }
                }
            }
        } else if(transformedName.equalsIgnoreCase(NAME_RENDER_EVENT_HANDLER)) {
            for(MethodNode mn : node.methods) {
                if (mn.name.equals("blockHighlight")) {
                    InsnList setBefore = new InsnList();
                    setBefore.add(new VarInsnNode(Opcodes.ALOAD, 1));
                    setBefore.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "makeo/gadomancy/common/events/EventHandlerRedirect",
                            "preBlockHighlight", "(Lnet/minecraftforge/client/event/DrawBlockHighlightEvent;)V", false));

                    mn.instructions.insertBefore(mn.instructions.get(0), setBefore);

                    AbstractInsnNode next = mn.instructions.get(0);
                    while(next != null) {
                        AbstractInsnNode insnNode = next;
                        next = insnNode.getNext();

                        if(insnNode.getOpcode() == Opcodes.RETURN) {
                            InsnList setAfter = new InsnList();
                            setAfter.add(new VarInsnNode(Opcodes.ALOAD, 1));
                            setAfter.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "makeo/gadomancy/common/events/EventHandlerRedirect",
                                    "postBlockHighlight", "(Lnet/minecraftforge/client/event/DrawBlockHighlightEvent;)V", false));
                            mn.instructions.insertBefore(insnNode, setAfter);
                        }
                    }
                }
            }
        } else if(transformedName.equalsIgnoreCase(NAME_NEI_ITEMPANEL)) {
            for (MethodNode mn : node.methods) {
                if(mn.name.equals("updateItemList")) {
                    InsnList newInstructions = new InsnList();
                    newInstructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
                    newInstructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "makeo/gadomancy/common/integration/IntegrationNEI",
                            "checkItems", "(Ljava/util/ArrayList;)V", false));
                    newInstructions.add(mn.instructions);
                    mn.instructions = newInstructions;
                }
            }
        }/* else if(transformedName.equalsIgnoreCase(NAME_ENTITY_LIVING_BASE)) {
            FieldNode fn = new FieldNode(Opcodes.ACC_PUBLIC, "ignoreCollisions", Type.BOOLEAN_TYPE.getDescriptor(), null, true);
            node.fields.add(fn);

            for(MethodNode mn : node.methods) {
                if(mn.name.equals("func_70636_d") || mn.name.equals("onLivingUpdate")) {
                    AbstractInsnNode indexNode = null; //To insert after
                    Label foundJumpLabel = null; //The jump in case we don't do collisions

                    for (int i = 0; i < mn.instructions.size() - 3; i++) {
                        AbstractInsnNode inNode = mn.instructions.get(i);
                        if(!(inNode instanceof FieldInsnNode)) continue;
                        if(!((FieldInsnNode) inNode).name.equals("field_70170_p") &&
                                !((FieldInsnNode) inNode).name.equals("worldObj")) continue;

                        inNode = mn.instructions.get(i + 1);
                        if(!(inNode instanceof FieldInsnNode)) continue;
                        if(!((FieldInsnNode) inNode).name.equals("field_72995_K") &&
                                !((FieldInsnNode) inNode).name.equals("isRemote")) continue;

                        inNode = mn.instructions.get(i + 2);
                        if(!(inNode instanceof JumpInsnNode)) continue;
                        if(inNode.getOpcode() != Opcodes.IFNE) continue;
                        foundJumpLabel = ((JumpInsnNode) inNode).label.getLabel();

                        //Index where we have to place our handling after.
                        indexNode = inNode;
                    }

                    if(indexNode == null || foundJumpLabel == null) {
                        FMLLog.bigWarning("Could not add doCollisions to EntityLivingBase!");
                    } else {
                        InsnList addingList = new InsnList();
                        addingList.add(new VarInsnNode(Opcodes.ALOAD, 0));
                        addingList.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/entity/EntityLivingBase", "ignoreCollisions", Type.BOOLEAN_TYPE.getDescriptor()));
                        addingList.add(new JumpInsnNode(Opcodes.IFNE, new LabelNode(foundJumpLabel)));
                        mn.instructions.insert(indexNode, addingList);
                    }
                }
            }
        }*/

        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        node.accept(writer);
        bytes = writer.toByteArray();
        return super.transform(name, transformedName, bytes);
    }
}
