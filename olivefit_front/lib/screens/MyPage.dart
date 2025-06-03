import 'package:flutter/material.dart';
import 'dart:convert';
import 'package:http/http.dart' as http;
import 'MyPostPage.dart'; // 게시글 관리 페이지
import 'board_page.dart'; // 게시판 페이지

class MyPage extends StatefulWidget {
  final String token;
  final int userId;
  final String username;

  const MyPage({
    super.key,
    required this.token,
    required this.userId,
    required this.username,
  });

  @override
  State<MyPage> createState() => _MyPageState();
}

class _MyPageState extends State<MyPage> {
  String? skinType;
  bool isLoading = true;

  @override
  void initState() {
    super.initState();
    fetchUserInfo();
  }

  Future<void> fetchUserInfo() async {
    try {
      final response = await http.get(
        Uri.parse('http://192.168.0.22:8080/api/user/mypage'),
        headers: {
          'Authorization': 'Bearer ${widget.token}',
          'Content-Type': 'application/json',
        },
      );

      if (response.statusCode == 200) {
        final data = json.decode(utf8.decode(response.bodyBytes));
        setState(() {
          skinType = data['skinType'];
          isLoading = false;
        });
      } else {
        debugPrint('❌ 사용자 정보 불러오기 실패: ${response.statusCode}');
        setState(() {
          isLoading = false;
        });
      }
    } catch (e) {
      debugPrint('❌ 에러 발생: $e');
      setState(() {
        isLoading = false;
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("마이페이지"),
        centerTitle: true,
      ),
      body: isLoading
          ? const Center(child: CircularProgressIndicator())
          : Padding(
              padding: const EdgeInsets.all(16.0),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  // 사용자 정보 섹션
                  Card(
                    child: Padding(
                      padding: const EdgeInsets.all(16.0),
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          Row(
                            children: [
                              const Icon(Icons.person, size: 24),
                              const SizedBox(width: 8),
                              Text(
                                "사용자 정보",
                                style: Theme.of(context).textTheme.titleLarge,
                              ),
                            ],
                          ),
                          const SizedBox(height: 12),
                          Text("사용자명: ${widget.username}"),
                          const SizedBox(height: 8),
                          Text("피부 타입: ${skinType ?? '정보 없음'}"),
                        ],
                      ),
                    ),
                  ),
                  const SizedBox(height: 20),
                  
                  // 메뉴 섹션
                  Text(
                    "메뉴",
                    style: Theme.of(context).textTheme.titleLarge,
                  ),
                  const SizedBox(height: 12),
                  
                  // 내 게시글 확인 버튼
                  Card(
                    child: ListTile(
                      leading: const Icon(Icons.article),
                      title: const Text("내 게시글 확인"),
                      subtitle: const Text("작성한 게시글을 확인하고 관리할 수 있습니다"),
                      trailing: const Icon(Icons.arrow_forward_ios),
                      onTap: () {
                        Navigator.push(
                          context,
                          MaterialPageRoute(
                            builder: (context) => MyPostsPage(
                              token: widget.token,
                              username: widget.username,
                            ),
                          ),
                        );
                      },
                    ),
                  ),
                  
                  const SizedBox(height: 8),
                  
                  // 추후 추가될 기능들을 위한 예시 메뉴들
                  Card(
                    child: ListTile(
                      leading: const Icon(Icons.settings),
                      title: const Text("설정"),
                      subtitle: const Text("앱 설정을 변경할 수 있습니다"),
                      trailing: const Icon(Icons.arrow_forward_ios),
                      onTap: () {
                        // TODO: 설정 페이지로 이동
                        ScaffoldMessenger.of(context).showSnackBar(
                          const SnackBar(content: Text("설정 기능은 준비 중입니다")),
                        );
                      },
                    ),
                  ),
                  
                  const SizedBox(height: 8),
                  
                  Card(
                    child: ListTile(
                      leading: const Icon(Icons.help),
                      title: const Text("도움말"),
                      subtitle: const Text("앱 사용법을 확인할 수 있습니다"),
                      trailing: const Icon(Icons.arrow_forward_ios),
                      onTap: () {
                        // TODO: 도움말 페이지로 이동
                        ScaffoldMessenger.of(context).showSnackBar(
                          const SnackBar(content: Text("도움말 기능은 준비 중입니다")),
                        );
                      },
                    ),
                  ),
                ],
              ),
            ),
      bottomNavigationBar: BottomNavigationBar(
        currentIndex: 2, // 마이페이지가 선택된 상태
        onTap: (index) {
          if (index == 0) {
            // 추천 페이지로 이동 (기존 ProductPage)
            Navigator.pop(context);
          } else if (index == 1) {
            // 게시판 페이지로 이동
            Navigator.pushReplacement(
              context,
              MaterialPageRoute(
                builder: (context) => BoardPage(
                  token: widget.token,
                  userId: widget.userId,
                  username: widget.username,
                ),
              ),
            );
          }
          // index == 2는 현재 페이지이므로 아무것도 하지 않음
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