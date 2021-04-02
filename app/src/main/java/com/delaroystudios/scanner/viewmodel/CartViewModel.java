package com.delaroystudios.scanner.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.delaroystudios.scanner.database.AppDatabase;
import com.delaroystudios.scanner.database.ScannerEntity;

import java.util.List;

public class CartViewModel extends AndroidViewModel {

    // Constant for logging
    private static final String TAG = CartViewModel.class.getSimpleName();

    private LiveData<List<ScannerEntity>> entries;

    public CartViewModel(Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        entries = database.scannerDao().loadAllProducts();
    }

    public LiveData<List<ScannerEntity>> getEntries() {
        return entries;
    }
}
