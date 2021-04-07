package com.delaroystudios.scanner.networking.api;

import com.delaroystudios.scanner.model.LoginModel;
import com.delaroystudios.scanner.model.LoginResponse;
import com.delaroystudios.scanner.model.PaidModel;
import com.delaroystudios.scanner.model.Product;
import com.delaroystudios.scanner.model.ProductFound;
import com.delaroystudios.scanner.model.RegisterModel;
import com.delaroystudios.scanner.model.RegisterResponse;
import com.delaroystudios.scanner.networking.Routes;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface Service {
    @POST(Routes.PRODUCT + "{sku}")
    Call<ProductFound> create(@Path("sku") String sku);

    @POST(Routes.REGISTER)
    Call<RegisterResponse> createRegister(@Body RegisterModel registerModel);

    @POST(Routes.LOGIN)
    Call<LoginResponse> createLogin(@Body LoginModel loginModel);

    @POST(Routes.PAID)
    Call<RegisterResponse> insertPayment(@Body PaidModel paidModel);
}
