package com.delaroystudios.scanner;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.delaroystudios.scanner.model.LoginModel;
import com.delaroystudios.scanner.model.LoginResponse;
import com.delaroystudios.scanner.networking.api.Service;
import com.delaroystudios.scanner.networking.generator.DataGenerator;
import com.delaroystudios.scanner.utils.PreferenceUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.delaroystudios.scanner.utils.Constant.BASE_URL;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    TextInputLayout  username, password;
    TextInputEditText txt_username, txt_password;
    MaterialButton sign_in;
    TextView sign_up;
    ProgressBar progress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        txt_username = findViewById(R.id.txt_username);
        txt_password = findViewById(R.id.txt_password);

        sign_in = findViewById(R.id.sign_in);
        sign_up = findViewById(R.id.sign_up);
        progress = findViewById(R.id.progress);

        sign_in.setOnClickListener(this);
        sign_up.setOnClickListener(this);
    }

    public void verifyData() {
        password.setError(null);
        username.setError(null);

        if (txt_password.length() == 0) {

            password.setError(getString(R.string.error_password));

        } else if (txt_username.length() == 0) {

            username.setError(getString(R.string.error_username));

        } else {
            String mpassword = txt_password.getText().toString().trim();
            String musername = txt_username.getText().toString().trim();

            login(mpassword, musername);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sign_in:
                verifyData();
                break;
            case R.id.sign_up:
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }

    public void login(String password, String username){
        try {
            progress.setVisibility(View.VISIBLE);
            Service service = DataGenerator.createService(Service.class, BASE_URL);
            Call<LoginResponse> call = service.createLogin(new LoginModel(username, password));

            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            LoginResponse loginResponse = response.body();
                            if (loginResponse.getMessage() != null) {
                                String message = loginResponse.getMessage();
                                boolean  error = loginResponse.getError();

                                //Save token to shared preference for persistence
                                if (error == false) {
                                    PreferenceUtils.saveMessage(error, getApplicationContext());
                                    PreferenceUtils.saveUsername(username, getApplicationContext());

                                    progress.setVisibility(View.GONE);
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    progress.setVisibility(View.GONE);
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                }

                            }

                        }
                    } else if (response.code() == 400){
                        progress.setVisibility(View.GONE);
                        //pin.setError(getString(R.string.wrong_pin));
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    progress.setVisibility(View.GONE);
                }
            });

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "error login2", Toast.LENGTH_SHORT).show();
            progress.setVisibility(View.GONE);
        }
    }

}