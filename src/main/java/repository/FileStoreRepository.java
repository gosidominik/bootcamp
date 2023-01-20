package repository;

import entity.StoreItem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class FileStoreRepository implements StoreItemRepository{

    private File storeFile;
    private static final String fileName = "store.txt";

    FileStoreRepository() {
        storeFile = new File(fileName);
    }
    @Override
    public StoreItem loadItem(String productName) {
        return null;
    }

    @Override
    public void saveItem(StoreItem item) {
        writeToFile(item);
    }

    private void writeToFile(StoreItem item) {
        try {
            FileWriter writer = new FileWriter(storeFile);
            writer.write(item.getProductName() + " " + item.getQuantity());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String readFile() {
        try {
            Scanner reader = new Scanner(storeFile);
            while (reader.hasNextLine()){
                String data = reader.nextLine();
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
