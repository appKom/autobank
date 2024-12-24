package com.example.autobank.controller

import com.example.autobank.data.authentication.AuthenticatedUserResponse
import com.example.autobank.service.AuthenticationService
import com.example.autobank.service.OnlineUserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/auth")
class AuthenticationController {
    @Autowired
    lateinit var onlineUserService: OnlineUserService;


    @Autowired
    lateinit var authenticationService: AuthenticationService;

    @GetMapping("/check")
    fun checkUser(): ResponseEntity<AuthenticatedUserResponse> {

        return try {
            ResponseEntity.ok(onlineUserService.checkUser())
        } catch (e: Exception) {
            print(e)
            ResponseEntity.badRequest().build();
        }
    }


}