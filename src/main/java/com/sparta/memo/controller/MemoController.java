package com.sparta.memo.controller;

import com.sparta.memo.dto.MemoRequestDto;
import com.sparta.memo.dto.MemoResponseDto;
import com.sparta.memo.entity.Memo;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.web.bind.annotation.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@RestController
@RequestMapping("/api")
public class MemoController {

    private final JdbcTemplate jdbcTemplate; //"JDBC 탬플릿 사용!" //JDBC 탬플릿 객체는 Spring에서 따로 관리

    public MemoController(JdbcTemplate jdbcTemplate) { //final 이기에 생성자 생성 후 넣을 탬플릿 매개인자로 삽입
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostMapping("/memos")
    public MemoResponseDto createMemo(@RequestBody MemoRequestDto requestDto) {
        // RequestDto -> Entity
        Memo memo = new Memo(requestDto);

        // DB 저장
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

        // Entity -> ResponseDto
        MemoResponseDto memoResponseDto = new MemoResponseDto(memo);

        return memoResponseDto;
    }

    @GetMapping("/memos")
    public List<MemoResponseDto> getMemos() {
        // DB 조회
        String sql = "SELECT * FROM memo";

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

    @PutMapping("/memos/{id}")
    public Long updateMemo(@PathVariable Long id, @RequestBody MemoRequestDto requestDto) {
        // 해당 메모가 DB에 존재하는지 확인
        Memo memo = findById(id);
        if (memo != null) {
            // memo 내용 수정
            String sql = "UPDATE memo SET username = ?, contents = ? WHERE id = ?";
            jdbcTemplate.update(sql, requestDto.getUsername(), requestDto.getContents(), id);

            return id;
        } else {
            throw new IllegalArgumentException("선택한 메모는 존재하지 않습니다.");
        }
    }

    @DeleteMapping("/memos/{id}")
    public Long deleteMemo(@PathVariable Long id) {
        // 해당 메모가 DB에 존재하는지 확인
        Memo memo = findById(id);
        if (memo != null) {
            // memo 삭제
            String sql = "DELETE FROM memo WHERE id = ?";
            jdbcTemplate.update(sql, id);

            return id;
        } else {
            throw new IllegalArgumentException("선택한 메모는 존재하지 않습니다.");
        }
    }

    private Memo findById(Long id) {
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
}

//PRACTICE ORIGINAL
//package com.sparta.memo.controller;
//
//import com.sparta.memo.dto.MemoRequestDto;
//import com.sparta.memo.dto.MemoResponseDto;
//import com.sparta.memo.entity.Memo;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController //JSON 데이터만 줄거야 got it?
//@RequestMapping("/api")
//public class MemoController {
//
//    private final Map<Long, Memo> memoList = new HashMap<>(); //3. HashMap으로 구현체 설정
//
//    @PostMapping("/memos")
//    public MemoResponseDto createMemo(@RequestBody MemoRequestDto requestDto) {
////        return null;
//        //1. RequestDto -> Entity
//        Memo memo = new Memo(requestDto);//requestDto를 Entity로 바꾸는 코드
//
//        //2. Memo Max ID check
//        Long maxId = memoList.size() > 0 ? Collections.max(memoList.keySet()) + 1 : 1;
//        //memoList Map의 size를 구해서 0보다 크'면' Map의 키를 다 대조해 거기에 1(다음값을 위한)을 더하라.
//        //아니면 0 => 메모가 하나도 없다는 거니까 1(첫메모)을 대입
//        //=>Memo의 최대값 구해짐
//        memo.setId(maxId);//4. 구해진 ID값으로 메모의 id초기값에서 set으로 해줌
//
//        //5. DB저장
//        memoList.put(memo.getId(), memo);
//
//        //6. Entity -> ResponseDto => Entitiy를 ResponseDto로 변경 후 반환
//        MemoResponseDto memoResponseDto = new MemoResponseDto(memo);
//
//        return memoResponseDto; //7. 메모 생성하기 API 완성
//    }
//
//    @GetMapping("/memos")
//    public List<MemoResponseDto> getMemos() { //하나, 혹 여러개의 메모를 가져올 거기에 List.
//        // Map To List
//        List<MemoResponseDto> responseList = memoList.values()
//            .stream() //memoList의 value들을 for문같이 돌려줌
//            .map(MemoResponseDto::new).toList(); //하나씩 나오는 메모를 변환 => .map(변환할객체Class::new)
//        //=> 변환할 객체 호출
//        return responseList;
//    }
//
//    @PutMapping("/memos/{id}")
//    public Long updateMemo(@PathVariable Long id, @RequestBody MemoRequestDto requestDto) {
//        //해당 메모가 DB에 존재하는지 확인
//        if (memoList.containsKey(id)) {//containsKey 메모가 지정된 Key를 포함하고 있는지 확인
//            //해당 메모 가져오기
//            Memo memo = memoList.get(id);
//
//            //메모 수정
//            memo.update(requestDto);
//            return memo.getId();
//        } else {
//            throw new IllegalArgumentException("!선택한 메모는 존재하지 않아요!");
//        }
//    }
//
//    @DeleteMapping("/memos/{id}")
//    public Long deleteMemo(@PathVariable Long id) { //ID만 받아오기에 @RequestBdoy 필요x
//        //해당 메모가 DB에 존재하는지 확인
//        if (memoList.containsKey(id)) {
//            //해당 메모 가져오기
//            memoList.remove(id);
//            return id;
//        } else {
//            throw new IllegalArgumentException("!선택한 메모는 존재하지 않아요!");
//
//        }
//    }
//}
