package com.portfolio.api.auth.dto;

/** JWT itself is never in the body — it only ever travels as an httpOnly cookie. */
public record LoginResponse(String username, long expiresIn) {
}
