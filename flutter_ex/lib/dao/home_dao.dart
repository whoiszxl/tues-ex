
import 'dart:convert';

import 'package:flustars/flustars.dart';
import 'package:flutter_ex/http/core/ex_net.dart';
import 'package:flutter_ex/http/request/home/banner_request.dart';
import 'package:flutter_ex/model/banner_model.dart';
import 'package:flutter_ex/util/log_util.dart';

///主页dao层
class HomeDao {

  ///获取banner列表
  static bannerList(String type) async {
    BannerRequest bannerRequest = new BannerRequest();
    bannerRequest.pathParams = type;
    var result = await ExNet.getInstance().request(bannerRequest);
    Log.debug("banner list" + result['data'].toString());

    var map = result['data'].map((item) => BannerModel.fromJson(item));
    return List<BannerModel>.from(map);
  }

}