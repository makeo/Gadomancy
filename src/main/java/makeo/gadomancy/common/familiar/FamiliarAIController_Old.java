package makeo.gadomancy.common.familiar;

import baubles.api.BaublesApi;
import makeo.gadomancy.common.registration.RegisteredFamiliarAI_Old;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by HellFirePvP @ 31.10.2015 14:08
 */
public class FamiliarAIController_Old {

    private static final Random RAND = new Random();

    private static Map<EntityPlayer, LinkedList<EntityLivingBase>> targetMap = new HashMap<EntityPlayer, LinkedList<EntityLivingBase>>();

    private List<FamiliarAIProcess_Old> availableTasks = new ArrayList<FamiliarAIProcess_Old>();
    private Map<FamiliarAIProcess_Old, Integer> cooldownProcesses = new HashMap<FamiliarAIProcess_Old, Integer>();
    private LinkedList<FamiliarAIProcess_Old> requestedLoop = new LinkedList<FamiliarAIProcess_Old>();
    private FamiliarAIProcess_Old runningTask;
    private int ticksInTask;
    private EntityPlayer owningPlayer;

    public FamiliarAIController_Old(EntityPlayer owningPlayer) {
        this.owningPlayer = owningPlayer;
    }

    public void registerDefaultTasks() {
        this.availableTasks.add(RegisteredFamiliarAI_Old.familiarAIIdle);
        this.availableTasks.add(RegisteredFamiliarAI_Old.familiarAIZapAttackingMonsters);
    }

    public void scheduleTick() {
        reduceRunningCooldowns();
        if(runningTask == null) {
            selectNewTask();
        } else {
            ItemStack famStack = BaublesApi.getBaubles(owningPlayer).getStackInSlot(0);
            runningTask.tick(ticksInTask, owningPlayer.worldObj, owningPlayer, famStack);
            ticksInTask++;
            if(ticksInTask >= runningTask.getDuration()) {
                if(runningTask.getCooldownDuration(famStack) > 0) cooldownProcesses.put(runningTask, runningTask.getCooldownDuration(famStack));
                if(runningTask.tryLoop() && !requestedLoop.contains(runningTask)) requestedLoop.addLast(runningTask);
                runningTask = null;
                ticksInTask = 0;
            }
        }
    }

    private void selectNewTask() {
        if(!requestedLoop.isEmpty()) {
            Iterator<FamiliarAIProcess_Old> it = requestedLoop.iterator();
            while(it.hasNext()) {
                FamiliarAIProcess_Old process = it.next();
                if(!cooldownProcesses.containsKey(process)) {
                    runningTask = process;
                    it.remove();
                    return;
                }
            }
        }
        int size = availableTasks.size();
        int randIndex = RAND.nextInt(size);
        for (int i = 0; i < size; i++) {
            int index = (randIndex + i) % size;
            FamiliarAIProcess_Old process = availableTasks.get(index);
            if(process.canRun(owningPlayer.worldObj, owningPlayer.posX, owningPlayer.posY, owningPlayer.posZ, owningPlayer, BaublesApi.getBaubles(owningPlayer).getStackInSlot(0)) &&
                    !cooldownProcesses.containsKey(process)) {
                runningTask = process;
            }
        }
    }

    private void reduceRunningCooldowns() {
        Iterator<FamiliarAIProcess_Old> itProcesses = cooldownProcesses.keySet().iterator();
        while(itProcesses.hasNext()) {
            FamiliarAIProcess_Old process = itProcesses.next();
            int cd = cooldownProcesses.get(process) - 1;
            if(cd <= 0) {
                itProcesses.remove();
            } else {
                cooldownProcesses.put(process, cd);
            }
        }
    }

    public EntityPlayer getOwningPlayer() {
        return owningPlayer;
    }

    public static boolean hasLastTargetter(EntityPlayer player) {
        return targetMap.containsKey(player) && targetMap.get(player).size() >= 1;
    }

    public static LinkedList<EntityLivingBase> getLastTargetters(EntityPlayer player) {
        if(!targetMap.containsKey(player)) return null;
        if(targetMap.get(player).size() < 1) return null;
        return targetMap.get(player);
    }

    public static void cleanTargetterList(EntityPlayer player) {
        if(!targetMap.containsKey(player)) return;
        Iterator<EntityLivingBase> it = targetMap.get(player).iterator();
        while(it.hasNext()) {
            EntityLivingBase living = it.next();
            if(living.isDead) it.remove();
        }
    }

    public static void notifyTargetEvent(EntityLivingBase targetter, EntityPlayer targetted) {
        LinkedList<EntityLivingBase> targetters;
        boolean needsUpdate = false;
        if(targetMap.containsKey(targetted)) {
            targetters = targetMap.get(targetted);
        } else {
            targetters = new LinkedList<EntityLivingBase>();
            needsUpdate = true;
        }

        if(!targetters.contains(targetter)) {
            targetters.addLast(targetter);
        }
        if(needsUpdate) {
            targetMap.put(targetted, targetters);
        }
    }
}
