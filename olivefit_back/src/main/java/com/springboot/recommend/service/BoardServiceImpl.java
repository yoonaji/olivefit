package com.springboot.recommend.service;

import com.springboot.recommend.dto.BoardRequestDto;
import com.springboot.recommend.dto.BoardResponseDto;
import com.springboot.recommend.entity.Board;
import com.springboot.recommend.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;

    @Override
    public void createPost(BoardRequestDto requestDto) {
        Board board = new Board();
        board.setTitle(requestDto.getTitle());
        board.setContent(requestDto.getContent());
        board.setAuthor(requestDto.getAuthor());
        boardRepository.save(board);
    }

    @Override
    public List<BoardResponseDto> getAllPosts() {
        return boardRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(board -> new BoardResponseDto(
                        board.getId(),
                        board.getTitle(),
                        board.getContent(),
                        board.getAuthor(),
                        board.getCreatedAt()))
                .collect(Collectors.toList());
    }
}
