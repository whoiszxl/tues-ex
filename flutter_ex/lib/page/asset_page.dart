import 'package:flutter/material.dart';

class AssetPage extends StatefulWidget {
  @override
  _AssetPageState createState() => _AssetPageState();
}

class _AssetPageState extends State<AssetPage> {

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(),
      body: Container(
        child: Center(
          child: Text('资产'),
        ),
      ),
    );
  }
}