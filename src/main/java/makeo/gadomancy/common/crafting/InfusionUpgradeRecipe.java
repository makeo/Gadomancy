package makeo.gadomancy.common.crafting;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 *
 * Created by makeo @ 21.07.2015 20:16
 */
public class InfusionUpgradeRecipe /*extends InfusionRecipe*/ {

    /*private static final ItemStack DEFAULT_OUTPUT = new ItemStack(Blocks.dirt);

    private final GolemUpgrade upgrade;

    private InfusionUpgradeRecipe(String research, GolemUpgrade upgrade, int inst, AspectList aspects2, ItemStack input, ItemStack[] recipe) {
        super(research, DEFAULT_OUTPUT, inst, aspects2, input, recipe);

        this.upgrade = upgrade;
    }

    @Override
    public boolean matches(ArrayList<ItemStack> input, ItemStack central, World world, EntityPlayer player) {
        return super.matches(input, central, world, player) && !upgrade.hasUpgrade(central);
    }

    @Override
    public Object getRecipeOutput(ItemStack input) {
        ItemStack output = input.copy();
        upgrade.addUpgrade(output);
        return output;
    }

    public static InfusionUpgradeRecipe[] createRecipes(String research, GolemUpgrade upgrade, int inst, AspectList aspects, ItemStack[] recipe) {
        InfusionUpgradeRecipe[] recipes = new InfusionUpgradeRecipe[EnumGolemType.values().length];

        for(int i = 0; i < EnumGolemType.values().length ; i++) {
            EnumGolemType type = EnumGolemType.values()[i];
            if(type.health > 0) {
                AdditionalGolemType addType = GadomancyApi.getAdditionalGolemType(type);
                Item input = addType == null ? ConfigItems.itemGolemPlacer : addType.getPlacerItem();
                recipes[i] = new InfusionUpgradeRecipe(research, upgrade, inst, aspects, new ItemStack(input, 1, type.ordinal()), recipe);
            }
        }
        return recipes;
    }*/
}
