package com.mljr.heil.enums;

/**
 * @description:
 * @Date : 2018/7/5$ 17:31$
 * @Author : liht
 */
public enum  JobStatus {

    NONE(0),
    START(1),
    STOP(2);

    public int CODE;

    JobStatus(int CODE) {
        this.CODE = CODE;
    }
}
