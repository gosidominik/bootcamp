package repository;

import entity.StoreItem;

public interface StoreItemRepository {
    StoreItem loadItem(String productName);
    void saveItem(StoreItem item);
}
