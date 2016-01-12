package makeo.gadomancy.common.research;

import thaumcraft.api.research.ResearchPage;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is part of the Gadomancy Mod
 * Gadomancy is Open Source and distributed under the
 * GNU LESSER GENERAL PUBLIC LICENSE
 * for more read the LICENSE file
 * <p/>
 * Created by makeo @ 08.12.2015 12:28
 */
public class MultiResearchPage extends ResearchPage {
    public MultiResearchPage(List... recipes) {
        super(recipes[0]);
        recipe = new OverrideList(recipes);
    }

    private class OverrideList extends ArrayList<Object> {
        private int recipeIndex = -1;
        private List[] recipes;

        public OverrideList(List... recipes) {
            super(recipes[0]);
            this.recipes = recipes;
        }

        @Override
        public Object get(int index) {
            int calcIndex;
            if(index == 0 && (calcIndex = calcIndex()) != recipeIndex) {
                this.clear();
                for(Object o : recipes[calcIndex]) {
                    this.add(o);
                }
                recipeIndex = calcIndex;
            }
            return super.get(index);
        }

        private int calcIndex() {
            return (int)(System.currentTimeMillis() / 1000L % recipes.length);
        }
    }
}
