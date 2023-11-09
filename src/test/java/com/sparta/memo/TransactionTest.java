package com.sparta.memo;

import com.sparta.memo.entity.*;
import com.sparta.memo.repository.*;
import jakarta.persistence.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.test.annotation.*;
import org.springframework.transaction.annotation.*;

@SpringBootTest
public class TransactionTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    MemoRepository memoRepository;

    @Test
    @Transactional
    @Rollback(value = false) // 테스트 코드에서 @Transactional 를 사용하면 테스트가 완료된 후 롤백하기 때문에 false 옵션 추가
    @DisplayName("메모 생성 성공")
    void test1() {
        Memo memo = new Memo();
        memo.setUsername("Robbert");
        memo.setContents("@Transactional 테스트 중!");

        em.persist(memo);  // 영속성 컨텍스트에 메모 Entity 객체를 저장합니다.
    }

    @Test
    @Disabled
    @DisplayName("메모 생성 실패")
    void test2() {
        Memo memo = new Memo();
        memo.setUsername("Robbie");
        memo.setContents("@Transactional 테스트 중!");

        em.persist(memo);  // 영속성 컨텍스트에 메모 Entity 객체를 저장합니다.
    }

    @Test
    @Disabled
//    @Transactional
//    @Rollback(value = false)
    @DisplayName("트랜잭션 전파 테스트")
    void test3() {
//        memoRepository.createMemo(em);
        System.out.println("테스트 test3 메서드 종료");
    }
}

//이전 테스트
//@SpringBootTest//무엇인가?
//public class TransactionTest {
//
//    @PersistenceContext //Entity Manager 받아오기
//    EntityManager em;
//
//    @Test
//    @Transactional //springframwork.transaction.annotation
//    @Rollback(value = false) // 테스트 코드에서 @Transactional 를 사용하면 테스트가 완료된 후 롤백하기 때문에 false 옵션 추가
//    @DisplayName("메모 생성 성공")
//    void test1() {
//        Memo memo = new Memo();
//        memo.setUsername("Robbert");
//        memo.setContents("@Transactional 테스트 중!");
//
//        em.persist(memo);  // 영속성 컨텍스트에 메모 Entity 객체를 저장합니다.
//    }
//
//    @Test
//    @Disabled // - 해당 메서드는 더이상 테스트 하지 않음
//    @DisplayName("메모 생성 실패")
//    void test2() {
//        Memo memo = new Memo();
//        memo.setUsername("Robbie");
//        memo.setContents("@Transactional 테스트 중!");
//
//        em.persist(memo);  // 영속성 컨텍스트에 메모 Entity 객체를 저장합니다.
//    }
//}
