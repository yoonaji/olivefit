import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'recommendpage.dart'; // 추천 페이지
import 'MyPage.dart'; // 마이페이지

class BoardPage extends StatefulWidget {
  final int userId;
  final String token;
  final String username;

  const BoardPage({super.key, required this.token, required this.username, required this.userId});

  @override
  State<BoardPage> createState() => _BoardPageState();
}

class _BoardPageState extends State<BoardPage> {
  List<dynamic> posts = [];

  final _titleController = TextEditingController();
  final _contentController = TextEditingController();

  @override
  void initState() {
    super.initState();
    fetchPosts();
  }

  Future<void> fetchPosts() async {
    final response = await http.get(
      Uri.parse('http://192.168.0.22:8080/api/board'),
      headers: {
        'Authorization': 'Bearer ${widget.token}',
        'Content-Type': 'application/json',
      },
    );

    if (response.statusCode == 200) {
      setState(() {
        posts = json.decode(utf8.decode(response.bodyBytes));
      });
    } else {
      debugPrint('❌ 게시글 불러오기 실패: ${response.statusCode}');
    }
  }

  Future<void> createPost() async {
    final response = await http.post(
      Uri.parse('http://192.168.0.22:8080/api/board'),
      headers: {
        'Authorization': 'Bearer ${widget.token}',
        'Content-Type': 'application/json',
      },
      body: jsonEncode({
        'title': _titleController.text,
        'content': _contentController.text,
        'author': widget.username,
      }),
    );

    if (response.statusCode == 200) {
      Navigator.of(context).pop();
      _titleController.clear();
      _contentController.clear();
      fetchPosts(); // 새로고침
    } else {
      debugPrint("❌ 게시글 작성 실패: ${response.statusCode}");
    }
  }

  void _showCreateDialog() {
    showDialog(
      context: context,
      builder: (_) => AlertDialog(
        title: const Text("게시글 작성"),
        content: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            TextField(
              controller: _titleController,
              decoration: const InputDecoration(labelText: "제목"),
            ),
            TextField(
              controller: _contentController,
              decoration: const InputDecoration(labelText: "내용"),
            ),
          ],
        ),
        actions: [
          TextButton(onPressed: () => Navigator.of(context).pop(), child: const Text("취소")),
          ElevatedButton(onPressed: createPost, child: const Text("작성")),
        ],
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text("게시판")),
      body: posts.isEmpty
          ? const Center(child: Text("작성된 게시글이 없습니다."))
          : ListView.builder(
              itemCount: posts.length,
              itemBuilder: (context, index) {
                final post = posts[index];
                return Card(
                  margin: const EdgeInsets.symmetric(horizontal: 10, vertical: 5),
                  child: ListTile(
                    title: Text(post['title'] ?? '', style: const TextStyle(fontWeight: FontWeight.bold)),
                    subtitle: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Text(post['content'] ?? ''),
                        const SizedBox(height: 4),
                        Text("작성자: ${post['author']}", style: const TextStyle(fontSize: 12, color: Colors.grey)),
                        Text("작성일: ${post['createdAt']}", style: const TextStyle(fontSize: 12, color: Colors.grey)),
                      ],
                    ),
                  ),
                );
              },
            ),
      floatingActionButton: FloatingActionButton(
        onPressed: _showCreateDialog,
        child: const Icon(Icons.add),
      ),
      bottomNavigationBar: BottomNavigationBar(
        currentIndex: 1, // 게시판이 선택된 상태
        onTap: (index) {
          if (index == 0) {
            // 추천 페이지로 이동
            Navigator.pushReplacement(
              context,
              MaterialPageRoute(
                builder: (context) => ProductPage(
                  token: widget.token,
                  userId: widget.userId,
                  username: widget.username,
                ),
              ),
            );
          } else if (index == 2) {
            // 마이페이지로 이동
            Navigator.pushReplacement(
              context,
              MaterialPageRoute(
                builder: (context) => MyPage(
                  token: widget.token,
                  userId: widget.userId,
                  username: widget.username,
                ),
              ),
            );
          }
          // index == 1은 현재 페이지이므로 아무것도 하지 않음
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