package com.springboot.recommend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.recommend.config.TestSecurityConfig;
import com.springboot.recommend.dto.BoardRequestDto;
import com.springboot.recommend.entity.Board;
import com.springboot.recommend.repository.BoardRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Import(TestSecurityConfig.class)
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class BoardControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("게시글 등록 테스트")
    void testCreatePost() throws Exception {
        BoardRequestDto requestDto = new BoardRequestDto("테스트 제목", "테스트 내용", "작성자");

        mockMvc.perform(post("/api/board")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("게시글이 등록되었습니다."));
    }

    @Test
    @DisplayName("게시글 전체 조회 테스트")
    void testGetAllPosts() throws Exception {
        boardRepository.saveAll(List.of(
                createBoard("제목1", "내용1", "작성자1"),
                createBoard("제목2", "내용2", "작성자2")
        ));

        mockMvc.perform(get("/api/board"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[*].title").value(Matchers.hasItems("제목1", "제목2")));

    }

    @Test
    @DisplayName("게시글 수정 테스트")
    void testUpdatePost() throws Exception {
        Board saved = boardRepository.save(createBoard("원래 제목", "원래 내용", "작성자"));

        BoardRequestDto updateDto = new BoardRequestDto("수정된 제목", "수정된 내용", "작성자");

        mockMvc.perform(put("/api/board/" + saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("게시글이 수정되었습니다."));
    }

    @Test
    @DisplayName("게시글 삭제 테스트")
    void testDeletePost() throws Exception {
        Board saved = boardRepository.save(createBoard("삭제 제목", "내용", "작성자"));

        mockMvc.perform(delete("/api/board/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("게시글이 삭제되었습니다."));
    }

    private Board createBoard(String title, String content, String author) {
        Board board = new Board();
        board.setTitle(title);
        board.setContent(content);
        board.setAuthor(author);
        return board;
    }
}
