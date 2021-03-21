import 'package:flutter/material.dart';
import 'package:flutter_ex/model/coin_model.dart';

class DetailPage extends StatefulWidget {

  final CoinModel coinModel;
  const DetailPage(this.coinModel);

  @override
  _DetailPageState createState() => _DetailPageState();
}

class _DetailPageState extends State<DetailPage>
    with SingleTickerProviderStateMixin {
  AnimationController _controller;

  @override
  void initState() {
    super.initState();
    _controller = AnimationController(vsync: this);
  }

  @override
  void dispose() {
    super.dispose();
    _controller.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(),
      body: Container(
        child: Center(
          child: Text('币种详情 : ${widget.coinModel.coinId}'),
        ),
      ),
    );
  }
}