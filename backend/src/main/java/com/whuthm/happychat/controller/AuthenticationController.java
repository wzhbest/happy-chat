package com.whuthm.happychat.controller;

import com.whuthm.happychat.proto.api.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(consumes = {"application/x-protobuf"}, produces = {"application/x-protobuf"})
public class AuthenticationController {

    @RequestMapping(value = "/v1/auth/login", method = {RequestMethod.POST})
    Authentication.LoginResponse login(@RequestBody Authentication.LoginRequest loginRequest) {
        return Authentication.LoginResponse.newBuilder().setUserId("vs").build();
    }

}
