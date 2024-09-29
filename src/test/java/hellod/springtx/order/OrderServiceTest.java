package hellod.springtx.order;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class OrderServiceTest {

    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;

    @Test
    @DisplayName("정상 처리")
    void complete() throws NotEnoughMoneyException {

        //given
        Order order = new Order();
        order.setUsername("정상");

        //when
        orderService.order(order);

        //then
        Order findOrder = orderRepository.findById(order.getId()).get();
        assertThat(findOrder.getPayStauts()).isEqualTo("완료");
    }

    @Test
    @DisplayName("예외")
    void runtimeException(){

        //given
        Order order = new Order();
        order.setUsername("예외");

        //when
        assertThatThrownBy(()->orderService.order(order))
                .isInstanceOf(RuntimeException.class);

        //then
        Optional<Order> findOrder = orderRepository.findById(order.getId());
        assertThat(findOrder.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("잔고 부족")
    void bizException(){

        //given
        Order order = new Order();
        order.setUsername("잔고 부족");

        //when
        assertThatThrownBy(()-> orderService.order(order))
                .isInstanceOf(NotEnoughMoneyException.class);

        //then
        Order findOrder = orderRepository.findById(order.getId()).get();
        assertThat(findOrder.getPayStauts()).isEqualTo("대기");
    }
}