
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_ex/navigator/bottom_navigator.dart';
import 'package:flutter_ex/page/asset_page.dart';
import 'package:flutter_ex/page/login_page.dart';
import 'package:flutter_ex/page/market_page.dart';
import 'package:flutter_ex/page/register_page.dart';
import 'package:flutter_ex/page/trade_page.dart';

typedef RouteChangeListener(RouteStatusInfo current, RouteStatusInfo pre);


///自定义路由封装，路由状态
enum RouteStatus { login, register, home, asset, market, trade, quotes, unknown }

///获取page对应的RouteStatus
RouteStatus getStatus(MaterialPage page) {
  if (page.child is LoginPage) {
    return RouteStatus.login;
  } else if (page.child is RegisterPage) {
    return RouteStatus.register;
  } else if (page.child is BottomNavigator) {
    return RouteStatus.home;
  } else if (page.child is MarketPage) {
    return RouteStatus.market;
  }  else if (page.child is TradePage) {
    return RouteStatus.trade;
  }  else if (page.child is AssetPage) {
    return RouteStatus.asset;
  } else {
    return RouteStatus.unknown;
  }
}


///路由信息
class RouteStatusInfo {

  //路由状态
  final RouteStatus routeStatus;

  //页面
  final Widget page;

  RouteStatusInfo(this.routeStatus, this.page);
}


class ExNavigator extends _RouteJumpListener {

  RouteJumpListener _routeJump;

  List<RouteChangeListener> _listeners = [];
  
  RouteStatusInfo _current;

  //首页底部tab
  RouteStatusInfo _bottomTab;

  ///首页底部tab切换监听
  void onBottomTabChange(int index, Widget page) {
    _bottomTab = RouteStatusInfo(RouteStatus.home, page);
    _notify(_bottomTab);
  }

  ///注册路由跳转逻辑
  void registerRouteJump(RouteJumpListener routeJumpListener) {
    this._routeJump = routeJumpListener;
  }

  ///监听路由页面跳转
  void addListener(RouteChangeListener listener) {
    if (!_listeners.contains(listener)) {
      _listeners.add(listener);
    }
  }

  ///移除监听
  void removeListener(RouteChangeListener listener) {
    _listeners.remove(listener);
  }

  @override
  void onJumpTo(RouteStatus routeStatus, {Map args}) {
    _routeJump.onJumpTo(routeStatus, args: args);
  }



  ///通知路由页面变化
  void notify(List<MaterialPage> currentPages, List<MaterialPage> prePages) {
    if (currentPages == prePages) return;
    var current =
        RouteStatusInfo(getStatus(currentPages.last), currentPages.last.child);
    _notify(current);
  }


  void _notify(RouteStatusInfo current) {
    if(current.page is BottomNavigator && _bottomTab != null) {
      //如果打开的是首页，则明确到首页具体的tab
      current = _bottomTab;
    }

    _listeners.forEach((listener) {
      listener(current, _current);
    });
    _current = current;
  }



  //单例实现
  static ExNavigator _instance;
  ExNavigator._();
  static ExNavigator getInstance() {
    if(_instance == null) {
      _instance = ExNavigator._();
    }
    return _instance;
  }
}



///抽象类供ExNavigator实现
abstract class _RouteJumpListener {
  void onJumpTo(RouteStatus routeStatus, {Map args});
}

typedef OnJumpTo = void Function(RouteStatus routeStatus, {Map args});

///定义路由跳转逻辑要实现的功能
class RouteJumpListener {
  final OnJumpTo onJumpTo;
  RouteJumpListener({this.onJumpTo});
}