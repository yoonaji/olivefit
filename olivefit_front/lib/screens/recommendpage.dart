import 'package:flutter/material.dart';
import 'package:url_launcher/url_launcher.dart';
import 'dart:convert';
import 'package:http/http.dart' as http;
import 'board_page.dart'; // 게시판 페이지
import 'MyPage.dart'; // 마이페이지

class ProductPage extends StatefulWidget {
  final int userId;
  final String token;
  final String username;

   const ProductPage({
    super.key,
    required this.userId,
    required this.token,
    required this.username,
  });
  //, required this.userId

  @override
  State<ProductPage> createState() => _ProductPageState();
}

class _ProductPageState extends State<ProductPage> {
  String selectedCategory = "allinone";
  List<dynamic> products = [];

  final List<String> categories = ["allinone", "set", "skin", "essence", "cream", "lotion"];

  @override
  void initState() {
    super.initState();
    fetchProducts();
  }

  final dummyUserId = 8;
  Future<void> fetchProducts() async {
    final response = await http.get(
    Uri.parse('https://olivefit-back-649511210818.us-central1.run.app/api/recommend/${selectedCategory.toLowerCase()}/${widget.userId}'),
    headers: {
      'Content-Type': 'application/json',
      'Authorization': 'Bearer ${widget.token}',
    },
  );

  if (response.statusCode == 200) {
    setState(() {
      products = json.decode(utf8.decode(response.bodyBytes));
    });
  } else {
    debugPrint("Error fetching data: ${response.statusCode}");
  }
  }

  void _changeCategory(String category) {
    setState(() {
      selectedCategory = category;
    });
    fetchProducts();
  }

  void _launchURL(String url) async {
  final encodedUrl = Uri.encodeFull(url);  // ✅ 한글 인코딩
  final uri = Uri.parse(encodedUrl);

  if (!await canLaunchUrl(uri)) {
    debugPrint("❌ 이 URL을 열 수 없습니다: $url");
    return;
  }

  final launched = await launchUrl(
    uri,
    mode: LaunchMode.externalApplication, // ✅ 외부 브라우저로 열기
  );

  if (!launched) {
    debugPrint("❌ Launch 실패: $url");
  }
 }




  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text("skin:fit")),
      body: Column(
        children: [
          SizedBox(
            height: 50,
            child: ListView.builder(
              scrollDirection: Axis.horizontal,
              itemCount: categories.length,
              itemBuilder: (context, index) {
                final category = categories[index];
                return GestureDetector(
                  onTap: () => _changeCategory(category),
                  child: Container(
                    alignment: Alignment.center,
                    padding: const EdgeInsets.symmetric(horizontal: 16),
                    decoration: BoxDecoration(
                      border: Border(
                        bottom: BorderSide(
                          color: selectedCategory == category
                              ? Colors.black
                              : Colors.transparent,
                          width: 2,
                        ),
                      ),
                    ),
                    child: Text(category,
                        style: TextStyle(
                            color: selectedCategory == category
                                ? Colors.black
                                : Colors.grey)),
                  ),
                );
              },
            ),
          ),
          Expanded(
            child: ListView.builder(
              itemCount: products.length,
              itemBuilder: (context, index) {
                final item = products[index];
                return GestureDetector(
                  onTap: () => _launchURL(item["link"]),
                  child: Card(
                    margin:
                        const EdgeInsets.symmetric(horizontal: 10, vertical: 5),
                    child: Row(
                      children: [
                        Image.network(
                          item["image"],
                          width: 100,
                          height: 100,
                          fit: BoxFit.cover,
                        ),
                        const SizedBox(width: 10),
                        Expanded(
                          child: Column(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              Text(item["brand"],
                                  style: const TextStyle(
                                      fontWeight: FontWeight.bold)),
                              Text(item["name"]),
                              Text("평점: ${item["score"]}"),
                              Text("가격: ${item["price"].toString()}"),
                            ],
                          ),
                        ),
                      ],
                    ),
                  ),
                );
              },
            ),
          )
        ],
      ),
      bottomNavigationBar: BottomNavigationBar(
        currentIndex: 0,
  onTap: (index) {
    if (index == 1) {
      Navigator.pushReplacement(
        context,
        MaterialPageRoute(builder: (context) =>  BoardPage(
          token: widget.token,
          userId: widget.userId,
          username: widget.username,
        )),
      );
    }
    else if (index == 2) {
      Navigator.pushReplacement(
        context,
        MaterialPageRoute(
          builder: (context) => MyPage(
            token: widget.token,
            username: widget.username,
            userId: widget.userId
      ),
      ),
      );
    }
  },
        items: const [
          BottomNavigationBarItem(icon: Icon(Icons.star), label: "추천"),
          BottomNavigationBarItem(icon: Icon(Icons.forum), label: "게시판"),
          BottomNavigationBarItem(icon: Icon(Icons.person), label: "마이페이지"),
        ],
      ),
    );
  }
}
