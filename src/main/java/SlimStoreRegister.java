import entity.StoreItem;
import enums.StorePesristenceType;
import exception.ItemNotAvailableException;
import repository.InMemoryStoreRepository;
import repository.StoreItemRepository;

public class SlimStoreRegister implements StoreRegister {

    private StoreItemRepository storeItemRepository;
    @Override
    public void setPersistanceType(StorePesristenceType persistanceType) {
        if(persistanceType == StorePesristenceType.InMemory) {
            storeItemRepository = new InMemoryStoreRepository();
        }
        if(persistanceType == StorePesristenceType.File) {
            storeItemRepository = new InMemoryStoreRepository();
        }
    }

    @Override
    public int sellProductItem(String productName, int productQuantity) {
        StoreItem item = storeItemRepository.loadItem(productName);
        int soldQuantity;
        if (item == null) {
            throw new ItemNotAvailableException();
        }
        if (item.getQuantity() > productQuantity) {
            item.setQuantity(item.getQuantity() - productQuantity);
            soldQuantity = productQuantity;
        } else {
            soldQuantity = item.getQuantity();
            item.setQuantity(0);
        }
        storeItemRepository.saveItem(item);
        return soldQuantity;
    }

    @Override
    public void createProduct(String ProductName) {
        StoreItem newStoreItem = new StoreItem(ProductName, 0);
        storeItemRepository.saveItem(newStoreItem);
    }

    @Override
    public void buyProductItem(String productName, int productQuantity) {
        StoreItem item = storeItemRepository.loadItem(productName);
        int itemQuantity = item.getQuantity();
        item.setQuantity(itemQuantity + productQuantity);
        storeItemRepository.saveItem(item);
    }
}
