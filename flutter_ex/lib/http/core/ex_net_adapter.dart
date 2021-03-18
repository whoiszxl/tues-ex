import 'dart:convert';

import 'package:flutter_ex/http/request/base_request.dart';

abstract class ExNetAdapter {
  Future<ExNetResponse<T>> send<T>(BaseRequest request);
}


class ExNetResponse<T> {
  ExNetResponse({
    this.data,
    this.request,
    this.statusCode,
    this.statusMessage,
    this.extra,
  });


  /// 返回的消息体
  T data;

  /// 请求信息
  BaseRequest request;

  /// 状态码
  int statusCode;

  /// 状态信息
  String statusMessage;

  ///扩展信息
  dynamic extra;

  @override
  String toString() {
    if (data is Map) {
      return json.encode(data);
    }
    return data.toString();
  }

}