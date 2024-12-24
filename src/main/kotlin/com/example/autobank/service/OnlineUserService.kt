package com.example.autobank.service

import com.example.autobank.data.authentication.Auth0User
import com.example.autobank.data.authentication.AuthenticatedUserResponse
import com.example.autobank.data.user.OnlineUser
import com.example.autobank.repository.user.OnlineUserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class OnlineUserService(
) {

    @Autowired
    lateinit var authenticationService: AuthenticationService

    @Autowired
    lateinit var onlineUserRepository: OnlineUserRepository




    fun getOnlineUser(): OnlineUser? {
        val sub: String = authenticationService.getUserSub()
        return onlineUserRepository.findByOnlineId(sub)
    }

    fun checkUser(): AuthenticatedUserResponse {
        val storedUser = onlineUserRepository.findByOnlineId(authenticationService.getUserSub())
        if (storedUser == null) {
            try {
                createOnlineUser()
            } catch (e: Exception) {
                return AuthenticatedUserResponse(success = false, false, false, null)
            }
        }

        return AuthenticatedUserResponse(success = true, authenticationService.checkBankomMembership(), authenticationService.checkSuperAdmin(), expiresat = authenticationService.getExpiresAt())
    }

    fun createOnlineUser(): OnlineUser {
            val userinfo: Auth0User = authenticationService.getUserDetails()
            val onlineUser = OnlineUser(
                id = 0,
                onlineId = userinfo.sub,
                email = userinfo.email,
                fullname = userinfo.name,
            )

            return onlineUserRepository.save(onlineUser)
    }

}
