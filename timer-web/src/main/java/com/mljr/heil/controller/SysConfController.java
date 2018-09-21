package com.mljr.heil.controller;

import com.mljr.heil.config.DynamicDataSource;
import com.mljr.heil.dto.ResponseDTO;
import com.mljr.heil.entity.SysConf;
import com.mljr.heil.service.SysConfService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @description:
 * @Date : 2018/7/12$ 21:15$
 * @Author : liht
 */
@RequestMapping("/sysConf")
@RestController
public class SysConfController {

    @Resource
    private SysConfService sysConfService;

    @Resource
    private DynamicDataSource dynamicDataSource;

    @RequestMapping(value = "/getSysConfByName/{name}",method = RequestMethod.GET)
    public SysConf getSysConf(@PathVariable String name) {
        SysConf sysConf = sysConfService.getByName(name);
        sysConf.setDesp(DynamicDataSource.getType().toString());
        return sysConf;
    }

    @RequestMapping(value = "insertSysConf",method = RequestMethod.PUT)
    public ResponseDTO<Object> insert(@RequestBody SysConf sysConf) {
        sysConfService.insertNewValue(sysConf.getName(), sysConf.getValue());
        return new ResponseDTO(DynamicDataSource.getType());
    }
}
