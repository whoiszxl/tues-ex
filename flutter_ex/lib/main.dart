import 'package:flutter/material.dart';
import 'package:flutter_ex/db/ex_cache.dart';
import 'package:flutter_ex/util/color.dart';
import 'package:flutter_ex/page/register_page.dart';

void main() {
  runApp(MyApp());
}


class MyApp extends StatelessWidget {

  @override
  Widget build(BuildContext context) {
    ExCache.getInstance();
    return MaterialApp(
      title: "tues-ex",
      theme: ThemeData(
        primarySwatch: white,
      ),

      home: RegisterPage()
    );
  }

}