
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_ex/navigator/ex_navigator.dart';
import 'package:flutter_ex/page/asset_page.dart';
import 'package:flutter_ex/page/home_page.dart';
import 'package:flutter_ex/page/market_page.dart';
import 'package:flutter_ex/page/trade_page.dart';
import 'package:flutter_ex/util/color.dart';

///底部导航按钮
class BottomNavigator extends StatefulWidget {

  @override
  _BottomNavigatorState createState() => _BottomNavigatorState();

}


class _BottomNavigatorState extends State<BottomNavigator> {
  final _defaultColor = Colors.grey;
  final _activeColor = primary;

  int _currentIndex = 0;
  static int initialPage = 0;

  final PageController _controller = PageController(initialPage: initialPage);

  List<Widget> _pages;
  bool _hasBuild = false;

  @override
  Widget build(BuildContext context) {
    _pages = [HomePage(), MarketPage(), TradePage(), AssetPage()];
    if(!_hasBuild) {
      //页面第一次打开时通知打开的是那个tab
      ExNavigator.getInstance().onBottomTabChange(initialPage, _pages[initialPage]);
      _hasBuild = true;
    }

    return Scaffold(
      body: PageView(
        controller: _controller,
        children: _pages,
        onPageChanged: (index) => _onJumpTo(index, pageChange: true),
        physics: NeverScrollableScrollPhysics(),
      ),

      bottomNavigationBar: BottomNavigationBar(
        currentIndex: _currentIndex,
        onTap: (index) => _onJumpTo(index),
        type: BottomNavigationBarType.fixed,
        selectedItemColor: _activeColor,
        
        items: [
          _bottomItem('首页', Icons.home, 0),
          _bottomItem('行情', Icons.music_off, 1),
          _bottomItem('交易', Icons.search, 2),
          _bottomItem('资产', Icons.money, 3),
        ],
      ),
    );
    
  }

  _bottomItem(String title, IconData icon, int index) {
    return BottomNavigationBarItem(
      label: title,
      icon: Icon(icon, color: _defaultColor),
      activeIcon: Icon(icon, color: _activeColor)
    );
  }

  void _onJumpTo(int index, {pageChange = false}) {
    if (!pageChange) {
      //让PageView展示对应tab
      _controller.jumpToPage(index);
    } else {
      ExNavigator.getInstance().onBottomTabChange(index, _pages[index]);
    }
    setState(() {
      //控制选中第一个tab
      _currentIndex = index;
    });
  }
}
