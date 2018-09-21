package com.mljr.heil.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @description:
 * @Date : 2018/7/12$ 21:53$
 * @Author : liht
 */
@Data
public class User implements Serializable{
    private Long id;
    private String userName;
    private String password;
    private String desp;
}
