import 'dart:io';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_ex/dao/member_dao.dart';
import 'package:flutter_ex/db/ex_cache.dart';
import 'package:flutter_ex/http/core/ex_error.dart';
import 'package:flutter_ex/http/core/ex_net.dart';
import 'package:flutter_ex/model/coin_model.dart';
import 'package:flutter_ex/navigator/bottom_navigator.dart';
import 'package:flutter_ex/navigator/ex_navigator.dart';
import 'package:flutter_ex/page/detail_page.dart';
import 'package:flutter_ex/page/login_page.dart';
import 'package:flutter_ex/util/color.dart';
import 'package:flutter_ex/page/register_page.dart';
import 'package:flutter_ex/util/toast.dart';

void main() {
  runApp(ExApp());
}


class ExApp extends StatefulWidget {

  @override
  _ExAppState createState() => _ExAppState();
}

class _ExAppState extends State<ExApp> {

  ExRouteDelegate _routeDelegate = new ExRouteDelegate();


  @override
  Widget build(BuildContext context) {
    return FutureBuilder<ExCache>(
      //初始化cache
      future: ExCache.preInit(),
      builder: (BuildContext context, AsyncSnapshot<ExCache> snapshot) {

        var widget = snapshot.connectionState == ConnectionState.done ?
          Router(routerDelegate: _routeDelegate) : Scaffold(body: Center(child: CircularProgressIndicator(),));
        return MaterialApp(
          home: widget,
          theme: ThemeData(primarySwatch: white),
        );
      },
    );
  }

}


class ExRouteDelegate extends RouterDelegate<ExRoutePath>
    with ChangeNotifier, PopNavigatorRouterDelegateMixin<ExRoutePath> {
  final GlobalKey<NavigatorState> navigatorKey;

  RouteStatus _routeStatus = RouteStatus.home;
  List<MaterialPage> pages = [];
  CoinModel coinModel;

  ExRouteDelegate() : navigatorKey = GlobalKey<NavigatorState>() {
    //路由跳转
    ExNavigator.getInstance().registerRouteJump(
      RouteJumpListener(onJumpTo: (RouteStatus routeStatus, {Map args}) {
        _routeStatus = routeStatus;
        if(routeStatus == RouteStatus.detail) {
          this.coinModel = args['coinModel'];
        }

        notifyListeners();
      }));

    ExNet.getInstance().setErrorInterceptor((error) {
      if(error is NeedLogin) {
        ExCache.getInstance().setString(MemberDao.LOCAL_TOKEN, null);
        ExNavigator.getInstance().onJumpTo(RouteStatus.login);
      }

    });
  }

  @override
  Future<void> setNewRoutePath(ExRoutePath path) async {}

  bool get hasLogin => MemberDao.getLocalToken() != null;

  ///获取路由状态，如果页面不是注册状态并且未登录都强制跳登录，如果币种模型不为空则跳转币种详情
  RouteStatus get routeStatus {
    if (_routeStatus != RouteStatus.register && _routeStatus != RouteStatus.home && !hasLogin) {
      return _routeStatus = RouteStatus.login;
    } else if (coinModel != null) {
      return _routeStatus = RouteStatus.detail;
    } else {
      return _routeStatus;
    }
  }

  @override
  Widget build(BuildContext context) {
    //获取当前路由状态在页面List中的位置
    var index = getPageIndex(pages, routeStatus);
    List<MaterialPage> tempPages = pages;

    if(index != -1) {
      //此时说明需要打开的页面在List中已经存在，则需要将这个页面和其上面所有页面都出栈,此处要求栈内只有一个同页面的实例。
      tempPages = tempPages.sublist(0, index);
    }

    var page;
    switch(routeStatus) {
      case RouteStatus.home:
        pages.clear();
        page = pageWrap(BottomNavigator());
        break;
      case RouteStatus.detail:
        page = pageWrap(DetailPage(coinModel));
        break;
      case RouteStatus.login:
        page = pageWrap(LoginPage());
        break;
      case RouteStatus.register:
        page = pageWrap(RegisterPage());
        break;
      default:
        page = pageWrap(BottomNavigator());
    }

    //重新创建一个数组，否则pages因引用没有改变路由不会生效
    tempPages = [...tempPages, page];
    //通知路由发生变化
    ExNavigator.getInstance().notify(tempPages, pages);

    pages = tempPages;

    return WillPopScope(
      //fix Android物理返回键，无法返回上一页问题@https://github.com/flutter/flutter/issues/66349
      onWillPop: () async => !await navigatorKey.currentState.maybePop(),
      child: Navigator(
        key: navigatorKey,
        pages: pages,
        onPopPage: (route, result) {
          if(route.settings is MaterialPage) {
            //登录页未登录返回拦截
            if ((route.settings as MaterialPage).child is LoginPage) {
              if (!hasLogin) {
                showErrorToast("请先登录");
                return false;
              }
            }
          }
          //执行返回操作
          if (!route.didPop(result)) {
            return false;
          }
          var tempPages = [...pages];
          pages.removeLast();
          //通知路由发生变化
          ExNavigator.getInstance().notify(pages, tempPages);
          return true;
        },
      ),
    );
  }
}

///定义路由数据，path
class ExRoutePath {
  final String location;

  ExRoutePath.home() : location = "/";

  ExRoutePath.detail() : location = "/detail";
}
