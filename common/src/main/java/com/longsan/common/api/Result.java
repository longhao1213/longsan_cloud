package com.longsan.common.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.longsan.common.constant.CommonConstant;
import com.longsan.common.exception.ApiException;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;

/**
 * 统一响应消息报文
 *
 * @param <T> T对象
 */
@Data
@Getter
public class Result<T> implements Serializable,IResultCode {

    private static final long serialVersionUID = 1L;

    private int code;

    private String msg;

    private long time;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    private Result() {
        this.time = System.currentTimeMillis();
    }

    private Result(IResultCode resultCode) {
        this(resultCode, null, resultCode.getMsg());
    }

    private Result(IResultCode resultCode, String msg) {
        this(resultCode, null, msg);
    }

    private Result(IResultCode resultCode, T data) {
        this(resultCode, data, resultCode.getMsg());
    }

    private Result(IResultCode resultCode, T data, String msg) {
        this(resultCode.getCode(), data, msg);
    }

    private Result(int code, T data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
        this.time = System.currentTimeMillis();
    }

    /**
     * 返回状态码
     *
     * @param resultCode 状态码
     * @param <T> 泛型标识
     * @return ApiResult
     */
    public static <T> Result<T> success(IResultCode resultCode) {
        return new Result<>(resultCode);
    }

    public static <T> Result<T> success(String msg) {
        return new Result<>(ResultCode.SUCCESS, msg);
    }

    public static <T> Result<T> success() {
        return new Result<>(ResultCode.SUCCESS, "操作成功");
    }

    public static <T> Result<T> addSuccess() {
        return new Result<>(ResultCode.SUCCESS, "新增成功");
    }

    public static <T> Result<T> putSuccess() {
        return new Result<>(ResultCode.SUCCESS, "修改成功");
    }

    public static <T> Result<T> delSuccess() {
        return new Result<>(ResultCode.SUCCESS, "删除成功");
    }

    public static <T> Result<T> success(IResultCode resultCode, String msg) {
        return new Result<>(resultCode, msg);
    }

    public static <T> Result<T> data(T data) {
        return data(data, CommonConstant.DEFAULT_SUCCESS_MESSAGE);
    }

    public static <T> Result<T> data(T data, String msg) {
        return data(ResultCode.SUCCESS.code, data, msg);
    }

    public static <T> Result<T> data(int code, T data, String msg) {
        return new Result<>(code, data, msg);
    }

    public static <T> Result<T> fail() {
        return new Result<>(ResultCode.FAILURE, ResultCode.FAILURE.getMsg());
    }

    public static <T> Result<T> fail(String msg) {
        return new Result<>(ResultCode.FAILURE, msg);
    }

    public static <T> Result<T> fail(int code, String msg) {
        return new Result<>(code, null, msg);
    }

    public static <T> Result<T> fail(IResultCode resultCode) {
        return new Result<>(resultCode);
    }

    public static <T> Result<T> fail(IResultCode resultCode, String msg) {
        return new Result<>(resultCode, msg);
    }

    public static <T> Result<T> fail(IResultCode resultCode, String msg, T data) {
        return new Result<>(resultCode, data, msg);
    }

    public static <T> Result<T> condition(boolean flag) {
        return flag ? success(CommonConstant.DEFAULT_SUCCESS_MESSAGE) : fail(CommonConstant.DEFAULT_FAIL_MESSAGE);
    }

    /**
     * 获取请求是否成功
     *
     * @return
     */
    public boolean isSuccess() {
        return ResultCode.SUCCESS.getCode() == this.code;
    }

    /**
     * 校验网络请求是否成功
     *
     * @return
     */
    public T checkSuccess() {
        if (ResultCode.SUCCESS.getCode() != this.code) {
            throw new ApiException(ResultCode.getResultCode(code), this.msg, this.data);
        }
        return this.getData();
    }
}
