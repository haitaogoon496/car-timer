package com.mljr.heil.dto;

import java.io.Serializable;

/**
 * Author : LI-JIAN
 * Date   : 2017-06-09
 */
public interface IResponse extends Serializable {

    boolean isSuccess();

    Integer getErrorCode();

    String getErrorMsg();

}
