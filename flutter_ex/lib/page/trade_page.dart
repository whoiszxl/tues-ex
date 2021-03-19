import 'package:flutter/material.dart';

class TradePage extends StatefulWidget {
  @override
  _TradePageState createState() => _TradePageState();
}

class _TradePageState extends State<TradePage> {
  
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(),
      body: Container(
        child: Center(
          child: Text('交易'),
        ),
      ),
    );
  }
}