import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'recommendpage.dart'; // 추천 결과 페이지

class Survey extends StatefulWidget {
  const Survey({super.key});

  @override
  State<Survey> createState() => _SurveyState();
}

class _SurveyState extends State<Survey> {

   final int _userId = 7; // ✅ 하드코딩된 더미 유저 ID

  String? _q1Answer;
  String? _q2Answer;
  String? _q3Answer;
  String? _q4Answer;
  String _q6Answer = 'B';
  String? _q5Answer;
  String? _q7Answer;

  Future<void> _submitSurvey() async {
    final url = Uri.parse('http://192.168.0.22:8080/api/survey');
    final response = await http.post(
      url,
      headers: {'Content-Type': 'application/json'},
      body: jsonEncode({
        "userId": _userId,
        "skinTypeAnswers": [
          _q1Answer ?? '',
          _q2Answer ?? '',
          _q3Answer ?? '',
          _q4Answer ?? '',
          _q6Answer,
        ],
        "skinConcerns": _q5Answer ?? '',
        "sensitivityLevel": _q7Answer ?? '',
      }),
    );

    if (response.statusCode == 200) {
      print('제출 성공: ${response.body}');
      Navigator.push(
        context,
        MaterialPageRoute(
          builder: (context) => const ProductPage(), // userId 필요 시 여기 수정
        ),
      );
    } else {
      print('제출 실패: ${response.statusCode}');
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('설문조사'),
        backgroundColor: Colors.white,
        foregroundColor: Colors.black,
        elevation: 0,
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.symmetric(horizontal: 20, vertical: 40),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            const Text(
              'skin:fit',
              style: TextStyle(
                color: Color(0xFF2F88FF),
                fontSize: 24,
                fontWeight: FontWeight.bold,
              ),
            ),
            const SizedBox(height: 30),
            const Text(
              '몇 가지 응답을 완료해주세요!',
              style: TextStyle(
                color: Colors.black,
                fontSize: 20,
                fontWeight: FontWeight.w600,
              ),
            ),
            const SizedBox(height: 30),

            const Text('1. 아침에 세안한 후, 점심쯤 내 피부는 어떤가요?'),
            _buildRadioGroup(
              questionValue: _q1Answer,
              onChanged: (val) => setState(() => _q1Answer = val),
              options: const [
                'A. 얼굴 전체가 번들거림',
                'B. T존만 약간 번들거리고 U존은 당김',
                'C. 전체적으로 당기거나 건조함'
              ],
            ),
            const SizedBox(height: 20),

            const Text('2. 세안 후 아무것도 바르지 않았을 때 피부 상태는?'),
            _buildRadioGroup(
              questionValue: _q2Answer,
              onChanged: (val) => setState(() => _q2Answer = val),
              options: const [
                'A. 금방 기름지기 시작함',
                'B. 볼은 당기는데 이마나 코는 괜찮음',
                'C. 전체가 건조하고 당김이 지속됨'
              ],
            ),
            const SizedBox(height: 20),

            const Text('3. 모공 상태는 어떤가요?'),
            _buildRadioGroup(
              questionValue: _q3Answer,
              onChanged: (val) => setState(() => _q3Answer = val),
              options: const [
                'A. 전체적으로 모공이 넓고 눈에 띔',
                'B. T존만 넓고 볼은 좁음',
                'C. 눈에 띄지 않음'
              ],
            ),
            const SizedBox(height: 20),

            const Text('4. 계절에 따라 피부 타입이 바뀌는 편인가요?'),
            _buildRadioGroup(
              questionValue: _q4Answer,
              onChanged: (val) => setState(() => _q4Answer = val),
              options: const [
                'A. 항상 유분 많음',
                'B. 여름엔 유분, 겨울엔 건조함',
                'C. 늘 건조함'
              ],
            ),
            const SizedBox(height: 20),

            const Text('5. 현재 기초제품을 사용중이라면, 사용감은 어떤가요? (선택)'),
            _buildRadioGroup(
              questionValue: _q6Answer,
              onChanged: (val) => setState(() => _q6Answer = val ?? 'B'),
              options: const [
                'A. 번들거린다.',
                'B. 적당한 수분감이 유지된다.',
                'C. 건조하다.'
              ],
            ),
            const SizedBox(height: 20),

            const Text('6. 피부 고민 중 가장 와닿는 것은?'),
            _buildRadioGroup(
              questionValue: _q5Answer,
              onChanged: (val) => setState(() => _q5Answer = val),
              options: const [
                'A. 유분, 번들거림, 트러블',
                'B. 유분+당김 둘 다 있음',
                'C. 당김, 각질, 속건조'
              ],
            ),
            const SizedBox(height: 20),

            const Text('7. 기초제품이나 화장품 사용 후, 자극이 있으신가요?'),
            _buildRadioGroup(
              questionValue: _q7Answer,
              onChanged: (val) => setState(() => _q7Answer = val),
              options: const [
                'A. 자극없음',
                'B. 가끔 있음',
                'C. 자극 심함'
              ],
            ),
            const SizedBox(height: 40),

            Center(
              child: ElevatedButton(
                onPressed: _submitSurvey,
                style: ElevatedButton.styleFrom(
                  backgroundColor: const Color(0xFFD9D9D9),
                  padding: const EdgeInsets.symmetric(horizontal: 40, vertical: 14),
                ),
                child: const Text(
                  '제출하기',
                  style: TextStyle(color: Colors.black, fontSize: 16),
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildRadioGroup({
    required String? questionValue,
    required void Function(String?) onChanged,
    required List<String> options,
  }) {
    return Column(
      children: options
          .map(
            (option) => RadioListTile<String>(
              title: Text(option, style: const TextStyle(color: Colors.black)),
              value: option[0], // 'A', 'B', 'C'
              groupValue: questionValue,
              onChanged: onChanged,
              activeColor: Colors.blue,
            ),
          )
          .toList(),
    );
  }
}
