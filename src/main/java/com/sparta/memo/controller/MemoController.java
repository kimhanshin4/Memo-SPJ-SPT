package com.sparta.memo.controller;

import com.sparta.memo.dto.*;
import com.sparta.memo.service.*;
import java.util.*;
import org.springframework.web.bind.annotation.*;

//내부에서 DispatcherServlet이 Handler Mapping을 통해 어떤 Controller인지 찾는다
//그 Controller에 method 호출
//사실 SpringCotainer에 Bean으로 등록되어 관리 되던 중!
@RestController
@RequestMapping("/api")
public class MemoController {

    private final MemoService memoService; //만들어진 Service

    public MemoController(MemoService memoService) { // ?? 저런 Bean객체는 처음인데 어케 넣으라는거니...
        this.memoService = memoService;//를 사용!
    }

    @PostMapping("/memos")
    public MemoResponseDto createMemo(@RequestBody MemoRequestDto requestDto) {
        return memoService.createMemo(
            requestDto);
    }

    @GetMapping("/memos")
    public List<MemoResponseDto> getMemos() {
        return memoService.getMemos();
    }

    @PutMapping("/memos/{id}")
    public Long updateMemo(@PathVariable Long id, @RequestBody MemoRequestDto requestDto) {
        return memoService.updateMemo(id, requestDto);
    }

    @DeleteMapping("/memos/{id}")
    public Long deleteMemo(@PathVariable Long id) {
        return memoService.deleteMemo(id);
    }
}
