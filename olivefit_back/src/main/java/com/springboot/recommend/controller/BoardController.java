package com.springboot.recommend.controller;

import com.springboot.recommend.dto.BoardRequestDto;
import com.springboot.recommend.dto.BoardResponseDto;
import com.springboot.recommend.dto.MessageResponse;
import com.springboot.recommend.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardController {

    private final BoardService boardService;

    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody BoardRequestDto requestDto) {
        boardService.createPost(requestDto);
        return ResponseEntity.ok(new MessageResponse("게시글이 등록되었습니다."));
    }

    @GetMapping
    public ResponseEntity<List<BoardResponseDto>> getAllPosts() {
        return ResponseEntity.ok(boardService.getAllPosts());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePost(@PathVariable Long id, @RequestBody BoardRequestDto requestDto) {
        boardService.updatePost(id, requestDto);
        return ResponseEntity.ok(new MessageResponse("게시글이 수정되었습니다."));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id) {
        boardService.deletePost(id);
        return ResponseEntity.ok(new MessageResponse("게시글이 삭제되었습니다."));
    }
}
