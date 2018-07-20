package com.whuthm.happychat.data.api;

import com.whuthm.happychat.proto.api.Authentication;
import com.whuthm.happychat.proto.api.Base;
import com.whuthm.happychat.proto.api.User;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * 定义http请求api
 * 
 * Created by huangming on 18/07/2018.
 */

public interface ApiService {
    
    @POST("/v1/auth/login")
    Observable<Authentication.LoginResponse> login(
            @Body Authentication.LoginRequest request);
    
    @GET("/v1/users/{id}")
    Observable<User.UserResponse> getUserById(@Path("id") String userId);
    
    @GET("/v1/users")
    Observable<User.UsersResponse> getUserList(@Body Base.StringListRequest request);
    
}
