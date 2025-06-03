package com.springboot.recommend.controller;

import com.springboot.recommend.dto.BoardResponseDto;
import com.springboot.recommend.dto.MyPageResponseDto;
import com.springboot.recommend.entity.Board;
import com.springboot.recommend.entity.User;
import com.springboot.recommend.repository.BoardRepository;
import com.springboot.recommend.repository.SurveyRepository;
import com.springboot.recommend.repository.UserRepository;
import com.springboot.recommend.security.services.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final SurveyRepository surveyRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    @GetMapping("/mypage")
    public ResponseEntity<?> getSkinTypeOnly(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        String username = userDetails.getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("사용자 없음"));

        String skinType = surveyRepository.findSkinTypeByUserId(user.getId());
        return ResponseEntity.ok(Map.of("skinType", skinType)); // 단일 응답
    }

    @GetMapping("/mine")
    public ResponseEntity<List<BoardResponseDto>> getMyPosts(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        String username = userDetails.getUsername();
        List<Board> boards = boardRepository.findAllByAuthor(username);

        List<BoardResponseDto> postDtos = boards.stream()
                .map(board -> new BoardResponseDto(
                        board.getId(),
                        board.getTitle(),
                        board.getContent(),
                        board.getAuthor(),
                        board.getCreatedAt()
                )).toList();

        return ResponseEntity.ok(postDtos);
    }


}
