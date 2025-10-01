package japbook.jpashop.service;

import jakarta.annotation.security.RunAs;
import japbook.jpashop.domain.Member;
import japbook.jpashop.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest //스프링 컨테이너에서 실행하겠다 없으면 Autowired가 실행 안됨.
@Transactional  //트랜젝션을 걸고 테스트가 끝날시 롤백.
class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

    @Test
    public void 회원가입() throws Exception{
        //given 이게 주어졌으면
        Member member = new Member();
        member.setName("kim");

        //when 이걸 실행하면
        Long savedId = memberService.join(member);

        //then 결과값이 이렇게 나옴
        assertEquals(member, memberRepository.findOne(savedId));
    }

    @Test
    public void 중복_회원_예외 () throws Exception{
        //given
        Member member1 = new Member();
        member1.setName("kim");

        Member member2 = new Member();
        member2.setName("kim");

        //when
        memberService.join(member1);
        try{
            memberService.join(member2);    //예외가 발생해야함
        } catch (IllegalStateException e) {
            return;
        }


        //then
        fail("예외가 발생 해야함");
    }

}