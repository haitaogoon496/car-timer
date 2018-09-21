package com.mljr.heil.dto;

import com.mljr.heil.bean.Code;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * ClassName:ResponseDTO <br/>
 * Function: 响应基类. <br/>
 * Date:     2016年3月25日 下午3:49:08 <br/>
 *
 * @author tongzheng.zhang
 * @version 1.0
 * @since JDK 1.8
 */
@SuppressWarnings("serial")
public class ResponseDTO<T> implements Serializable, IResponse {

    protected Integer errorCode = Code.SUCCESS;

    protected String errorMsg;

    protected T data;

    protected Map<String, Object> extra = new LinkedHashMap<>();

    public ResponseDTO() {
    }

    public ResponseDTO(int errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public ResponseDTO(T data) {
        this.data = data;
    }

    public ResponseDTO(int errorCode, String errorMsg, T data) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.data = data;
    }

    public ResponseDTO(int errorCode, String errorMsg, Map<String, Object> extra) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.extra = extra;
    }

    public ResponseDTO(Integer errorCode, String errorMsg, T data, Map<String, Object> extra) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
        this.data = data;
        this.extra = extra;
    }

    @Override
    public boolean isSuccess() {
        return errorCode == Code.SUCCESS;
    }

    public static <E> ResponseDTO<E> rsOK(E data) {
        return new ResponseDTO<E>(data);
    }

    /**
     * errorCode.
     *
     * @return the errorCode
     * @since JDK 1.8
     */
    public Integer getErrorCode() {
        return errorCode;
    }

    /**
     * errorMsg.
     *
     * @return the errorMsg
     * @since JDK 1.8
     */
    public String getErrorMsg() {
        return errorMsg;
    }

    /**
     * errorCode.
     *
     * @param errorCode the errorCode to set
     * @since JDK 1.8
     */
    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * errorMsg.
     *
     * @param errorMsg the errorMsg to set
     * @since JDK 1.8
     */
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    protected boolean isSuccessful() {
        return errorCode == 0;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setResponse(int code, String msg) {
        this.errorCode = code;
        this.errorMsg = msg;
    }

    public void setResponse(int code, String msg, T data) {
        this.errorCode = code;
        this.errorMsg = msg;
        this.data = data;
    }

    public ResponseDTO<T> addExtra(String key, Object extraData) {
        this.getExtra().put(key, extraData);
        return this;
    }

    public Map<String, Object> getExtra() {
        return extra;
    }
}
