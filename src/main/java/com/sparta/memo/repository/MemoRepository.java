package com.sparta.memo.repository;

import com.sparta.memo.dto.*;
import com.sparta.memo.entity.*;
import java.sql.*;
import java.util.*;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.support.*;
import org.springframework.stereotype.*;

//@Component // 넌 이제부터 bean이다
@Repository //세상 명확한 ANtation
public class MemoRepository { //memoRepository 로 bean에 등록

    private final JdbcTemplate jdbcTemplate;

    public MemoRepository(JdbcTemplate jdbcTemplate) { //final 이기에 생성자 생성 후 넣을 탬플릿 매개인자로 삽입
        this.jdbcTemplate = jdbcTemplate; //생성자로 DI - 객체의 불변성을 지켜줄 수 있음
    }

    public Memo save(Memo memo) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        String sql = "INSERT INTO memo (username, contents) VALUES (?, ?)";
        jdbcTemplate.update(con -> {
                PreparedStatement preparedStatement = con.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);

                preparedStatement.setString(1, memo.getUsername());
                preparedStatement.setString(2, memo.getContents());
                return preparedStatement;
            },
            keyHolder);

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

    public Memo findById(Long id) {
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
                Long id = rs.getLong("id");
                String username = rs.getString("username");
                String contents = rs.getString("contents");
                return new MemoResponseDto(id, username, contents);
            }
        });
    }
}
