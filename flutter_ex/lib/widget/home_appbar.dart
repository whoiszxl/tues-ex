import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_ex/navigator/ex_navigator.dart';

///主页appbar
homeAppBar() {

  return Padding(
      padding: EdgeInsets.only(left: 10, right: 10),
      child: Row(
        children: [
          InkWell(
            onTap: () {
              ExNavigator.getInstance().onJumpTo(RouteStatus.login);
            },
            child: ClipRRect(
              borderRadius: BorderRadius.circular(13),
              child: Image(
                height: 26,
                width: 26,
                image: AssetImage('images/avatar.png'),
              ),
            ),
          ),
          Expanded(
              child: Padding(
                padding: EdgeInsets.only(left: 15, right: 15),
                child: ClipRRect(
                  borderRadius: BorderRadius.circular(16),
                  child: Container(
                    padding: EdgeInsets.only(left: 10),
                    height: 32,
                    alignment: Alignment.centerLeft,
                    child: Icon(Icons.search, color: Colors.grey),
                    decoration: BoxDecoration(color: Colors.grey[100]),
                  ),
                ),
              )),
          Icon(
            Icons.explore_outlined,
            color: Colors.grey,
          ),
          Padding(
            padding: EdgeInsets.only(left: 12),
            child: Icon(
              Icons.mail_outline,
              color: Colors.grey,
            ),
          ),
        ],
      ),
    );


}