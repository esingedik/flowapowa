package flowapowa.application;

import flowapowa.forGettingPrices.DeprecatedProvider;
import flowapowa.library.NewProductProvider;
import flowapowa.library.VendorProduct;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BuildBouquetShould {
    BouquetBuilder bouquetBuilder;
    @Mock
    private DeprecatedProvider priceProvider;

    @Mock
    private NewProductProvider newProductProvider;

    @Deprecated
    @Test
    void buildABouquetFromARecipe() {
        Bouquet expected = new Bouquet(0);
        when(priceProvider.getPrice("rose")).thenReturn(1.5);
        expected.add(new Recipe.Element("rose", 12), priceProvider);

        bouquetBuilder = new BouquetBuilder(priceProvider);
        BuildBouquet buildBouquet = new BuildBouquet(bouquetBuilder);

        Bouquet bouquet = buildBouquet.withRecipe("rose:12;", 0);

        assertEquals(expected.receipt(), bouquet.receipt());
    }

    @Test
    void buildABouquetFromARecipeForNewProductProvider() {
        Bouquet expected = new Bouquet(0);

        VendorProduct vendorProduct = new VendorProduct("rose", 1.5);
        when(newProductProvider.getProductByName("rose")).thenReturn(vendorProduct);

        expected.add(new Recipe.Element("rose", 12), newProductProvider);

        bouquetBuilder = new BouquetBuilder(newProductProvider);
        BuildBouquet buildBouquet = new BuildBouquet(bouquetBuilder);

        Bouquet bouquet = buildBouquet.withRecipe("rose:12;", 0);

        assertEquals(expected.receipt(), bouquet.receipt());
    }
}