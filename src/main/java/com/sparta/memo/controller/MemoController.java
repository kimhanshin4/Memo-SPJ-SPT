package com.sparta.memo.controller;

import com.sparta.memo.dto.*;
import com.sparta.memo.service.*;
import java.util.*;
import org.springframework.jdbc.core.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class MemoController {


    private final JdbcTemplate jdbcTemplate; //"JDBC 탬플릿 사용!" //JDBC 탬플릿 객체는 Spring에서 따로 관리

    public MemoController(JdbcTemplate jdbcTemplate) { //final 이기에 생성자 생성 후 넣을 탬플릿 매개인자로 삽입
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostMapping("/memos")
    public MemoResponseDto createMemo(@RequestBody MemoRequestDto requestDto) {
        //객체간 이동(다른 Class의 method 호출)시 그 Class의 객체 필요 (Instance화)
        //MemoService 부터 instance화
        MemoService memoService = new MemoService(jdbcTemplate);
        //Controller의 method의 이름과 Service의 method의 이름을 일치시키면 협업시 이해 용이
        return memoService.createMemo(
            requestDto);
        //'Controller createMemo에서 전달받아 Business logic을 수행하는 method 구나' 라고 알 수 있음
        //Service에서 완료된 결과를 바로 Controller에 리턴 후 Client에 보낸다

        //"createMemo method가 호출 완료"

    }

    @GetMapping("/memos")
    public List<MemoResponseDto> getMemos() {
        MemoService memoService = new MemoService(jdbcTemplate);
        return memoService.getMemos();
    }

    @PutMapping("/memos/{id}")
    public Long updateMemo(@PathVariable Long id, @RequestBody MemoRequestDto requestDto) {
        MemoService memoService = new MemoService(jdbcTemplate);
        return memoService.updateMemo(id, requestDto);
    }

    @DeleteMapping("/memos/{id}")
    public Long deleteMemo(@PathVariable Long id) {
        MemoService memoService = new MemoService(jdbcTemplate);
        return memoService.deleteMemo(id);
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
