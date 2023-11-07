package com.sparta.memo.service;

import com.sparta.memo.dto.*;
import com.sparta.memo.entity.*;
import com.sparta.memo.repository.*;
import java.util.*;
import org.springframework.jdbc.core.*;

//Service - 변환, 전달, 확인하는 등의 로직들이 이루어짐
public class MemoService {

    //    private final JdbcTemplate jdbcTemplate; //"JDBC 탬플릿 사용!" //JDBC 탬플릿 객체는 Spring에서 따로 관리
    private final MemoRepository memoRepository;

    public MemoService(JdbcTemplate jdbcTemplate) { //final 이기에 생성자 생성 후 넣을 탬플릿 매개인자로 삽입
//        this.jdbcTemplate = jdbcTemplate;
        this.memoRepository = new MemoRepository(jdbcTemplate);
    }

    public MemoResponseDto createMemo(MemoRequestDto requestDto) {

        // RequestDto -> Entity
        Memo memo = new Memo(requestDto);

        // DB 저장
//        MemoRepository memoRepository = new MemoRepository(jdbcTemplate); //중복제거
        Memo saveMemo = memoRepository.save(memo); //DB와 연동하는 것 이기에 'save'
        //MemoRepository에 save method가 만들어지고, 파라미터로는 저장할 memo객체 전달
        //반환 => Memo

        // Entity -> ResponseDto
        MemoResponseDto memoResponseDto = new MemoResponseDto(memo);

        return memoResponseDto;
    }

    public List<MemoResponseDto> getMemos() {
        // DB 조회
//        MemoRepository memoRepository = new MemoRepository(jdbcTemplate); //중복제거
        return memoRepository.findAll();
    }

    public Long updateMemo(Long id, MemoRequestDto requestDto) {
//        MemoRepository memoRepository = new MemoRepository(jdbcTemplate); //중복제거
        // 해당 메모가 DB에 존재하는지 확인
        Memo memo = memoRepository.findById(id); //findById => Repository로 이동시켰음
        if (memo != null) {
            // memo 내용 수정
            memoRepository.update(id, requestDto);

            return id;
        } else {
            throw new IllegalArgumentException("선택한 메모는 존재하지 않습니다.");
        }
    }


    public Long deleteMemo(Long id) {
//        MemoRepository memoRepository = new MemoRepository(jdbcTemplate); //중복제거
        // 해당 메모가 DB에 존재하는지 확인
        Memo memo = memoRepository.findById(id); //findById => Repository로 이동시켰음
        if (memo != null) {
            // memo 삭제
            memoRepository.delete(id);

            return id;
        } else {
            throw new IllegalArgumentException("선택한 메모는 존재하지 않습니다.");
        }
    }
}
