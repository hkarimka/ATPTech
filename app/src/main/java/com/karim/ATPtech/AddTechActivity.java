package com.karim.ATPtech;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.karim.ATPtech.Model.Request.AddTechRequest;
import com.karim.ATPtech.Model.Request.GetSubtypesRequest;
import com.karim.ATPtech.Model.Response.AddTechResponse;
import com.karim.ATPtech.Model.Response.GetSubtypesResponse;
import com.karim.ATPtech.Model.Response.GetTypes;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddTechActivity extends AppCompatActivity {
    Spinner spinner, spinner2;
    EditText etProp, etNumb;
    Button button;
    SharedPreferences preferences;
    String phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtech);

        spinner2 = findViewById(R.id.spinner2);
        spinner = findViewById(R.id.spinner);
        etProp = findViewById(R.id.etProp);
        etNumb = findViewById(R.id.etNumb);
        button = findViewById(R.id.bAdd);
        ImageView bBack = findViewById(R.id.bBack);
        bBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        gettypes();

        preferences = this.getSharedPreferences("ATPTech", MODE_PRIVATE);
        if(preferences.contains("login")) {
            phone = preferences.getString("login", "");
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type = spinner.getSelectedItem().toString();
                String modif = spinner2.getSelectedItem().toString();
                String property = etProp.getText().toString();
                String numb = etNumb.getText().toString();
                if (!modif.isEmpty() && !property.isEmpty() && !numb.isEmpty())
                    change(phone, type, modif, property, numb);
                else Toast.makeText(AddTechActivity.this, "Заполните все поля", Toast.LENGTH_SHORT).show();
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getsubtypes(spinner.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    void change(String phone, String type, String modif, String property, String numb) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitAPI.BaseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final RetrofitAPI service = retrofit.create(RetrofitAPI.class);

        AddTechRequest rr = new AddTechRequest(phone, type, modif, property, numb);
        Call<AddTechResponse> call = null;
        call = service.addtech(rr);
        call.enqueue(new Callback<AddTechResponse>() {
            @Override
            public void onResponse(Call<AddTechResponse> call, Response<AddTechResponse> response) {
                if (response.body() != null) {
                    int success = response.body().getSuccess();
                    String message = response.body().getMessage();
                    if (success == 1) {
                        Toast.makeText(AddTechActivity.this, "Успешно", Toast.LENGTH_SHORT).show();
                        Intent returnIntent = new Intent();
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    }
                    else if (success == 0) {
                        Toast.makeText(AddTechActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                    else Toast.makeText(AddTechActivity.this, "Ошибка", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AddTechResponse> call, Throwable t) {
                Toast.makeText(AddTechActivity.this, "Произошла ошибка", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void gettypes() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitAPI.BaseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final RetrofitAPI service = retrofit.create(RetrofitAPI.class);

        GetTypes rr = new GetTypes();
        Call<GetTypes> call = null;
        call = service.gettypes();
        call.enqueue(new Callback<GetTypes>() {
            @Override
            public void onResponse(Call<GetTypes> call, Response<GetTypes> response) {
                if (response.body() != null) {
                    int success = response.body().getSuccess();
                    if (success == 1) {
                        ArrayList<String> types = response.body().getTypes();
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(AddTechActivity.this, android.R.layout.simple_spinner_item, types);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(adapter);
                    }
                    else
                        Toast.makeText(AddTechActivity.this, "Ошибка", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetTypes> call, Throwable t) {
                Toast.makeText(AddTechActivity.this, "Произошла ошибка", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void getsubtypes(String type) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitAPI.BaseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final RetrofitAPI service = retrofit.create(RetrofitAPI.class);

        GetSubtypesRequest rr = new GetSubtypesRequest(type);
        Call<GetSubtypesResponse> call = null;
        call = service.getsubtypes(rr);
        call.enqueue(new Callback<GetSubtypesResponse>() {
            @Override
            public void onResponse(Call<GetSubtypesResponse> call, Response<GetSubtypesResponse> response) {
                if (response.body() != null) {
                    int success = response.body().getSuccess();
                    if (success == 1) {
                        ArrayList<String> subtypes = response.body().getSubtypes();
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(AddTechActivity.this, android.R.layout.simple_spinner_item, subtypes);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner2.setAdapter(adapter);
                    }
                    else
                        Toast.makeText(AddTechActivity.this, "Ошибка", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GetSubtypesResponse> call, Throwable t) {
                Toast.makeText(AddTechActivity.this, "Произошла ошибка", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
