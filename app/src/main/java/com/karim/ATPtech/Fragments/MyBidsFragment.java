package com.karim.ATPtech.Fragments;

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
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.karim.ATPtech.Model.Request.MyBidsRequest;
import com.karim.ATPtech.Model.Response.MyBidsResponse;
import com.karim.ATPtech.R;
import com.karim.ATPtech.RetrofitAPI;
import com.karim.ATPtech.WinnerBidActivity;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.Context.MODE_PRIVATE;

public class MyBidsFragment extends Fragment {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    SharedPreferences preferences;
    String phone;
    ArrayList<Integer> isWinner;
    ArrayList<Integer> bids;
    ArrayList<Integer> images;
    ArrayList<Integer> status;
    ArrayList<String> op_phone;
    ArrayList<String> typeAndMod;
    ArrayList<String> text;
    ArrayList<HashMap<String, Object>> arrayList;
    HashMap<String, Object> map;
    ListView listView;
    SimpleAdapter adapter;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mybids, container, false);
        listView = (ListView) view.findViewById(R.id.lvMyBids);
        bids = new ArrayList<>();
        images = new ArrayList<>();
        isWinner = new ArrayList<>();
        typeAndMod = new ArrayList<>();
        op_phone = new ArrayList<>();
        status = new ArrayList<>();
        text = new ArrayList<>();
        preferences = getActivity().getSharedPreferences("ATPTech", MODE_PRIVATE);
        if (preferences.contains("login")) {
            phone = preferences.getString("login", "");
        }
        arrayList = new ArrayList<>();
        getBids(phone);

        mSwipeRefreshLayout = view.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Отменяем анимацию обновления
                        mSwipeRefreshLayout.setRefreshing(false);
                        images.clear();
                        isWinner.clear();
                        adapter.notifyDataSetChanged();
                        getBids(phone);
                    }
                }, 2000);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if(arrayList.get(position).get("TypeAndMod").equals("Принятых заявок пока нет.")) {
                    Toast.makeText(getContext(), "Принятых заявок пока нет.", Toast.LENGTH_SHORT).show();
                    return;
                }
                Integer isWin = isWinner.get(position);
                //if (isWin == 1) {
                    Intent intent = new Intent(getContext(), WinnerBidActivity.class);
                    intent.putExtra("TypeAndMod", typeAndMod.get(position));
                    intent.putExtra("Text", text.get(position));
                    intent.putExtra("Status", status.get(position));
                    intent.putExtra("Op_phone", op_phone.get(position));
                    startActivity(intent);
                //}
                //else Toast.makeText(getContext(), "Не победитель", Toast.LENGTH_SHORT).show();
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

        MyBidsRequest rr = new MyBidsRequest(phone);
        Call<MyBidsResponse> call = null;
        call = service.getmybids(rr);
        call.enqueue(new Callback<MyBidsResponse>() {
            @Override
            public void onResponse(Call<MyBidsResponse> call, Response<MyBidsResponse> response) {
                if (response.body() != null) {
                    int success = response.body().getSuccess();
                    if (success == 1) {
                        typeAndMod = response.body().getTypeandmod();
                        isWinner  = response.body().getIsWinner();
                        bids = response.body().getId();
                        text = response.body().getText();
                        status = response.body().getStatus();
                        op_phone = response.body().getOp_phone();
                        for(int i = 0; i<isWinner.size(); i++) {
                            if (isWinner.get(i) == 1)
                                images.add(R.drawable.winner);
                            else images.add(null);
                        }
                        for (int i = 0; i < typeAndMod.size(); i++) {
                            map = new HashMap<>();
                            map.put("TypeAndMod", typeAndMod.get(i));
                            map.put("Text", text.get(i));
                            map.put("image", images.get(i));
                            arrayList.add(map);
                        }

                        adapter = new SimpleAdapter(getContext(), arrayList, R.layout.item_mybids,
                                new String[]{"image", "TypeAndMod", "Text"},
                                new int[]{R.id.item_mybids_image, R.id.item_mybids_title, R.id.item_mybids_subtitle});
                        listView.setAdapter(adapter);
                    } else Toast.makeText(getContext(), "Ошибка", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MyBidsResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Произошла ошибка", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
