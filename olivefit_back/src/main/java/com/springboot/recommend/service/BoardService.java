package com.springboot.recommend.service;

import com.springboot.recommend.dto.BoardRequestDto;
import com.springboot.recommend.dto.BoardResponseDto;

import java.util.List;

public interface BoardService {
    void createPost(BoardRequestDto requestDto);
    List<BoardResponseDto> getAllPosts();
    void updatePost(Long id, BoardRequestDto requestDto);
    void deletePost(Long id);

}
