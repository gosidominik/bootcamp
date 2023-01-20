import enums.StorePesristenceType;

public interface StoreRegister {
    void setPersistanceType(StorePesristenceType persistentType);

    int sellProductItem(String firstProductName, int numberOfProduct);

    void createProduct(String firstProductName);

    void buyProductItem(String firstProductName, int numberOfProduct);
}
