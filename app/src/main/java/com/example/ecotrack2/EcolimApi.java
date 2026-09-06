package com.example.ecotrack2;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface EcolimApi {
    // Simulamos el envío al endpoint de residuos de ECOLIM
    @POST("posts")
    Call<ApiResiduo> enviarResiduo(@Body ApiResiduo residuo);
}
