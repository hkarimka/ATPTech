package com.karim.ATPtech.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.karim.ATPtech.Model.Request.RegisterRequest;
import com.karim.ATPtech.Model.Response.RegisterResponse;
import com.karim.ATPtech.R;
import com.karim.ATPtech.Register;
import com.karim.ATPtech.RetrofitAPI;
import com.karim.ATPtech.SendMessageService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SendErrorFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_senderror, container, false);
        TextInputLayout textinput = (TextInputLayout) view.findViewById(R.id.textinput);
        final TextInputEditText etSendError = (TextInputEditText) view.findViewById(R.id.etSendError);
        Button bSendError = (Button)view.findViewById(R.id.bSendError);

        bSendError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = etSendError.getText().toString();
                if(text.length() > 1000)
                    Toast.makeText(getContext(), "Не более 1000 символов", Toast.LENGTH_SHORT).show();
                else if (text.length() < 10)
                    Toast.makeText(getContext(), "Опишите проблему подробнее", Toast.LENGTH_SHORT).show();
                else {
                    SendMessageService sm = new SendMessageService(getContext(), RetrofitAPI.host, RetrofitAPI.port, RetrofitAPI.fromEmail, RetrofitAPI.password, RetrofitAPI.toEmail, new SendMessageService.AsyncResponse(){
                        @Override
                        public void processFinish(boolean isMessageSended){
                            if(isMessageSended) {
                                Toast.makeText(getContext(), "Спасибо за обратную связь!", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(getContext(), "Произошла ошибка", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    sm.setSUBJECT("Приложение АТП заявки");
                    sm.setMESSAGE(text);
                    sm.setFileNeed(false);
                    sm.execute();
                }
            }
        });
        return view;
    }
}
