package com.delaroystudios.scanner;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.delaroystudios.scanner.model.LoginModel;
import com.delaroystudios.scanner.model.LoginResponse;
import com.delaroystudios.scanner.networking.api.Service;
import com.delaroystudios.scanner.networking.generator.DataGenerator;
import com.delaroystudios.scanner.utils.PreferenceUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.delaroystudios.scanner.utils.Constant.BASE_URL;

public class ControllerLogin extends DialogFragment {

    EditText username;
    EditText password;
    String m_username;
    String m_password;

    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.controller_login, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        final EditText username = view.findViewById(R.id.username);
        final EditText password = view.findViewById(R.id.password);

        if (getArguments() != null && !TextUtils.isEmpty(getArguments().getString("email")))
            username.setText(getArguments().getString("email"));
        if (getArguments() != null && !TextUtils.isEmpty(getArguments().getString("password")))
            password.setText(getArguments().getString("password"));

        Button btnDone = view.findViewById(R.id.btnDone);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogListener dialogListener = (DialogListener) getActivity();
                dialogListener.onFinishEditDialog(username.getText().toString(), password.getText().toString());
                dismiss();
            }
        });
    }

    public interface DialogListener {
        void onFinishEditDialog(String email, String password);
    }


}
