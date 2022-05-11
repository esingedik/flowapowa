package flowapowa.forGettingPrices;

import flowapowa.application.PriceProvider;

import java.util.HashMap;
import java.util.Map;

@Deprecated
public class DeprecatedProvider implements PriceProvider {
    private final Map<String, Double> products;

    public DeprecatedProvider() {
        products = new HashMap<>();
    }

    @Override
    public void add(String product, Double unitaryPrice) {
        products.put(product, unitaryPrice);
    }

    @Override
    public double getPrice(String product) {
        return products.get(product);
    }
}
