package com.sparta.memo.repository;

import com.sparta.memo.dto.*;
import com.sparta.memo.entity.*;
import java.sql.*;
import java.util.*;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.support.*;

public class MemoRepository {

    private final JdbcTemplate jdbcTemplate; //"JDBC 탬플릿 사용!" //JDBC 탬플릿 객체는 Spring에서 따로 관리

    public MemoRepository(JdbcTemplate jdbcTemplate) { //final 이기에 생성자 생성 후 넣을 탬플릿 매개인자로 삽입
        this.jdbcTemplate = jdbcTemplate;
    }

    public Memo save(Memo memo) {
        //DB 저장
        KeyHolder keyHolder = new GeneratedKeyHolder(); // 기본 키를 반환받기 위한 객체

        String sql = "INSERT INTO memo (username, contents) VALUES (?, ?)"; //INSERT는 값이 항상 바뀌어 동적 처리를 위한 ? 삽입
        jdbcTemplate.update(con -> { //INSERT,UPDATE,DELETE 사용시 사용하는 .update()메서드
                PreparedStatement preparedStatement = con.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);

                preparedStatement.setString(1, memo.getUsername()); //VALUES ("?",?)";
                preparedStatement.setString(2, memo.getContents()); //VALUES (?,"?")";
                return preparedStatement;
            },
            keyHolder);

        // DB Insert 후 받아온 기본키 확인
        Long id = keyHolder.getKey().longValue();
        memo.setId(id);
        return memo;
    }


    public void update(Long id, MemoRequestDto requestDto) {
        String sql = "UPDATE memo SET username = ?, contents = ? WHERE id = ?";
        jdbcTemplate.update(sql, requestDto.getUsername(), requestDto.getContents(), id);
    }

    public void delete(Long id) {
        String sql = "DELETE FROM memo WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public Memo findById(Long id) { //접근제어자 변경 private->public
        // DB 조회
        String sql = "SELECT * FROM memo WHERE id = ?";

        return jdbcTemplate.query(sql, resultSet -> {
            if (resultSet.next()) {
                Memo memo = new Memo();
                memo.setUsername(resultSet.getString("username"));
                memo.setContents(resultSet.getString("contents"));
                return memo;
            } else {
                return null;
            }
        }, id);
    }

    public List<MemoResponseDto> findAll() {
        String sql = "SELECT * From memo";

        return jdbcTemplate.query(sql, new RowMapper<MemoResponseDto>() {
            @Override
            public MemoResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                // SQL 의 결과로 받아온 Memo 데이터들을 MemoResponseDto 타입으로 변환해줄 메서드
                Long id = rs.getLong("id");
                String username = rs.getString("username");
                String contents = rs.getString("contents");
                return new MemoResponseDto(id, username, contents);
            }
        });
    }
}
