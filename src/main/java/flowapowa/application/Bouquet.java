package flowapowa.application;

import flowapowa.forGettingPrices.DeprecatedProvider;
import flowapowa.library.NewProductProvider;
import flowapowa.library.VendorProduct;

import java.util.ArrayList;
import java.util.List;

public class Bouquet {

    private final Receipt receipt = new Receipt();
    private final List<Product> products = new ArrayList<>();
    private Integer crafting;

    public Bouquet() {
    }

    public Bouquet(Integer crafting) {
        this.crafting = crafting;
    }

    @Deprecated
    public void add(Recipe.Element element, DeprecatedProvider priceProvider) {
        double price = priceProvider.getPrice(element.element());

        Product product = new Product(element.element(), element.quantity(), price);

        products.add(product);
    }

    public void add(Recipe.Element element, NewProductProvider newProductProvider) {
        double price = newProductProvider.getProductByName(element.element()).unitPrice();

        Product product = new Product(element.element(), element.quantity(), price);

        products.add(product);
    }

    @Deprecated
    public void add(String productName, Integer quantity, DeprecatedProvider priceProvider) {
        double price = priceProvider.getPrice(productName);

        Product product = new Product(productName, quantity, price);

        products.add(product);
    }

    public void add(String productName, Integer quantity, NewProductProvider newProductProvider) {
        double price = newProductProvider.getProductByName(productName).unitPrice();

        Product product = new Product(productName, quantity, price);

        products.add(product);
    }

    public String receipt() {
        for (Product product : products) {
            receipt.add(product);
        }
        receipt.addTotal("Crafting", crafting());
        receipt.addSeparator();
        receipt.addTotal("Total", total());

        return receipt.toString();
    }

    private float crafting() {
        return partial() * crafting / 100;
    }

    private float total() {
        return partial() + crafting();
    }

    private float partial() {
        float amount = 0;
        for (Product product :
                products) {
            amount += product.amount();
        }

        return amount;
    }

    static class Product {
        private final String element;
        private final int quantity;
        private final double price;

        public Product(String element, int quantity, double price) {

            this.element = element;
            this.quantity = quantity;
            this.price = price;
        }

        public float amount() {
            return (float) (quantity * price);
        }

        public void addToReceipt(Receipt receipt) {
            receipt.addPart(element, quantity, price, amount());
        }
    }
}
