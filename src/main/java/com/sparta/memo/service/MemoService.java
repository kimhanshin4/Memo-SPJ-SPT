package com.sparta.memo.service;

import com.sparta.memo.dto.*;
import com.sparta.memo.entity.*;
import com.sparta.memo.repository.*;
import java.util.*;
import org.springframework.context.*;
import org.springframework.stereotype.*;


//@Component // 넌 이제부터 bean이다
//@RequiredArgsConstructor //final이 달린(상수) 필드를 가지는 '생성자'를 만들어줌 // Lombok으로 DI
@Service //세상 명확한 ANtation
public class MemoService { //memoService 로 bean에 등록

    //    @Autowired //편하고 용이하지만 불안정성 때문에 현업에서도 대부분 생성자 DI를 사용
//    private MemoRepository memoRepository; //만들어진 Repository
    private final MemoRepository memoRepository;

//    public void setDi(MemoRepository memoRepository) { //메서드로 DI
//        this.memoRepository = memoRepository;
//    }

    //    final - 초기화 필요 => 아래 처럼 생성자로 초기화
    public MemoService(MemoRepository memoRepository) { //생성자로 DI
        this.memoRepository = memoRepository; //를 사용!
    }

    //수동으로 DI
//    public MemoService(ApplicationContext context) {
//    1. 'Bean'이름으로 가져오기 //Component한 클래스를 직접 IoC Container에 접근해서 꺼내오기
//        MemoRepository memoRepository = (MemoRepository) context.getBean(
//            "memoRepository");//(클래스를 캐스팅) 후 Bean의 이름으로 가져옴
//        this.memoRepository = memoRepository;
//    }
//    2. 'Bean'클래스 형식으로 가져오기
//        MemoRepository memoRepository = context.getBean(MemoRepository.class);
//        this.memoRepository = memoRepository;
//    }

    public MemoResponseDto createMemo(MemoRequestDto requestDto) {
        Memo memo = new Memo(requestDto);
        Memo saveMemo = memoRepository.save(memo);
        MemoResponseDto memoResponseDto = new MemoResponseDto(memo);

        return memoResponseDto;
    }

    public List<MemoResponseDto> getMemos() {
        return memoRepository.findAll();
    }

    public Long updateMemo(Long id, MemoRequestDto requestDto) {
        Memo memo = memoRepository.findById(id);
        if (memo != null) {
            // memo 내용 수정
            memoRepository.update(id, requestDto);

            return id;
        } else {
            throw new IllegalArgumentException("선택한 메모는 존재하지 않습니다.");
        }
    }


    public Long deleteMemo(Long id) {
        Memo memo = memoRepository.findById(id);
        if (memo != null) {
            memoRepository.delete(id);

            return id;
        } else {
            throw new IllegalArgumentException("선택한 메모는 존재하지 않습니다.");
        }
    }
}
