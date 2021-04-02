package com.delaroystudios.scanner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.delaroystudios.scanner.database.AppDatabase;
import com.delaroystudios.scanner.database.ScannerEntity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ScannerBottomSheet extends BottomSheetDialogFragment implements AdapterView.OnItemSelectedListener {
    private static int product_price;
    private static String product_title;
    private static String product_sku;
    TextView m_title, m_price;
    String quantity_value = "1";
    Spinner m_spinner;
    Button m_button;
    AppDatabase db;

    public static ScannerBottomSheet  newInstance(String title, int price, String sku) {
        final ScannerBottomSheet  fragment = new ScannerBottomSheet ();
        final Bundle args = new Bundle();
        product_title = title;
        product_price = price;
        product_sku = sku;
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.scanner_sheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        db = AppDatabase.getInstance(getContext());
        m_title = view.findViewById(R.id.m_title);
        m_price = view.findViewById(R.id.m_price);
        m_spinner = view.findViewById(R.id.m_spinner);
        m_button = view.findViewById(R.id.m_button);

        m_title.setText(product_title);
        m_price.setText("$"  + product_price);

        m_button.setOnClickListener(view1 -> {
            int price = product_price * Integer.parseInt(quantity_value);
            AppExecutors.getInstance().diskIO().execute(() -> db.scannerDao().insertProduct(new ScannerEntity(product_title, product_sku, price, Integer.parseInt(quantity_value))));
            Toast.makeText(getContext(), "Added to cart", Toast.LENGTH_SHORT).show();
            dismiss();
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.scanner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        m_spinner.setAdapter(adapter);
        m_spinner.setOnItemSelectedListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        quantity_value = adapterView.getItemAtPosition(i).toString();
        Toast.makeText(getContext(), "value is " + quantity_value, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        int value = 1;
        quantity_value = String.valueOf(value);
    }
}
