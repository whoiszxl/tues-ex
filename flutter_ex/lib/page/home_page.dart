
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_easyrefresh/easy_refresh.dart';
import 'package:flutter_ex/dao/home_dao.dart';
import 'package:flutter_ex/model/banner_model.dart';
import 'package:flutter_ex/widget/ex_banner.dart';
import 'package:flutter_ex/widget/home_appbar.dart';
import 'package:flutter_ex/widget/navigation_bar.dart';

class HomePage extends StatefulWidget {
  @override
  _HomePageState createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> with AutomaticKeepAliveClientMixin {

  @override
  bool get wantKeepAlive =>true;

  bool isNav = true;

  @override
  void initState() {
    super.initState();
  }

  @override
  void dispose() {
    super.dispose();
  }


  GlobalKey<RefreshIndicatorState> _easyRefreshKey =new GlobalKey<RefreshIndicatorState>();
  GlobalKey<MaterialFooterWidgetState> _footerKey = new GlobalKey<MaterialFooterWidgetState>();

  @override
  Widget build(BuildContext context) {
    super.build(context);
    return Scaffold(
      backgroundColor: Color.fromRGBO(244, 245, 245, 1.0),
      body: FutureBuilder(
        future: HomeDao.bannerList("1"),
        builder: (context, snapshot) {
          if(snapshot.hasData) {
            //var data=json.decode(snapshot.data.toString());
            List<BannerModel> bannerList = snapshot.data;

            return EasyRefresh(
              footer: ClassicalFooter(
                  key:_footerKey,
                  bgColor:Colors.white,
                  textColor: Colors.pink,
                  noMoreText: '没有了',
                  loadingText: '加载中',
                  loadReadyText:'上拉加载....'
              ),
              child: ListView(
                children: <Widget>[
                  //沉浸式Appbar
                  NavigationBar(
                    height: 50,
                    child: homeAppBar(),
                    color: Colors.white,
                    statusStyle: StatusStyle.DARK_CONTENT,
                  ),

                  //banner轮播
                  Padding(
                    padding: EdgeInsets.only(left: 7, right: 7, top: 5),
                    child: ExBanner(bannerList),
                  )
                ],
              ),
            );
          }else {
            return Center(
              child: Text("没有数据"),
            );
          }
        },
      ),
    );
  }
}