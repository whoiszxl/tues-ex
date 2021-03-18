import 'package:flutter/material.dart';
import 'package:flutter_ex/db/ex_cache.dart';
import 'package:flutter_ex/http/core/ex_net.dart';
import 'package:flutter_ex/http/request/register_sms_request.dart';

void main() {
  runApp(MyApp());
}


class MyApp extends StatelessWidget {

  void test() async {
    try{
                RegisterSmsRequest request = new RegisterSmsRequest();
                request.addParam("mobile", "17688900411");
                var result = await ExNet.getInstance().request(request);
                print(result);
              }catch(e) {

              }
  }

  @override
  Widget build(BuildContext context) {
    ExCache.getInstance();
    return MaterialApp(
      title: "tues-ex",
      home: Scaffold(
        appBar: AppBar(
          title: Text('周二交易平台'),
        ),

        body: Center(
          child: InkWell(
            child: Text('hello world'),
            onTap: () {
              test();

            },
          )
        )
      ),
    );
  }

}