package com.karim.ATPtech.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.karim.ATPtech.Model.Request.ChangeDataRequest;
import com.karim.ATPtech.Model.Response.ChangeDataResponse;
import com.karim.ATPtech.R;
import com.karim.ATPtech.RetrofitAPI;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

public class ChangeDataFragment extends Fragment {
    TextView textView;
    String login, name;
    SharedPreferences preferences;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_changedata, container, false);
        final String[] change = new String[]{"Имя", "Номер", "Пароль"};

        final Spinner spinner = (Spinner) view.findViewById(R.id.lvChangeData);
        final EditText editText = (EditText) view.findViewById(R.id.etChangeData);
        Button button = (Button)view.findViewById(R.id.bChangeData);
        textView = (TextView) view.findViewById(R.id.tvFIO);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, change);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        preferences = getActivity().getSharedPreferences("ATPTech", MODE_PRIVATE);
        if(preferences.contains("login") && preferences.contains("name")) {
            login = preferences.getString("login", "");
            name = preferences.getString("name", "");
        }
        textView.setText("Имя: " + name + "\n\n" + "Номер: " + login);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position)
                {
                    case 1: editText.setInputType(InputType.TYPE_CLASS_NUMBER); break;
                    case 2: editText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD); break;
                    default: editText.setInputType(InputType.TYPE_CLASS_TEXT); break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editText.getText().toString();
                if (spinner.getSelectedItemPosition() == 1)
                    if (text.length() != 10) {
                        Toast.makeText(getContext(), "Номер состоит из 10 цифр", Toast.LENGTH_SHORT).show();
                        return;
                    }
                change(spinner.getSelectedItemPosition(), text);
                editText.setText("");
            }
        });
        return view;
    }

    void change(int key, String value) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitAPI.BaseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final RetrofitAPI service = retrofit.create(RetrofitAPI.class);

        ChangeDataRequest rr = new ChangeDataRequest(key, value, login);
        Call<ChangeDataResponse> call = null;
        call = service.changedata(rr);
        call.enqueue(new Callback<ChangeDataResponse>() {
            @Override
            public void onResponse(Call<ChangeDataResponse> call, Response<ChangeDataResponse> response) {
                if (response.body() != null) {
                    int success = response.body().getSuccess();
                    if (success == 1) {
                        String name = response.body().getName();
                        String login = response.body().getPhone();
                        String password = response.body().getPassword();
                        textView.setText("Имя: " + name + "\n\n" + "Номер: " + login);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("login", login);
                        editor.putString("password", password);
                        editor.putString("name", name);
                        editor.apply();
                        Toast.makeText(getContext(), "Успешно", Toast.LENGTH_SHORT).show();
                    }
                    else
                        Toast.makeText(getContext(), "Ошибка", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ChangeDataResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Произошла ошибка", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
