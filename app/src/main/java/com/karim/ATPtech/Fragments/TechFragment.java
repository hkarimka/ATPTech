package com.karim.ATPtech.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.karim.ATPtech.AddTechActivity;
import com.karim.ATPtech.Model.Request.AddTechRequest;
import com.karim.ATPtech.Model.Request.DeleteTechRequest;
import com.karim.ATPtech.Model.Request.TechRequest;
import com.karim.ATPtech.Model.Response.AddTechResponse;
import com.karim.ATPtech.Model.Response.DeleteTechResponse;
import com.karim.ATPtech.Model.Response.TechResponse;
import com.karim.ATPtech.R;
import com.karim.ATPtech.RetrofitAPI;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

public class TechFragment extends Fragment {
    SharedPreferences preferences;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    String phone;
    AlertDialog diag;
    ArrayList<HashMap<String, String>> arrayList;
    ArrayList<Integer> techs;
    HashMap<String, String> map;
    ListView listView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tech, container, false);
        listView = (ListView) view.findViewById(R.id.lvTech);
        Button button = view.findViewById(R.id.bAdd);

        preferences = getActivity().getSharedPreferences("ATPTech", MODE_PRIVATE);
        if(preferences.contains("login")) {
            phone = preferences.getString("login", "");
        }

        getTech(phone);
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Отменяем анимацию обновления
                        mSwipeRefreshLayout.setRefreshing(false);
                        getTech(phone);
                    }
                }, 2000);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(arrayList.get(position).get("TypeAndMod"));
                builder.setMessage("Удалить технику?");
                builder.setPositiveButton("Удалить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Integer tech = techs.get(position);
                        deleteTech(phone, tech);
                    }
                });
                builder.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                diag = builder.create();
                diag.show();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddTechActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        getTech(phone);
    }

    void getTech(String phone) {
        techs = new ArrayList<>();
        arrayList = new ArrayList<>();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitAPI.BaseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final RetrofitAPI service = retrofit.create(RetrofitAPI.class);

        TechRequest rr = new TechRequest(phone);
        Call<TechResponse> call = null;
        call = service.gettech(rr);
        call.enqueue(new Callback<TechResponse>() {
            @Override
            public void onResponse(Call<TechResponse> call, Response<TechResponse> response) {
                if (response.body() != null) {
                    int success = response.body().getSuccess();
                    if (success == 1) {
                        ArrayList<String> typeAndMod = response.body().getTypeandmod();
                        ArrayList<String> property = response.body().getProperty();
                        techs = response.body().getTechs();
                        for(int i = 0; i < property.size(); i++) {
                            map = new HashMap<>();
                            map.put("TypeAndMod", typeAndMod.get(i));
                            map.put("Property", property.get(i));
                            arrayList.add(map);
                        }

                        SimpleAdapter adapter = new SimpleAdapter(getContext(), arrayList, R.layout.item_bids,
                                new String[]{"TypeAndMod", "Property"},
                                new int[]{R.id.item_bids_title, R.id.item_bids_subtitle});
                        listView.setAdapter(adapter);
                    }
                    else Toast.makeText(getContext(), "Ошибка", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TechResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Произошла ошибка", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void deleteTech(final String phone, Integer tech) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitAPI.BaseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final RetrofitAPI service = retrofit.create(RetrofitAPI.class);

        DeleteTechRequest rr = new DeleteTechRequest(phone, tech);
        Call<DeleteTechResponse> call = null;
        call = service.deletetech(rr);
        call.enqueue(new Callback<DeleteTechResponse>() {
            @Override
            public void onResponse(Call<DeleteTechResponse> call, Response<DeleteTechResponse> response) {
                if (response.body() != null) {
                    int success = response.body().getSuccess();
                    String message = response.body().getMessage();
                    if (success == 1) {
                        getTech(phone);
                        Toast.makeText(getContext(), "Техника удалена", Toast.LENGTH_SHORT).show();
                    }
                    else Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DeleteTechResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Произошла ошибка", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
