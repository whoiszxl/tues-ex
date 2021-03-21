
///轮播图模型
class BannerModel {
  String name;
  int type;
  String pic;
  int status;
  int clickCount;
  String url;
  String note;
  int sort;
  String createdAt;
  String updatedAt;

  BannerModel(
      {this.name,
        this.type,
        this.pic,
        this.status,
        this.clickCount,
        this.url,
        this.note,
        this.sort,
        this.createdAt,
        this.updatedAt});

  BannerModel.fromJson(Map<String, dynamic> json) {
    name = json['name'];
    type = json['type'];
    pic = json['pic'];
    status = json['status'];
    clickCount = json['clickCount'];
    url = json['url'];
    note = json['note'];
    sort = json['sort'];
    createdAt = json['createdAt'];
    updatedAt = json['updatedAt'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['name'] = this.name;
    data['type'] = this.type;
    data['pic'] = this.pic;
    data['status'] = this.status;
    data['clickCount'] = this.clickCount;
    data['url'] = this.url;
    data['note'] = this.note;
    data['sort'] = this.sort;
    data['createdAt'] = this.createdAt;
    data['updatedAt'] = this.updatedAt;
    return data;
  }
}
