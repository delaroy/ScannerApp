package com.delaroystudios.scanner.adapter;

import android.app.ListActivity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.delaroystudios.scanner.R;

import java.util.ArrayList;
import java.util.List;

public class ListDialog extends ListActivity {

    public String[] names;
    private TypedArray imgs;
    private List<DialogHelper> list;
    public static String RESULT_LIST = "listcode";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        populateList();

        ArrayAdapter<DialogHelper> adapter = new ListDialogAdapter(this, list);
        setListAdapter(adapter);

        getListView().setOnItemClickListener((parent, view, position, id) -> {
            DialogHelper c = list.get(position);
            Intent returnIntent = new Intent();
            returnIntent.putExtra(RESULT_LIST, c.getName());
            setResult(RESULT_OK, returnIntent);
            imgs.recycle(); //recycle images
            finish();
        });
    }

    private void populateList() {
        list = new ArrayList<>();
        names = getResources().getStringArray(R.array.names);
        imgs = getResources().obtainTypedArray(R.array.flags);
        for(int i = 0; i < names.length; i++){
            list.add(new DialogHelper(names[i], imgs.getDrawable(i)));
        }
    }

    public class Payment {
        private String name;
        private String code;
        private Drawable flag;
        public Payment(String name, String code, Drawable flag){
            this.name = name;
            this.code = code;
            this.flag = flag;
        }
        public String getName() {
            return name;
        }
        public Drawable getFlag() {
            return flag;
        }
        public String getCode() {
            return code;
        }
    }
}
