package japbook.jpashop.service;

import jakarta.persistence.EntityManager;
import japbook.jpashop.domain.Address;
import japbook.jpashop.domain.Member;
import japbook.jpashop.domain.Order;
import japbook.jpashop.domain.OrderStatus;
import japbook.jpashop.domain.item.Book;
import japbook.jpashop.domain.item.Item;
import japbook.jpashop.exception.NotEnoughStockException;
import japbook.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    EntityManager em;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;
    @Test
    public void 상품주문() throws Exception{
        //given 이게 주어졌으면
        Member member = getMember();

        Book book = getBook("JPA", 10000, 10);

        //when 이걸 실행하면
        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //then 결과값이 이렇게 나옴
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals(OrderStatus.ORDER, getOrder.getStatus(), "상품 주문시 상태는 ORDER");
        assertEquals(1, getOrder.getOrderItems().size(), "주문한 상품 종류 수가 정확해야한다.");
        assertEquals(10000 * orderCount, getOrder.getTotalPrice(), "주문 가격은 가격 * 수량이다");
        assertEquals(8, book.getStockQuantity(), "주문 수량만큼 재고가 줄어야 한다.");

    }



    @Test
    public void 상품주문_재고수량초과2() throws Exception {
        //given 이게 주어졌으면
        Member member = getMember();
        Item item = getBook("시골 JPA", 10000, 10);

        int orderCount = 15;

        //when 이걸 실행하면
        assertThrows(NotEnoughStockException.class,
                () -> orderService.order(member.getId(), item.getId(), orderCount));

        //then 결과값이 이렇게 나옴
//        fail("재고 수량 부족 예외 발생");

    }

    @Test
    public void 주문취소() throws Exception{
        //given 이게 주어졌으면
        Member member = getMember();
        Book book = getBook("시골 JPA", 10000, 10);

        int orderCount = 2;

      Long id = orderService.order(member.getId(), book.getId(), orderCount);
        //when 이걸 실행하면
        orderService.cancelOrder(id);


        //then 결과값이 이렇게 나옴
        Order order = orderRepository.findOne(id);

        assertEquals(OrderStatus.CANCEL, order.getStatus(), "주문 취소시 상태는 CANCEL이다");
        assertEquals(10, book.getStockQuantity(), "주문이 취소된 상품은 그만큼 재고가 증가해야 한다.");

    }




    private Book getBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member getMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "강가", "123-123"));
        em.persist(member);
        return member;
    }



}