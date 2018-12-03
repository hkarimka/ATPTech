package com.karim.ATPtech.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.karim.ATPtech.MainActivity;
import com.karim.ATPtech.Model.Request.AcceptBidRequest;
import com.karim.ATPtech.Model.Request.BidsRequest;
import com.karim.ATPtech.Model.Request.TechRequest;
import com.karim.ATPtech.Model.Response.AcceptBidResponse;
import com.karim.ATPtech.Model.Response.BidsResponse;
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

public class BidsFragment extends Fragment {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    SharedPreferences preferences;
    String phone;
    AlertDialog diag;
    ArrayList<Integer> bids;
    ArrayList<HashMap<String, String>> arrayList;
    HashMap<String, String> map;
    ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bids, container, false);
        listView = (ListView) view.findViewById(R.id.lvBids);
        Button bUpdate = view.findViewById(R.id.bUpdate);
        bids = new ArrayList<>();
        preferences = getActivity().getSharedPreferences("ATPTech", MODE_PRIVATE);
        if (preferences.contains("login")) {
            phone = preferences.getString("login", "");
        }
        arrayList = new ArrayList<>();
        getBids(phone);
        bUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBids(phone);
            }
        });
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Отменяем анимацию обновления
                        mSwipeRefreshLayout.setRefreshing(false);
                        getBids(phone);
                    }
                }, 2000);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if(arrayList.get(position).get("TypeAndMod").equals("Заявок пока нет.")) {
                    Toast.makeText(getContext(), "Заявок пока нет.", Toast.LENGTH_SHORT).show();
                    return;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(arrayList.get(position).get("TypeAndMod"));
                builder.setMessage("Принять заявку?");
                builder.setPositiveButton("Принять", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        acceptBid(phone, bids.get(position), "accept");
                    }
                });
                builder.setNegativeButton("Убрать", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        acceptBid(phone, bids.get(position), "hide");
                        dialog.cancel();
                    }
                });
                builder.setNeutralButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                diag = builder.create();
                diag.show();
            }
        });
        return view;
    }

    void getBids(String phone) {
        arrayList.clear();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitAPI.BaseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final RetrofitAPI service = retrofit.create(RetrofitAPI.class);


        BidsRequest rr = new BidsRequest(phone);
        Call<BidsResponse> call = null;
        call = service.getbids(rr);
        call.enqueue(new Callback<BidsResponse>() {
            @Override
            public void onResponse(Call<BidsResponse> call, Response<BidsResponse> response) {
                if (response.body() != null) {
                    int success = response.body().getSuccess();
                    if (success == 1) {
                        ArrayList<String> typeAndMod = response.body().getTypeandmod();
                        ArrayList<String> text = response.body().getText();
                        bids = response.body().getId();
                        for (int i = 0; i < text.size(); i++) {
                            map = new HashMap<>();
                            map.put("TypeAndMod", typeAndMod.get(i));
                            map.put("Text", text.get(i));
                            arrayList.add(map);
                        }

                        SimpleAdapter adapter = new SimpleAdapter(getContext(), arrayList, android.R.layout.simple_list_item_2,
                                new String[]{"TypeAndMod", "Text"},
                                new int[]{android.R.id.text1, android.R.id.text2});
                        listView.setAdapter(adapter);
                    } else Toast.makeText(getContext(), "Ошибка", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BidsResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Произошла ошибка", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void acceptBid(final String phone, int bid, String key) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetrofitAPI.BaseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final RetrofitAPI service = retrofit.create(RetrofitAPI.class);

        AcceptBidRequest rr = new AcceptBidRequest(phone, bid, key);
        Call<AcceptBidResponse> call = null;
        call = service.acceptbid(rr);
        call.enqueue(new Callback<AcceptBidResponse>() {
            @Override
            public void onResponse(Call<AcceptBidResponse> call, Response<AcceptBidResponse> response) {
                if (response.body() != null) {
                    int success = response.body().getSuccess();
                    if (success == 1) {
                        getBids(phone);
                        Toast.makeText(getContext(), "Готово!", Toast.LENGTH_SHORT).show();
                    }
                    else Toast.makeText(getContext(), "Ошибка", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AcceptBidResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Произошла ошибка", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

