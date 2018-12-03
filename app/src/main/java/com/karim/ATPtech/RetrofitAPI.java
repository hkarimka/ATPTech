package com.karim.ATPtech;

import com.karim.ATPtech.Model.Request.AcceptBidRequest;
import com.karim.ATPtech.Model.Request.AddTechRequest;
import com.karim.ATPtech.Model.Request.BidsRequest;
import com.karim.ATPtech.Model.Request.ChangeDataRequest;
import com.karim.ATPtech.Model.Request.DeleteTechRequest;
import com.karim.ATPtech.Model.Request.GetSubtypesRequest;
import com.karim.ATPtech.Model.Request.LoginRequest;
import com.karim.ATPtech.Model.Request.MyBidsRequest;
import com.karim.ATPtech.Model.Request.RegisterRequest;
import com.karim.ATPtech.Model.Request.RemoveTokenRequest;
import com.karim.ATPtech.Model.Request.TechRequest;
import com.karim.ATPtech.Model.Request.UpdateTokenRequest;
import com.karim.ATPtech.Model.Response.AcceptBidResponse;
import com.karim.ATPtech.Model.Response.AddTechResponse;
import com.karim.ATPtech.Model.Response.BidsResponse;
import com.karim.ATPtech.Model.Response.ChangeDataResponse;
import com.karim.ATPtech.Model.Response.DeleteTechResponse;
import com.karim.ATPtech.Model.Response.GetSubtypesResponse;
import com.karim.ATPtech.Model.Response.GetTypes;
import com.karim.ATPtech.Model.Response.LoginResponse;
import com.karim.ATPtech.Model.Response.MyBidsResponse;
import com.karim.ATPtech.Model.Response.RegisterResponse;
import com.karim.ATPtech.Model.Response.RemoveTokenResponse;
import com.karim.ATPtech.Model.Response.TechResponse;
import com.karim.ATPtech.Model.Response.UpdateTokenResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RetrofitAPI {

    String BaseURL = "http://46.229.215.232/";
    String host = "smtp.yandex.ru";
    String port = "465";
    String fromEmail = "atp_app@top-it.ru";
    String toEmail = "atp_app@top-it.ru";
    String password = "top-atp";

    @POST("login.php")
    Call<LoginResponse> auth (@Body LoginRequest body);

    @POST("updatetoken.php")
    Call<UpdateTokenResponse> updatetoken (@Body UpdateTokenRequest body);

    @POST("removetoken.php")
    Call<RemoveTokenResponse> removetoken (@Body RemoveTokenRequest body);

    @POST("register.php")
    Call<RegisterResponse> register (@Body RegisterRequest body);

    @POST("changedata.php")
    Call<ChangeDataResponse> changedata (@Body ChangeDataRequest body);

    @POST("addtech.php")
    Call<AddTechResponse> addtech (@Body AddTechRequest body);

    @POST("gettech.php")
    Call<TechResponse> gettech (@Body TechRequest body);

    @POST("acceptbid.php")
    Call<AcceptBidResponse> acceptbid (@Body AcceptBidRequest body);

    @GET("gettypes.php")
    Call<GetTypes> gettypes();

    @POST("getbids.php")
    Call<BidsResponse> getbids(@Body BidsRequest body);

    @POST("getmybids.php")
    Call<MyBidsResponse> getmybids(@Body MyBidsRequest body);

    @POST("deletetech.php")
    Call<DeleteTechResponse> deletetech(@Body DeleteTechRequest body);

    @POST("getsubtypes.php")
    Call<GetSubtypesResponse> getsubtypes(@Body GetSubtypesRequest body);
}
