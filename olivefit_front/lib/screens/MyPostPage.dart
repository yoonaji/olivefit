import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;

class MyPostsPage extends StatefulWidget {
  final String token;
  final String username;

  const MyPostsPage({super.key, required this.token, required this.username});

  @override
  State<MyPostsPage> createState() => _MyPostsPageState();
}

class _MyPostsPageState extends State<MyPostsPage> {
  List<dynamic> posts = [];

  @override
  void initState() {
    super.initState();
    fetchMyPosts();
  }

  Future<void> fetchMyPosts() async {
    final response = await http.get(
      Uri.parse('http://192.168.0.22:8080/api/user/mine'),
      headers: {
        'Authorization': 'Bearer ${widget.token}',
      },
    );

    if (response.statusCode == 200) {
      setState(() {
        posts = json.decode(utf8.decode(response.bodyBytes));
      });
    } else {
      debugPrint('❌ 내 게시글 불러오기 실패: ${response.statusCode}');
    }
  }

  Future<void> deletePost(int postId) async {
    final response = await http.delete(
      Uri.parse('http://192.168.0.22:8080/api/board/$postId'),
      headers: {
        'Authorization': 'Bearer ${widget.token}',
      },
    );

    if (response.statusCode == 200) {
      fetchMyPosts();
    } else {
      debugPrint('❌ 삭제 실패: ${response.body}');
    }
  }

  void _showUpdateDialog(Map<String, dynamic> post) {
    final _titleController = TextEditingController(text: post['title']);
    final _contentController = TextEditingController(text: post['content']);

    showDialog(
      context: context,
      builder: (_) => AlertDialog(
        title: const Text("게시글 수정"),
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
          ElevatedButton(
            onPressed: () async {
              final response = await http.put(
                Uri.parse('http://192.168.0.22:8080/api/board/${post['id']}'),
                headers: {
                  'Authorization': 'Bearer ${widget.token}',
                  'Content-Type': 'application/json',
                },
                body: jsonEncode({
                  'title': _titleController.text,
                  'content': _contentController.text,
                }),
              );

              if (response.statusCode == 200) {
                Navigator.of(context).pop();
                fetchMyPosts();
              } else {
                debugPrint('❌ 수정 실패: ${response.body}');
              }
            },
            child: const Text("수정"),
          ),
        ],
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text("내 게시글 관리")),
      body: posts.isEmpty
          ? const Center(child: Text("작성한 게시글이 없습니다."))
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
                        Text("작성일: ${post['createdAt']}", style: const TextStyle(fontSize: 12, color: Colors.grey)),
                      ],
                    ),
                    trailing: Row(
                      mainAxisSize: MainAxisSize.min,
                      children: [
                        IconButton(
                          icon: const Icon(Icons.edit),
                          onPressed: () => _showUpdateDialog(post),
                        ),
                        IconButton(
                          icon: const Icon(Icons.delete),
                          onPressed: () => deletePost(post['id']),
                        ),
                      ],
                    ),
                  ),
                );
              },
            ),
      bottomNavigationBar: BottomNavigationBar(
        currentIndex: 1,
        onTap: (index) {
          // 원하는 페이지로 이동하게 구현
        },
        items: const [
          BottomNavigationBarItem(icon: Icon(Icons.home), label: '홈'),
          BottomNavigationBarItem(icon: Icon(Icons.person), label: '마이페이지'),
        ],
      ),
    );
  }
}
