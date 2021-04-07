package com.delaroystudios.scanner;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.delaroystudios.scanner.adapter.CartAdapter;
import com.delaroystudios.scanner.adapter.ListDialog;
import com.delaroystudios.scanner.database.AppDatabase;
import com.delaroystudios.scanner.database.ScannerEntity;
import com.delaroystudios.scanner.model.PaidModel;
import com.delaroystudios.scanner.model.RegisterModel;
import com.delaroystudios.scanner.model.RegisterResponse;
import com.delaroystudios.scanner.model.SpinnerModel;
import com.delaroystudios.scanner.networking.api.Service;
import com.delaroystudios.scanner.networking.generator.DataGenerator;
import com.delaroystudios.scanner.utils.PreferenceUtils;
import com.delaroystudios.scanner.viewmodel.CartViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.delaroystudios.scanner.utils.Constant.BASE_URL;

public class CartList extends AppCompatActivity implements CartAdapter.ItemClickListener{
    RecyclerView mRecyclerView;
    AppDatabase db;
    private CartAdapter adapter;
    private TextView mtotalPrice;
    Button button_payment;
    ArrayList<SpinnerModel> list=new ArrayList<>();
    List<ScannerEntity> dscannerEntities = new ArrayList<>();
    Intent intent2;
    String mTitle;
    int totalPrice = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_layout);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView = findViewById(R.id.cart_recycler);
        mtotalPrice = findViewById(R.id.totalPrice);
        button_payment = findViewById(R.id.checkout);
        db = AppDatabase.getInstance(this);

        intent2 = new Intent(this, ListDialog.class);

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
            } else {
                startActivityForResult(intent2, 1);
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
        mTitle = "";

        if (scannerEntities != null) {
            for (ScannerEntity scannerEntity : scannerEntities) {
                int price = scannerEntity.getPrice();
                String title = scannerEntity.getTitle();

                mTitle += title + ",";
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            String countryCode = data.getStringExtra(ListDialog.RESULT_LIST);
            insertPayment(mTitle, totalPrice);
            //Toast.makeText(this, "You selected this payment: " + countryCode + ", " + "with this details " +  mTitle, Toast.LENGTH_LONG).show();
        }
    }

    public void insertPayment(String items, int amounnt) {
        try {
            //progress.setVisibility(View.VISIBLE);
            Service service = DataGenerator.createService(Service.class, BASE_URL);
            Call<RegisterResponse> createResponseCall = service.insertPayment(new PaidModel(PreferenceUtils.getUsername(this), items, amounnt, genToken()));

            createResponseCall.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            RegisterResponse createResponse = response.body();
                            boolean error = createResponse.getError();
                            String message = createResponse.getMessage();
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "error inserting " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    //progress.setVisibility(View.GONE);
                }
            });

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "error inserting " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            //progress.setVisibility(View.GONE);
        }
    }

    public String genToken() {

        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        String generatedString = buffer.toString();

        return generatedString;
    }

}
