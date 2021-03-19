
import 'package:flustars/flustars.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_ex/http/core/ex_error.dart';
import 'package:flutter_ex/util/validate_util.dart';
import 'package:flutter_ex/widget/appbar.dart';
import 'package:flutter_ex/widget/login_input.dart';
import 'package:flutter_ex/widget/login_button.dart';
import 'package:flutter_ex/util/string_util.dart';
import 'package:flutter_ex/util/toast.dart';
import 'package:flutter_ex/dao/member_dao.dart';
import 'package:flutter_ex/widget/send_sms_button.dart';


///注册页面
class RegisterPage extends StatefulWidget {

  final VoidCallback onJumpToLogin;

  const RegisterPage({Key key, this.onJumpToLogin}) : super(key: key);

  @override
  _RegisterPageState createState() => _RegisterPageState();

}

class _RegisterPageState extends State<RegisterPage> {

  bool protect = false;
  bool loginEnable = false;
  String username;
  String password;
  String rePassword;
  String verifyCode;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: appBar("注册", "登录", widget.onJumpToLogin),

      body: Container(
        child: ListView(
          children: [

            //todo 头部样式暂略

            NormalInput("手机号", "请输入手机号", onChanged: (text){
              username = text;
              checkInput();
            }),

            NormalInput("验证码", "请输入验证码",
              onChanged: (text) {
                verifyCode = text;
                checkInput();
              },
              focusChanged: (focus) {
                this.setState(() {
                  protect = focus;
                });
            },),

            Padding(
              padding: EdgeInsets.only(right: 10, left: 300),
              child: SendSmsButton('点击发送', enable: true, onPressed: sendSms),
            ),

            NormalInput("密码", "请输入密码", lineStretch: true, obscureText: true,
              onChanged: (text) {
                password = text;
                checkInput();
              },
              focusChanged: (focus) {
                this.setState(() {
                  protect = focus;
                });
              },
            ),


        
            NormalInput("确认密码", "请再次输入密码", lineStretch: true, obscureText: true,
              onChanged: (text) {
                rePassword = text;
                checkInput();
              },
              focusChanged: (focus) {
                this.setState(() {
                  protect = focus;
                });
              },
            ),

            Padding(
              padding: EdgeInsets.only(top: 20, left: 20, right: 20),
              child: LoginButton(
                "注册",
                enable: loginEnable,
                onPressed: sendRegister,
              ),
            )
          ],
        ),
      ),
    );
  }


  void sendSms() async {
    try {
      if(!RegexUtil.isMobileSimple(username)) {
        showErrorToast("手机号码不正确");
        return;
      }
      var result = await MemberDao.sendSmsVerifyCode(username);
      if (verifyResult(result)) {
        showToast('发送成功' + result['data']);
        if (widget.onJumpToLogin != null) {
          widget.onJumpToLogin();
        }

      } else {
        print(result['message']);
        showErrorToast(result['message']);
      }
    } on NeedAuth catch (e) {
      print(e);
      showErrorToast(e.message);
    } on ExNetError catch (e) {
      print(e);
      showErrorToast(e.message);
    }
  }

  void sendRegister() async {
    try {
      if (password != rePassword) {
        showErrorToast('两次密码不一致');
        return;
      }

      var result = await MemberDao.register(username, verifyCode, password, rePassword);
      if (result) {
        print('注册成功');
        showToast('注册成功');
        if (widget.onJumpToLogin != null) {
          widget.onJumpToLogin();
        }
      } else {
        print(result['message']);
        showErrorToast(result['message']);
      }
    } on NeedAuth catch (e) {
      print(e);
      showErrorToast(e.message);
    } on ExNetError catch (e) {
      print(e);
      showErrorToast(e.message);
    }
  }

  void checkInput() {
    bool enable;
    if (isNotEmpty(username) &&
        isNotEmpty(password) &&
        isNotEmpty(rePassword) &&
        isNotEmpty(verifyCode)) {
      enable = true;
    } else {
      enable = false;
    }
    setState(() {
      print("enable:" + enable.toString());
      loginEnable = enable;
    });
  }

}