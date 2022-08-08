package flowapowa.application;

import flowapowa.forGettingPrices.DeprecatedProvider;
import flowapowa.library.NewProductProvider;

public class BouquetBuilder {
    private DeprecatedProvider priceProvider;
    private NewProductProvider newProductProvider;

    @Deprecated
    public BouquetBuilder(DeprecatedProvider priceProvider) {
        this.priceProvider = priceProvider;
    }

    public BouquetBuilder(NewProductProvider newProductProvider) {
        this.newProductProvider = newProductProvider;
    }

    public Bouquet withRecipe(Recipe recipe, Integer crafting) {
        Bouquet bouquet = new Bouquet(crafting);

        for (Recipe.Element element : recipe) {
            if(priceProvider != null)
            {
                bouquet.add(element, priceProvider);
            } else {
                bouquet.add(element, newProductProvider);
            }
        }

        return bouquet;
    }
}
