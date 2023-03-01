package com.longsan.common.exception;

/**
 * 认证异常
 *
 */
public class AuthException extends Exception {

    private static final long serialVersionUID = 4973608667652183916L;

    public AuthException(String message) {
        super(message);
    }

}
