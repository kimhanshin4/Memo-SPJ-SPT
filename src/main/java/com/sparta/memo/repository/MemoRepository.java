package com.sparta.memo.repository;

import com.sparta.memo.entity.*;
import java.util.*;
import org.springframework.data.jpa.repository.*;

//@Repository // SimpleJpaRepository에 @Repository가 달려있다.
public interface MemoRepository extends JpaRepository<Memo, Long> {

    //SQL을 동적으로 처리 할 수 있다! SELECT문을 직접 작성하려면... 어휴
    List<Memo> findAllByOrderByModifiedAtDesc(); //"모두 찾아서, 정렬, ModifiedAt을 가지고, 내림차순으로"
//    List<Memo> findAllByUsername(String username); //'Username'에 의한 User가 작성한 모든 이름 찾기

    List<Memo> findAllByContentsContainsOrderByModifiedAtDesc(String Keyword);


}

//JDBC Template 잘가...
//public class MemoRepository {
//    private final JdbcTemplate jdbcTemplate;
//
//    public MemoRepository(JdbcTemplate jdbcTemplate) {
//        this.jdbcTemplate = jdbcTemplate; //생성자로 DI - 객체의 불변성을 지켜줄 수 있음
//    }
//
//    public Memo save(Memo memo) {
//        KeyHolder keyHolder = new GeneratedKeyHolder();
//
//        String sql = "INSERT INTO memo (username, contents) VALUES (?, ?)";
//        jdbcTemplate.update(con -> {
//                PreparedStatement preparedStatement = con.prepareStatement(sql,
//                    Statement.RETURN_GENERATED_KEYS);
//
//                preparedStatement.setString(1, memo.getUsername());
//                preparedStatement.setString(2, memo.getContents());
//                return preparedStatement;
//            },
//            keyHolder);
//
//        Long id = keyHolder.getKey().longValue();
//        memo.setId(id);
//        return memo;
//    }
//
//
//    public void update(Long id, MemoRequestDto requestDto) {
//        String sql = "UPDATE memo SET username = ?, contents = ? WHERE id = ?";
//        jdbcTemplate.update(sql, requestDto.getUsername(), requestDto.getContents(), id);
//    }
//
//    public void delete(Long id) {
//        String sql = "DELETE FROM memo WHERE id = ?";
//        jdbcTemplate.update(sql, id);
//    }
//
//    public Memo findById(Long id) {
//        String sql = "SELECT * FROM memo WHERE id = ?";
//
//        return jdbcTemplate.query(sql, resultSet -> {
//            if (resultSet.next()) {
//                Memo memo = new Memo();
//                memo.setUsername(resultSet.getString("username"));
//                memo.setContents(resultSet.getString("contents"));
//                return memo;
//            } else {
//                return null;
//            }
//        }, id);
//    }
//
//    public List<MemoResponseDto> findAll() {
//        String sql = "SELECT * From memo";
//
//        return jdbcTemplate.query(sql, new RowMapper<MemoResponseDto>() {
//            @Override
//            public MemoResponseDto mapRow(ResultSet rs, int rowNum) throws SQLException {
//                Long id = rs.getLong("id");
//                String username = rs.getString("username");
//                String contents = rs.getString("contents");
//                return new MemoResponseDto(id, username, contents);
//            }
//        });
//    }
//
//    @Transactional
//    public Memo createMemo(EntityManager em) {
//        Memo memo = em.find(Memo.class, 1);
//        memo.setUsername("Robbert");
//        memo.setContents("@Transactional 전파 테스트 중!");
//
//        System.out.println("createMemo 메서드 종료");
//        return memo;
//    }
//}
