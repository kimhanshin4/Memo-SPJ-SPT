package com.sparta.memo.service;

import com.sparta.memo.dto.*;
import com.sparta.memo.entity.*;
import com.sparta.memo.repository.*;
import java.util.*;
import org.springframework.stereotype.*;
import org.springframework.transaction.annotation.*;


@Service
public class MemoService {

    private final MemoRepository memoRepository;

    public MemoService(MemoRepository memoRepository) {
        this.memoRepository = memoRepository;
    }

    public MemoResponseDto createMemo(MemoRequestDto requestDto) {
        Memo memo = new Memo(requestDto);
        Memo saveMemo = memoRepository.save(memo);
        MemoResponseDto memoResponseDto = new MemoResponseDto(memo);

        return memoResponseDto;
    }

    public List<MemoResponseDto> getMemos() {
        //DB 조회
        return memoRepository.findAllByOrderByModifiedAtDesc().stream().map(MemoResponseDto::new)
            .toList();
//        return memoRepository.findAll();
    }

    @Transactional
    public Long updateMemo(Long id, MemoRequestDto requestDto) {
        //해당 메모가 DB에 존재하는지 확인
        Memo memo = findMemo(id);
        memo.update(requestDto);

        return id;

    }


    public Long deleteMemo(Long id) {
        //해당 메모가 DB에 존재하는지 확인
        Memo memo = findMemo(id);

        //memo 삭제
        memoRepository.delete(memo);
        return id;

    }

    //중복 코드 메서드화
    private Memo findMemo(Long id) {
        return memoRepository.findById(id).orElseThrow(() ->
            new IllegalArgumentException("선택한 메모는 존재하지 않는다.")
        );
    }
}
