package com.heil.service;

import com.alibaba.fastjson.JSON;
import com.heil.AbstractTest;
import com.mljr.heil.entity.SysConf;
import com.mljr.heil.service.SysConfService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @description:
 * @Date : 2018/7/12$ 18:03$
 * @Author : liht
 */
@Slf4j
public class SysConfServiceTest extends AbstractTest {

    @Autowired
    private SysConfService sysConfService;

    @Test
    public void testSelect() {

        SysConf sysConf = sysConfService.getByName("liht");
        log.info("sysConf ->:{}", JSON.toJSON(sysConf));
    }

    @Test
    @Rollback(value = false)
    public void testInsert() {

        SysConf sysConf = new SysConf();
        sysConf.setName("liht");
        sysConf.setValue("帅哥");
        sysConf.setCreatetime(new Date());
        sysConf.setDesp("只是测试");

        int count = sysConfService.insertNewValue("liht", "帅哥222");
        log.info("count=============={}", count);
    }

}
