import 'package:shared_preferences/shared_preferences.dart';

///SharedPreferences缓存管理工具
class ExCache {

  SharedPreferences _sp;

  static ExCache _instance;

  ///构造方法，进行初始化
  ExCache ._() {
    _init();
  }

  ///执行初始化sp逻辑
  void _init() async {
    if(_sp == null) {
      _sp = await SharedPreferences.getInstance();
    }
  }

  ///获取sp实例
  static ExCache getInstance() {
    if(_instance == null) {
      _instance = ExCache._();
    }
    return _instance;
  }


  ///保存字符串
  setString(String key, String value) {
    _sp.setString(key, value);
  }

  ///保存双精度小数
  setDouble(String key, double value) {
    _sp.setDouble(key, value);
  }

  ///保存整型
  setInt(String key, int value) {
    _sp.setInt(key, value);
  }

  ///保存true or false
  setBool(String key, bool value) {
    _sp.setBool(key, value);
  }

  ///保存字符串集合
  setStringList(String key, List<String> value) {
    _sp.setStringList(key, value);
  }

  ///泛型获取
  T get<T>(String key) {
    return _sp.get(key);
  }
}