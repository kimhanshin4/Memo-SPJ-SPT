package com.sparta.memo.dto;

import com.sparta.memo.entity.Memo;
import java.time.*;
import lombok.Getter;

@Getter
public class MemoResponseDto {

    private Long id;
    private String username;
    private String contents;
    private LocalDateTime creatAt;
    private LocalDateTime modifiedAt;

    public MemoResponseDto(Memo memo) {
        this.id = memo.getId();
        this.username = memo.getUsername();
        this.contents = memo.getContents();
        this.creatAt = memo.getCreatedAt();
        this.modifiedAt = memo.getModifiedAt();
    }
}