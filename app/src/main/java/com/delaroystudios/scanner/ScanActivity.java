package com.delaroystudios.scanner;

import android.content.Intent;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.delaroystudios.scanner.model.Product;
import com.delaroystudios.scanner.model.ProductFound;
import com.delaroystudios.scanner.networking.api.Service;
import com.delaroystudios.scanner.networking.generator.DataGenerator;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

import info.androidhive.barcode.BarcodeReader;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.delaroystudios.scanner.utils.Constant.BASE_URL;

public class ScanActivity extends AppCompatActivity implements BarcodeReader.BarcodeReaderListener {
    BarcodeReader barcodeReader;
    BottomSheetDialogFragment bottomSheetDialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_scan);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // get the barcode reader instance
        barcodeReader = (BarcodeReader) getSupportFragmentManager().findFragmentById(R.id.barcode_scanner);
    }

    @Override
    public void onScanned(Barcode barcode) {

        // playing barcode reader beep sound
        barcodeReader.playBeep();

        // ticket details activity by passing barcode
       // Intent intent = new Intent(ScanActivity.this, TicketResultActivity.class);
        //intent.putExtra("code", barcode.displayValue);
        //startActivity(intent);

        fetch(barcode.displayValue);
    }

    @Override
    public void onScannedMultiple(List<Barcode> list) {

    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {

    }

    @Override
    public void onScanError(String s) {
        Toast.makeText(getApplicationContext(), "Error occurred while scanning " + s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCameraPermissionDenied() {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            barcodeReader.pauseScanning();
        } else if (item.getItemId() == R.id.checkout) {
            Intent intent = new Intent(this, CartList.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void fetch(String sku){
        try {
            //loading.setVisibility(View.VISIBLE);
            Service service = DataGenerator.createService(Service.class, BASE_URL);
            Call<ProductFound> call = service.create(sku);

            call.enqueue(new Callback<ProductFound>() {
                @Override
                public void onResponse(Call<ProductFound> call, Response<ProductFound> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            Product product = response.body().getProduct();

                            if (product.getProductName() != null) {
                                String productName = product.getProductName();
                                int price = product.getPrice();
                                String sku = product.getSku();

                                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                                BottomSheetDialogFragment Fragment = ScannerBottomSheet.newInstance(productName, price, sku);
                                transaction.add(Fragment, "loading");
                                transaction.commitAllowingStateLoss();

                                //bottomSheetDialogFragment = ScannerBottomSheet.newInstance(productName, price, sku);
                                //bottomSheetDialogFragment.show(getSupportFragmentManager(), "Bottom Sheet Dialog Fragment");
                            }

                        }
                    } else if (response.code() == 400){
                        //loading.setVisibility(View.GONE);
                        //pin.setError(getString(R.string.wrong_pin));
                    }
                }

                @Override
                public void onFailure(Call<ProductFound> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    //loading.setVisibility(View.GONE);
                }
            });

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "error login2", Toast.LENGTH_SHORT).show();
            //loading.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.cart, menu);

        /*//you can add some logic (hide it if the count == 0)
        if (mNotificationCounter > 0) {
            BadgeCounter.update(this,
                    menu.findItem(R.id.notification),
                    R.mipmap.ic_notification,
                    BadgeCounter.BadgeColor.BLUE,
                    mNotificationCounter);
        } else {
            BadgeCounter.hide(menu.findItem(R.id.notification));
        }
*/
        return true;
    }
}
