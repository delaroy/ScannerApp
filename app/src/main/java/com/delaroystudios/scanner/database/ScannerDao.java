package com.delaroystudios.scanner.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ScannerDao {

    @Query("SELECT * FROM scannerentity")
    LiveData<List<ScannerEntity>> loadAllProducts();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertProduct(ScannerEntity scannerEntity);

    @Query("SELECT * FROM scannerentity WHERE sku = :sku")
    LiveData<ScannerEntity> loadProductById(String sku);

    @Query("DELETE FROM scannerentity")
    void deleteAllProducts();

    @Query("DELETE FROM scannerentity WHERE sku = :sku")
    void deletProduct(String sku);
}
