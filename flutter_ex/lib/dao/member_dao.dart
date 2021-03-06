import 'dart:convert';

import 'package:flutter_ex/db/ex_cache.dart';
import 'package:flutter_ex/http/core/ex_net.dart';
import 'package:flutter_ex/http/request/member/login_request.dart';
import 'package:flutter_ex/http/request/member/member_info_request.dart';
import 'package:flutter_ex/http/request/member/register_request.dart';
import 'package:flutter_ex/http/request/member/register_sms_request.dart';
import 'package:flutter_ex/util/string_util.dart';

///用户DAO层
class MemberDao {

  static const LOCAL_TOKEN = "LOCAL_TOKEN";

  ///登录
  static login(String username, String password) async {
    LoginRequest loginRequest = new LoginRequest();
    loginRequest
      .addParam("username", username)
      .addParam("password", password);

    var result = await ExNet.getInstance().request(loginRequest);
    print("登录结果：" + result.toString());

    if(result['code'] == 0 && result['data'] != null) {
      //登录成功，保存jwt token，country code and username
      ExCache.getInstance().setString(LOCAL_TOKEN, json.encoder.convert(result['data']));
    }
    return result;
  }

  ///注册
  static register(String username, String verifyCode, String password, String rePassword) async {
    RegisterRequest registerRequest = new RegisterRequest();
    registerRequest
      .addParam("mobile", username)
      .addParam("code", verifyCode)
      .addParam("password", password)
      .addParam("rePassword", rePassword);

    var result = await ExNet.getInstance().request(registerRequest);
    print("注册结果：" + result.toString());

    return result;
  }

  ///通过本地token获取用户信息
  static userInfo() async {
    //从本地获取用户token
    String localToken = ExCache.getInstance().get(LOCAL_TOKEN);
    if(isEmpty(localToken)) {
      return null;
    }
    var tokenObj = json.decoder.convert(localToken);
    String token = tokenObj['token'];

    //构建请求
    MemberInfoRequest memberInfoRequest = new MemberInfoRequest();
    memberInfoRequest.addHeaderToken(token);

    var result = await ExNet.getInstance().request(memberInfoRequest);
    print("获取用户信息结果：" + result.toString());

    return result;
  }


  ///发送注册短信验证码
  static sendSmsVerifyCode(String mobile) async {
    RegisterSmsRequest registerSmsRequest = new RegisterSmsRequest();
    registerSmsRequest.addParam("mobile", mobile);

    var result = await ExNet.getInstance().request(registerSmsRequest);
    print("发送注册短信结果：" + result.toString());

    return result;
  }

  ///获取本地token
  static getLocalToken() {
    return ExCache.getInstance().get(LOCAL_TOKEN);
  }
}