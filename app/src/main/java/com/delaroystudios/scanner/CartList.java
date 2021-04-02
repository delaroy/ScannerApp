package com.delaroystudios.scanner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.delaroystudios.scanner.adapter.CartAdapter;
import com.delaroystudios.scanner.database.AppDatabase;
import com.delaroystudios.scanner.database.ScannerEntity;
import com.delaroystudios.scanner.utils.PreferenceUtils;
import com.delaroystudios.scanner.viewmodel.CartViewModel;

import java.util.ArrayList;
import java.util.List;

public class CartList extends AppCompatActivity implements CartAdapter.ItemClickListener {
    RecyclerView mRecyclerView;
    AppDatabase db;
    private CartAdapter adapter;
    private TextView mtotalPrice;
    Button button_payment;
    List<ScannerEntity> dscannerEntities = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_layout);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView = findViewById(R.id.cart_recycler);
        mtotalPrice = findViewById(R.id.totalPrice);
        button_payment = findViewById(R.id.checkout);
        db = AppDatabase.getInstance(this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CartAdapter(this, this);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL));

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            // Called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                //List<ScannerEntity> scannerList = adapter.getScannerEntities();
                String sku = dscannerEntities.get(viewHolder.getAdapterPosition()).getSku();

                AppExecutors.getInstance().diskIO().execute(() -> db.scannerDao().deletProduct(sku));
                Toast.makeText(getApplicationContext(), "Item deleted from cart", Toast.LENGTH_SHORT).show();

                // remove the item from recycler view
                adapter.removeItem(viewHolder.getAdapterPosition());
                setupViewModel();


            }
        }).attachToRecyclerView(mRecyclerView);
        setupViewModel();

        button_payment.setOnClickListener(view -> {
            boolean value = PreferenceUtils.getMessage(getApplicationContext());
            if (value) {
                Intent intent = new Intent(CartList.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    //View model needed to display farmers records through livedata
    private void setupViewModel() {
        CartViewModel viewModel = new ViewModelProvider(this).get(CartViewModel.class);
        viewModel.getEntries().observe(this, (List<ScannerEntity> scannerEntities) -> {
            if (scannerEntities != null) {
                adapter.setScannerEntities(scannerEntities);
                dscannerEntities.addAll(scannerEntities);

                getTotal(scannerEntities);
            }
        });
    }

    private void getTotal(List<ScannerEntity> scannerEntities) {
       // List<ScannerEntity> scannerList = adapter.getScannerEntities();
        //Toast.makeText(this, scannerEntities.size() + " ", Toast.LENGTH_SHORT).show();
        int totalPrice = 0;

        if (scannerEntities != null) {
            for (ScannerEntity scannerEntity : scannerEntities) {
                int price = scannerEntity.getPrice();
                totalPrice += price;
            }
        }

        if (totalPrice <= 0) {
            //button_payment.setVisibility(View.GONE);
        }
        mtotalPrice.setText("$" + String.valueOf(totalPrice));
    }

    @Override
    public void onItemClickListener(String sku) {

    }
}
