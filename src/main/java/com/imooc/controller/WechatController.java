package com.imooc.controller;

import com.imooc.enums.ResultEnum;
import com.imooc.exception.ProductException;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Controller
@RequestMapping("/wechat")
@Slf4j
public class WechatController {

    @Autowired
    private WxMpService wxMpService;

    @Autowired
    private WxMpService wxOpenService;

    @GetMapping("/authorize")
    public String authorize(@RequestParam("returnUrl") String returnUrl) {
        //1. 配置 2. 调用方法
        String url = "http://kkllsc.natapp1.cc/product2/wechat/userInfo";
        try {
            URLEncoder.encode(returnUrl, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error("[微信网页授权] {}", e);
            throw new ProductException(ResultEnum.WECHAT_MP_ERROR.getCode(), ResultEnum.WECHAT_MP_ERROR.getMessage());
        }
        String redirectUrl = wxMpService.getOAuth2Service().buildAuthorizationUrl(url, WxConsts.OAuth2Scope.SNSAPI_BASE, returnUrl);
        return "redirect:" + redirectUrl;
    }

    @GetMapping("/userInfo")
    public String userInfo(@RequestParam("code") String code,
                           @RequestParam("state") String returnUrl) {
        WxOAuth2AccessToken wxMpOAuth2AccessToken = new WxOAuth2AccessToken();
        try {
            wxMpOAuth2AccessToken = wxMpService.getOAuth2Service().getAccessToken(code);
        } catch (WxErrorException e) {
            log.error("[微信网页授权] {}", e);
            throw new ProductException(ResultEnum.WECHAT_MP_ERROR.getCode(), e.getError().getErrorMsg());
        }
        String openId = wxMpOAuth2AccessToken.getOpenId();
        return "redirect:" + returnUrl + "?openid=" + openId;
    }

    @GetMapping("/QRAuthorize")
    public String QRAuthorize(@RequestParam("returnUrl") String returnUrl) {
        String url = "http://kkllsc.natapp1.cc/product2/wechat/QRUserInfo";
        try {
            URLEncoder.encode(returnUrl, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error("[微信网页授权] {}", e);
            throw new ProductException(ResultEnum.WECHAT_MP_ERROR.getCode(), ResultEnum.WECHAT_MP_ERROR.getMessage());
        }
        String redirectUrl = wxOpenService.buildQrConnectUrl(url, WxConsts.QrConnectScope.SNSAPI_LOGIN, returnUrl);
        return "redirect" + redirectUrl;
    }

    @GetMapping("/QRUserInfo")
    public String QRUserInfo(@RequestParam("code") String code,
                             @RequestParam("state") String returnUrl) {
        WxOAuth2AccessToken wxMpOAuth2AccessToken = new WxOAuth2AccessToken();
        try {
            wxMpOAuth2AccessToken = wxOpenService.getOAuth2Service().getAccessToken(code);
        } catch (WxErrorException e) {
            log.error("[微信网页授权] {}", e);
            throw new ProductException(ResultEnum.WECHAT_MP_ERROR.getCode(), e.getError().getErrorMsg());
        }
        log.info("wxOAuth2AccessToken={}", wxMpOAuth2AccessToken);
        String openId = wxMpOAuth2AccessToken.getOpenId();
            return "redirect:" + returnUrl + "?openid=" +openId;
    }
}

