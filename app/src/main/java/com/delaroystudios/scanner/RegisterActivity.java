package com.delaroystudios.scanner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.delaroystudios.scanner.model.RegisterModel;
import com.delaroystudios.scanner.model.RegisterResponse;
import com.delaroystudios.scanner.networking.api.Service;
import com.delaroystudios.scanner.networking.generator.DataGenerator;
import com.delaroystudios.scanner.utils.PreferenceUtils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.delaroystudios.scanner.utils.Constant.BASE_URL;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    TextInputLayout name, email, password, retype_password;
    TextInputEditText input_name, input_email, input_password, input_retype_password;
    MaterialButton sign_up;
    TextView sign_in;
    ProgressBar progress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        retype_password = findViewById(R.id.retype_password);
        input_name = findViewById(R.id.input_name);
        input_email = findViewById(R.id.input_email);
        input_password = findViewById(R.id.input_password);
        input_retype_password = findViewById(R.id.input_retype_password);
        sign_up = findViewById(R.id.sign_up);
        sign_in = findViewById(R.id.sign_in);
        progress = findViewById(R.id.progress);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sign_in.setOnClickListener(this);
        sign_up.setOnClickListener(this);
        setTitle("Register");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sign_up:
                verifyData();
                break;
            case R.id.sign_in:
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
        }
    }

    public void verifyData() {
        name.setError(null);
        email.setError(null);
        password.setError(null);
        retype_password.setError(null);

        if (input_name.length() == 0) {

            name.setError("Name is required");

        } else if (input_email.length() == 0) {

            email.setError("Email is required");

        } else if (input_password.length() == 0) {

            password.setError("Password is required");

        } else if (input_retype_password.length() == 0) {

            retype_password.setError("Repeat password is required");

        } else if (!(input_password.getText().toString().equals(input_retype_password.getText().toString()))) {

            retype_password.setError("Passwrd mis-match");

        }  else {
            String mname = input_name.getText().toString().trim();
            String memail = input_email.getText().toString().trim();
            String mpassword = input_password.getText().toString().trim();

            register(mname, memail, mpassword);
        }
    }

    public void register(String mname, String memail, String mpassword) {
        try {
            progress.setVisibility(View.VISIBLE);
            Service service = DataGenerator.createService(Service.class, BASE_URL);
            Call<RegisterResponse> createResponseCall = service.createRegister(new RegisterModel(mname, memail, mpassword));

            createResponseCall.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            RegisterResponse createResponse = response.body();
                            boolean error = createResponse.getError();
                            String message = createResponse.getMessage();

                            if (!error) {
                                PreferenceUtils.saveMessage(error, getApplicationContext());

                                progress.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                            } else {
                                progress.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                }

                @Override
                public void onFailure(Call<RegisterResponse> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "error registering", Toast.LENGTH_SHORT).show();
                    progress.setVisibility(View.GONE);
                }
            });

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "error registering", Toast.LENGTH_SHORT).show();
            progress.setVisibility(View.GONE);
        }
    }
}
