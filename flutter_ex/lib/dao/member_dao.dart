import 'package:flutter_ex/http/core/ex_net.dart';
import 'package:flutter_ex/http/request/register_request.dart';

class MemberDao {


  static login() {

  }

  static register(String username, String verifyCode, String password, String rePassword) async {
    RegisterRequest request = new RegisterRequest();
    request
        .addParam("mobile", username)
        .addParam("code", verifyCode)
        .addParam("password", password)
        .addParam("rePassword", rePassword);

    var result = await ExNet.getInstance().request(request);
    print("注册结果：" + request.toString());

    if(result['code'] == 0 && result['data'] != null) {
      return true;
    }
    return false;
  }
}