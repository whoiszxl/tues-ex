import 'package:flutter/material.dart';
import 'package:flutter_ex/navigator/ex_navigator.dart';

class HomePage extends StatefulWidget {
  @override
  _HomePageState createState() => _HomePageState();
}

class _HomePageState extends State<HomePage> with AutomaticKeepAliveClientMixin {

  @override
  bool get wantKeepAlive =>true;

  @override
  void initState() {
    super.initState();
  }

  @override
  void dispose() {
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Container(
        child: Center(
          child: InkWell(
            child: Text('登录'),
            onTap: () {
              ExNavigator.getInstance().onJumpTo(RouteStatus.login);
            },
          ),
        ),
      ),
    );
  }
}