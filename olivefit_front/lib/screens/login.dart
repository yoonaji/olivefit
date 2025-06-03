import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'survey.dart';
import 'recommendpage.dart'; // 🔄 추가: 설문 완료 시 이동할 페이지
import 'package:shared_preferences/shared_preferences.dart';
import 'signup.dart';

class LoginPage extends StatefulWidget {
  const LoginPage({super.key});

  @override
  State<LoginPage> createState() => _LoginPageState();
}

class _LoginPageState extends State<LoginPage> {
  final TextEditingController _idController = TextEditingController();
  final TextEditingController _passwordController = TextEditingController();

  Future<void> _login() async {
    final response = await http.post(
      Uri.parse('http://192.168.0.22:8080/api/auth/signin'),
      headers: {'Content-Type': 'application/json'},
      body: jsonEncode({
        'username': _idController.text,
        'password': _passwordController.text,
      }),
    );

    if (response.statusCode == 200) {
      final data = jsonDecode(response.body);
      final token = data['accessToken'];
      final username = data['username'];
      final userId = data['id']; // 🔄 변경: userId 저장

      final prefs = await SharedPreferences.getInstance();
      await prefs.setString('accessToken', token);
      await prefs.setString('username', username);

      await _checkSurveyStatus(token, username, userId); // 🔄 변경
    } else {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('로그인 실패: 아이디 또는 비밀번호 확인')),
      );
    }
  }

  // 🔄 추가: 설문 존재 여부 확인 후 분기
  Future<void> _checkSurveyStatus(String token, String username, int userId) async {
    final response = await http.get(
      Uri.parse('http://192.168.0.22:8080/api/user/has-survey'),
      headers: {'Authorization': 'Bearer $token'},
    );

    if (response.statusCode == 200) {
      final hasSurvey = jsonDecode(response.body);

      if (!hasSurvey) {
        Navigator.pushReplacement(
          context,
          MaterialPageRoute(
            builder: (context) => Survey(
              token: token,
              username: username,
              userId: userId,
            ),
          ),
        );
      } else {
        Navigator.pushReplacement(
          context,
          MaterialPageRoute(
            builder: (context) => ProductPage(
              token: token,
              username: username,
              userId: userId,
            ),
          ),
        );
      }
    } else {
      debugPrint("설문 여부 확인 실패: ${response.statusCode}");
    }
  }

  void _goToSignup() {
    Navigator.push(
      context,
      MaterialPageRoute(builder: (context) => const SignupPage()),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.white,
      resizeToAvoidBottomInset: true,
      body: SafeArea(
        child: SingleChildScrollView(
          padding: EdgeInsets.only(
            left: 24,
            right: 24,
            top: 80,
            bottom: MediaQuery.of(context).viewInsets.bottom + 24,
          ),
          child: ConstrainedBox(
            constraints: BoxConstraints(
              minHeight: MediaQuery.of(context).size.height -
                  MediaQuery.of(context).padding.top -
                  MediaQuery.of(context).padding.bottom,
            ),
            child: IntrinsicHeight(
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  const Text.rich(
                    TextSpan(
                      children: [
                        TextSpan(
                            text: 'skin:',
                            style: TextStyle(
                                color: Color(0xFF2F88FF), fontSize: 24)),
                        TextSpan(
                            text: 'fit',
                            style: TextStyle(
                                color: Color(0xFF2F88FF), fontSize: 24)),
                      ],
                    ),
                  ),
                  const SizedBox(height: 60),
                  const Text('로그인',
                      style:
                          TextStyle(fontSize: 18, fontWeight: FontWeight.bold)),
                  const SizedBox(height: 12),
                  Row(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Expanded(
                        child: Column(
                          children: [
                            TextField(
                              controller: _idController,
                              decoration: const InputDecoration(
                                hintText: 'id:',
                                filled: true,
                                fillColor: Color(0xFFD9D9D9),
                              ),
                            ),
                            const SizedBox(height: 12),
                            TextField(
                              controller: _passwordController,
                              obscureText: true,
                              decoration: const InputDecoration(
                                hintText: 'password:',
                                filled: true,
                                fillColor: Color(0xFFD9D9D9),
                              ),
                            ),
                          ],
                        ),
                      ),
                      const SizedBox(width: 12),
                      ElevatedButton(
                        onPressed: _login,
                        style: ElevatedButton.styleFrom(
                          backgroundColor: const Color(0xFFD9D9D9),
                        ),
                        child: const Text('login',
                            style: TextStyle(color: Colors.black)),
                      )
                    ],
                  ),
                  const SizedBox(height: 20),
                  GestureDetector(
                    onTap: _goToSignup,
                    child: const Text(
                      '회원가입',
                      style: TextStyle(
                          decoration: TextDecoration.underline,
                          color: Colors.black54),
                    ),
                  )
                ],
              ),
            ),
          ),
        ),
      ),
    );
  }
}
