package com.im.platform.common.model.vo;

import java.io.Serializable;

/**
 * 用户登录VO
 */
public class LoginVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 每次请求都必须在header中携带accessToken
     */
    private String accessToken;

    /**
     * accessToken过期时间(秒)
     */
    private Integer accessTokenExpiresIn;

    /**
     * accessToken过期后，通过refreshToken换取新的token
     */
    private String refreshToken;

    /**
     * refreshToken过期时间(秒)
     */
    private Integer refreshTokenExpiresIn;

    public LoginVO() {
    }

    public LoginVO(String accessToken, Integer accessTokenExpiresIn, String refreshToken, Integer refreshTokenExpiresIn) {
        this.accessToken = accessToken;
        this.accessTokenExpiresIn = accessTokenExpiresIn;
        this.refreshToken = refreshToken;
        this.refreshTokenExpiresIn = refreshTokenExpiresIn;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Integer getAccessTokenExpiresIn() {
        return accessTokenExpiresIn;
    }

    public void setAccessTokenExpiresIn(Integer accessTokenExpiresIn) {
        this.accessTokenExpiresIn = accessTokenExpiresIn;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Integer getRefreshTokenExpiresIn() {
        return refreshTokenExpiresIn;
    }

    public void setRefreshTokenExpiresIn(Integer refreshTokenExpiresIn) {
        this.refreshTokenExpiresIn = refreshTokenExpiresIn;
    }

    @Override
    public String toString() {
        return "LoginVO{" +
                "accessToken='" + accessToken + '\'' +
                ", accessTokenExpiresIn=" + accessTokenExpiresIn +
                ", refreshToken='" + refreshToken + '\'' +
                ", refreshTokenExpiresIn=" + refreshTokenExpiresIn +
                '}';
    }
}

