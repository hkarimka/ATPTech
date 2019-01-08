package com.karim.ATPtech;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.karim.ATPtech.Model.Request.LoginRequest;
import com.karim.ATPtech.Model.Response.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Login extends AppCompatActivity {

    Button bLogin, bRegister;
    EditText etLogin, etPassword;
    ProgressDialog dialog;
    SharedPreferences preferences;
    String login, password;
    MyFirebaseMessagingService ms;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        preferences = getSharedPreferences("ATPTech", MODE_PRIVATE);

        if(preferences.contains("login") && preferences.contains("password")) {
            login = preferences.getString("login", "");
            password = preferences.getString("password", "");
            auth(login, password);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        bLogin = (Button)findViewById(R.id.bLogin);
        bRegister = (Button)findViewById(R.id.bRegister);
        etLogin = (EditText)findViewById(R.id.etLogin);
        etPassword  =(EditText)findViewById(R.id.etPassword);

        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etLogin.getText().toString().isEmpty() && !etPassword.getText().toString().isEmpty())
                    auth(etLogin.getText().toString(), etPassword.getText().toString());
                else Toast.makeText(Login.this, "Заполните все поля", Toast.LENGTH_SHORT).show();
            }
        });

        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Register.class));
            }
        });

    }

    @Override
    public void onBackPressed()
    {
        finish();
    }

    void auth(final String login, final String password) {
        final String token;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitAPI.BaseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        if(preferences.contains("token")) {
            token = preferences.getString("token", "");
        }
        else {
            ms = new MyFirebaseMessagingService();
            ms.onFirstInstall(Login.this);
            token = ms.getToken();
        }
        final RetrofitAPI service = retrofit.create(RetrofitAPI.class);

        dialog = new ProgressDialog(Login.this);
        dialog.setMessage("Загрузка...");
        dialog.show();

        int version_app = BuildConfig.VERSION_CODE;
        LoginRequest lr = new LoginRequest(login, password, token, version_app);

        Call<LoginResponse> call = null;
        call = service.auth(lr);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.body() != null) {
                    int success = response.body().getSuccess();
                    String message = response.body().getMessage();
                    String name, isActivated;
                    name = response.body().getName();
                    isActivated = response.body().getIsActivated();
                    if (success == 1) {
                        //if(Integer.parseInt(isActivated) == 1) {
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("login", login);
                            editor.putString("password", password);
                            editor.putString("name", name);
                            editor.apply();
                            Intent intent = new Intent(Login.this, MainActivity.class);
                            startActivity(intent);
                        //}
                        //else Toast.makeText(Login.this, "Ваш аккаунт еще не активирован", Toast.LENGTH_LONG).show();
                    }
                    else if (success == 0)
                        Toast.makeText(Login.this, message, Toast.LENGTH_SHORT).show();
                    else Toast.makeText(Login.this, "Ошибка", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(Login.this, "Произошла ошибка", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
