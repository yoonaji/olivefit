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

    @Override
    public void updatePost(Long id, BoardRequestDto requestDto) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id=" + id));

        board.setTitle(requestDto.getTitle());
        board.setContent(requestDto.getContent());
        board.setAuthor(requestDto.getAuthor());
        // createdAt은 그대로 두는 게 일반적
        boardRepository.save(board);
    }

    @Override
    public void deletePost(Long id) {
        if (!boardRepository.existsById(id)) {
            throw new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id=" + id);
        }
        boardRepository.deleteById(id);
    }

}
