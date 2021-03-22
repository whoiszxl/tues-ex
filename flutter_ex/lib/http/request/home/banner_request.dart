
import 'package:flutter_ex/http/request/base_request.dart';

class BannerRequest extends BaseRequest {
  @override
  bool needLogin() {
    return false;
  }

  @override
  String path() {
    return "/home/banner";
  }

  @override
  RequestMethod requestMethod() {
    return RequestMethod.GET;
  }


}