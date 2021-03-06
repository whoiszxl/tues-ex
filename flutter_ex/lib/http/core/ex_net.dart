import 'package:flutter_ex/http/core/dio_adapter.dart';
import 'package:flutter_ex/http/core/ex_error.dart';
import 'package:flutter_ex/http/core/ex_interceptor.dart';
import 'package:flutter_ex/http/core/ex_net_adapter.dart';
import 'package:flutter_ex/http/request/base_request.dart';

class ExNet {

  ExNet ._();

  static ExNet _instance;

  ExErrorInterceptor _exErrorInterceptor;

  ///单例获取实例
  static ExNet getInstance() {
    if (_instance == null) {
      _instance = ExNet._();
    }
    return _instance;
  }


  Future request(BaseRequest request) async {
    ExNetResponse response;
    var error;
    try {
      response = await send(request);
    } on ExNetError catch (e) {
      error = e;
      response = e.data;
      printLog(e.message);
    } catch (e) {
      //其它异常
      error = e;
      printLog(e);
    }
    if (response == null) {
      printLog(error);
    }
    var result = response.data;
    var status = response.statusCode;
    var exError;

    switch (status) {
      case 200:
        return result;
        break;
      case 401:
        exError = NeedLogin();
        break;
      case 403:
        exError = NeedAuth(result.toString(), data: result);
        break;
      default:
        exError = ExNetError(status, result.toString(), data: result);
        break;
    }

    if(_exErrorInterceptor != null) {
      _exErrorInterceptor(exError);
    }

    throw exError;
  }

  void setErrorInterceptor(ExErrorInterceptor interceptor) {
    _exErrorInterceptor = interceptor;
  }

  Future<ExNetResponse<T>> send<T>(BaseRequest request) async {
    ///使用Dio发送请求
    ExNetAdapter adapter = DioAdapter();
    return adapter.send(request);
  }


  void printLog(log) {
    print('ex_net:' + log.toString());
  }
}