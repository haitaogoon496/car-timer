package com.mljr.heil.bean;

/**
 * @description:
 * @Date : 2018/7/5$ 15:56$
 * @Author : liht
 */
public class Code {

    public static final int SUCCESS = 0;//成功处理
    public static final int E_100 = 100;//继续,请求者应当继续提出请求
    public static final int E_204 = 204;//无数据
    public static final int E_400 = 400;//错误请求/处理失败
    public static final int E_401 = 401;//（未授权） 请求要求身份验证, 认证失败，需要登录(APP使用)
    public static final int E_403 = 403;//（禁止） 服务器拒绝请求
    public static final int E_409 = 409;//冲突
    public static final int E_500 = 500;//系统异常
    public static final int E_502 = 502;//Bad Gateway
    public static final int E_503 = 503;//Service Unavailable
    public static final int E_504 = 504;//Gateway Timeout

    //其他[1000, 1999]
    public static final int E_1101 = 1101;//风控拒绝(APP使用)
    public static final String E_1101_TIP = "您当前的信用有点问题，请保持良好信用";
    public static final int E_1503 = 1503;//风控请求失败

    public static final int E_1002 = 1002;//需要填写图片验证码验证非Robot请求(APP使用)
    public static final int E_1003 = 1003;//图片验证码不正确(APP使用)
    public static final int E_1004 = 1004;//图片验证码已过期(APP使用)

    //[2000, 2999] 用户相关
    public static final int E_2100 = 2100;//已经认证成功，请勿重复操作
    public static final int E_2101 = 2101;//认证审核中
    public static final int E_2102 = 2102;//认证被拒绝
    public static final int E_2103 = 2103;//身份证type非法
    public static final int E_2104 = 2104;//应该有的信息没有
    public static final int E_2105 = 2105;//身份证过期
    public static final int E_2106 = 2106;//该银行暂不支持
    public static final int E_2107 = 2107;//银行卡认证失败(银行卡和身份证不匹配等)(第一次绑卡)(APP使用)
    public static final int E_2108 = 2108;//户籍地址解析错误
    public static final int E_2109 = 2109;//身份证已经被占用
    public static final int E_2200 = 2200;//命中黑名单(APP使用)
    public static final int E_2201 = 2201;//银行卡认证过期
    public static final int E_2202 = 2202;//绑卡验证码发送失败
    public static final int E_2203 = 2203;//不能绑定银行卡
    public static final int E_2204 = 2204;//银行卡认证失败(银行卡和身份证不匹配等)
    public static final int E_2205 = 2205;//提交进件时 身份证有效期 跟 手机有效期  失效后  返回客户端   刷新页面
    public static final int E_2206 = 2206;//提交进件时 可选认证  失效后  返回客户端   刷新页面
    public static final int E_2209 = 2209;//绑定银行卡操作失败
    public static final int E_2208 = 2208;//不支持信用卡,请使用储蓄卡
    public static final int E_2210 = 2210;//提现的时候，需要重新绑定银行卡
    public static final int E_2403 = 2403;//密码错误

    //短信[3000, 3500]
    public static final int E_3000 = 3000;//取现需要发送短信验证码(APP使用)
    public static final int E_3001 = 3001;//验证码不正确
    public static final int E_3409 = 3409;//数据冲突，重复发送短信
    public static final int E_3403 = 3403;//短信发送发送受限
    public static final int E_3404 = 3404;//短信发送提示（剩余2次和1次给予提示）

    public static final int E_3100 = 3100;//第三笔提现提示

    //还款相关[5100, 5199]
    public static final String E_5101_TIP = "系统维护中，暂时不能还款，请半个小时后再试！";//不能发起还款，没有完成日切(协议服务)
    public static final int    E_5101     = 5101;
    public static final String E_5102_TIP = "您有正在处理中的还款支付，暂时不能还款，请稍后再试！";//不能发起还款，代扣未回盘(协议服务)或主动还款未回盘
    public static final int    E_5102     = 5102;//不能发起还款，主动还款未回盘
    public static final int    E_5105     = 5105;//不能发起还款，代扣未回盘(协议服务)
    public static final String E_5103_TIP = "您有逾期订单， 请先结清！";
    public static final int    E_5103     = 5103;//不能发起还款, 您有逾期订单， 请先结清！
    public static final int    E_5199     = 5199;//还款通用异常

    //gateway相关[5200, 5299]
    public static final int E_5203 = 5203;//三要素鉴权不通过
    public static final int E_5204 = 5204;//四要素鉴权不通过//see E_2107
    public static final int E_5209 = 5209;//三/四要素鉴权异常
    public static final int E_5211 = 5211;//发起支付--发送验证码失败
    public static final int E_5212 = 5212;//发起支付--请求失败
    public static final int E_5213 = 5213;//发起支付--结果查询请求失败
    public static final int E_5214 = 5214;//发起支付--交易验证码不正确
    public static final int E_5215 = 5215;//发起支付--银行卡号余额不足
    public static final int E_5216 = 5216;//查询支付状态 暂时没有结果
    public static final int E_5221 = 5221;//自动放款--请求失败（结算服务）
    public static final int E_5299 = 5299;//gateway接口调用失败（通用异常）

    //信审进件相关
    public static final int E_5300 = 5300;//已经提交认证，取现审核中
    public static final int E_5302 = 5302;//引导客户端重定向到认证中心进行认证(APP使用)

    //[5400, 5499] 提现相关
    public static final int E_5404 = 5404;//订单不存在
    public static final int E_5409 = 5409;//有正在提现的订单，目前不可以提现
    public static final int E_5411 = 5411;//风控提现订单审核暂时没有结果
    public static final int E_5412 = 5412;//风控提现订单拒绝(APP使用)
    public static final int E_5413 = 5413;//风控提现订单07拒绝(APP使用)
    public static final int E_5414 = 5414;//风控提现订单08拒绝(APP使用)
    public static final int E_5415 = 5415;//风控提现订单09拒绝(APP使用)
    public static final int E_5416 = 5416;//风控提现订单风控审核错误
    public static final int E_5417 = 5417;//轮询请求查询(APP使用)


    //[5500, 5599] 协议服务相关
    public static final int E_5504 = 5504;//协议服务dubbo服务异常
    public static final int E_5599 = 5599;//协议服务通用错误

    //[6500, 6599] 聚信立
    public static final int E_6500 = 6500;//调用token失败
    public static final int E_6501 = 6501;//调用collect失败
    public static final int E_6503 = 6503;//运营商下架
    public static final int E_6504 = 6504;//聚信立服务异常
    public static final int E_6505 = 6505;//聚信立异步返回code，客户端需要等待，同时轮训请求返回结果

    //[6600, 6700] 芝麻授信
    public static final int E_6601 = 6601;//芝麻授信异常
    public static final int E_6602 = 6602;//签名错误
    public static final int E_6699 = 6699;//芝麻授信失败

    //[6700, 6799] 挖财相关API
    public static final int E_6705 = 6705;//挖财HTTP请求异常
    public static final int E_6799 = 6799;//挖财通用异常


    public static final int E_9400 = 9400;//Alert提示extra:(title, content)
    public static final int E_9403 = 9403;//命中业务开关(启动拦截)

    //dubbo
    public static final int E_9500 = 9500;//dubbo-UNKNOWN_EXCEPTION
    public static final int E_9501 = 9501;//dubbo-NETWORK_EXCEPTION
    public static final int E_9502 = 9502;//dubbo-TIMEOUT_EXCEPTION
    public static final int E_9503 = 9503;//dubbo-BIZ_EXCEPTION
    public static final int E_9504 = 9504;//dubbo-FORBIDDEN_EXCEPTION
    public static final int E_9505 = 9505;//dubbo-SERIALIZATION_EXCEPTION

    //ThreadPool
    public static final int E_9603 = 9603;//ThreadPool RejectedExecutionException

    //客户端(E_2开头)
    public static final int E_23200 = 23200;//客户端版本太低（最低要求2.0.0，其中 21：Android, 22:iOS，23: 全部 ）
}
