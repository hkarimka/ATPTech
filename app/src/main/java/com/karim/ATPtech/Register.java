package com.karim.ATPtech;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.karim.ATPtech.Model.Request.LoginRequest;
import com.karim.ATPtech.Model.Request.RegisterRequest;
import com.karim.ATPtech.Model.Response.LoginResponse;
import com.karim.ATPtech.Model.Response.RegisterResponse;

import org.w3c.dom.Text;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Register extends AppCompatActivity {
    EditText etLogin, etPassword, etName;
    Button bReg;
    private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        etLogin = (EditText) findViewById(R.id.etLogin);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etName = (EditText) findViewById(R.id.etName);
        bReg = (Button) findViewById(R.id.bReg);

        bReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String login, password, name;
                login = etLogin.getText().toString();
                password = etPassword.getText().toString();
                name = etName.getText().toString();

                if(!login.isEmpty() && !password.isEmpty() && !name.isEmpty())
                {
                    if (login.length() == 10)
                        regist(login, password, name);
                    else
                        Toast.makeText(Register.this, "Введите 10 цифр в поле ввода номера", Toast.LENGTH_SHORT).show();
                }
                else Toast.makeText(Register.this, "Заполните все поля", Toast.LENGTH_SHORT).show();
            }
        });

    }

    void regist(String login, String password, String name) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitAPI.BaseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final RetrofitAPI service = retrofit.create(RetrofitAPI.class);

        dialog = new ProgressDialog(Register.this);
        dialog.setMessage("Загрузка...");
        dialog.show();

        RegisterRequest rr = new RegisterRequest(login, password, name);

        Call<RegisterResponse> call = null;
        call = service.register(rr);
        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.body() != null) {
                    int success = response.body().getSuccess();
                    String message = response.body().getMessage();
                    if (success == 1) {
                        Toast.makeText(Register.this, "В ближайшее время с вами свяжется оператор.", Toast.LENGTH_LONG).show();
                        finish();
                    } else if (success == 0)
                        Toast.makeText(Register.this, message, Toast.LENGTH_SHORT).show();
                    else Toast.makeText(Register.this, "Ошибка", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(Register.this, "Произошла ошибка", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
