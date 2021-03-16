import 'package:flutter/material.dart';

void main() {
  runApp(MyApp());
}


class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {

    return MaterialApp(
      title: "tues-ex",
      home: Scaffold(
        appBar: AppBar(
          title: Text('周二交易平台'),
        ),

        body: Center(
          child: Text('hello world'),
        )
      ),
    );
  }

}