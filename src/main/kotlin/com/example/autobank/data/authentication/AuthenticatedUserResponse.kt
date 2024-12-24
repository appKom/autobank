package com.example.autobank.data.authentication;

import java.time.Instant

class AuthenticatedUserResponse(
    val success: Boolean,
    val isadmin: Boolean,
    val issuperadmin: Boolean,
    val expiresat: Instant?
)