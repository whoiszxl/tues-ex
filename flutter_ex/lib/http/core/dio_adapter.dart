import 'package:dio/dio.dart';
import 'package:flutter_ex/http/core/ex_error.dart';
import 'package:flutter_ex/http/core/ex_net_adapter.dart';
import 'package:flutter_ex/http/request/base_request.dart';

///Dio请求适配器
class DioAdapter extends ExNetAdapter {

  @override
  Future<ExNetResponse<T>> send<T>(BaseRequest request) async {
    //设置配置
    var response, options = Options(headers: request.headers);
    var error;
    try {

      //对不同method做不同适配
      switch (request.requestMethod()) {
        case RequestMethod.GET:
          response = await Dio().get(request.url(), options: options);
          break;
        case RequestMethod.POST:
          response = await Dio().post(request.url(), data: request.params, options: options);
          break;
        case RequestMethod.DELETE:
          response = await Dio().delete(request.url(), data: request.params, options: options);
          break;
        case RequestMethod.PUT:
          response = await Dio().put(request.url(), data: request.params, options: options);
          break;
        default:
          response = await Dio().post(request.url(), data: request.params, options: options);
      }
    } on DioError catch (e) {
      error = e;
      response = e.response;
    }
    if (error != null) {
      ///抛出ExNetError
      throw ExNetError(response?.statusCode ?? -1, error.toString(), data: buildResponse(response, request));
    }
    return buildResponse(response, request);
  }

  ///构建ExNetResponse
  ExNetResponse buildResponse(Response response, BaseRequest request) {
    return ExNetResponse(
        data: response.data,
        request: request,
        statusCode: response.statusCode,
        statusMessage: response.statusMessage,
        extra: response);
  }
}