package flowapowa;

import flowapowa.application.BouquetBuilder;
import flowapowa.application.BuildBouquet;
import flowapowa.application.ReceiptPrinter;
import flowapowa.config.Config;
import flowapowa.config.ConfigHandler;
import flowapowa.forGettingPrices.DeprecatedProvider;
import flowapowa.forPrintingReceipts.ConsoleReceiptPrinter;
import flowapowa.forUsingApplication.FlowaPowaApp;
import flowapowa.library.NewProductProvider;
import flowapowa.library.VendorProduct;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.io.FileNotFoundException;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StepDefinitions {

    private final DeprecatedProvider provider;
    private final NewProductProvider newProductProvider;
    private final boolean isNewProductProviderEnabled;
    private Integer crafting;
    private String recipe;

    public StepDefinitions() {
        ConfigHandler handler = null;
        try {
            handler = ConfigHandler.getInstance();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Config config = handler.getConfig();

        this.isNewProductProviderEnabled = config.getIsNewProductProviderEnabled();
        this.provider = new DeprecatedProvider();
        this.newProductProvider = new NewProductProvider();
    }

    @Given("{string} costs {double}")
    public void costs(String product, Double unitaryPrice) {
        if (isNewProductProviderEnabled) {
            VendorProduct vendorProduct = new VendorProduct(product, unitaryPrice);
            newProductProvider.store(vendorProduct);
        } else {
            provider.add(product, unitaryPrice);

        }
    }

    @Given("crafting adds {int}%")
    public void crafting_adds(Integer percent) {
        crafting = percent;
    }

    @When("I request a bouquet with {int} {string}")
    public void i_request_a_bouquet_with(Integer quantity, String product) {
        recipe = new RawRecipe(quantity, product).toString();
    }

    @Then("the receipt looks like")
    public void the_receipt_looks_like(String expectedReceipt) {

        BuildBouquet buildBouquet;
        if (isNewProductProviderEnabled) {
            buildBouquet = new BuildBouquet(
                    new BouquetBuilder(newProductProvider)
            );
        } else {
            buildBouquet = new BuildBouquet(
                    new BouquetBuilder(provider)
            );
        }


        ReceiptPrinter receiptPrinter = new ConsoleReceiptPrinter();

        FlowaPowaApp.inject(buildBouquet, receiptPrinter);
        FlowaPowaApp.main(new String[]{recipe, String.valueOf(crafting)});

        assertEquals(expectedReceipt, receiptPrinter.output());
    }

    @When("I request a bouquet with {int} {string} and {int} {string}")
    public void iRequestABouquetWithAnd(int qty1, String product1, int qty2, String product2) {
        recipe = new RawRecipe(qty1, product1) + new RawRecipe(qty2, product2).toString();
    }

    public record RawRecipe(Integer quantity, String product) {

        public String toString() {
            return String.format(Locale.ROOT, "%s:%s;", product, quantity);
        }
    }
}
