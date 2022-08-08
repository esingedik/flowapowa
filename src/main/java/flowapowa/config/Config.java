package flowapowa.config;

public class Config {
    private String isNewProductProviderEnabled;

    public Boolean getIsNewProductProviderEnabled() {
        return Boolean.parseBoolean(isNewProductProviderEnabled);
    }

    public void setIsNewProductProviderEnabled(Boolean isNewProductProviderEnabled) {
        this.isNewProductProviderEnabled = Boolean.toString(isNewProductProviderEnabled);
    }
}