package hellod.springtx.apply;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.PostConstruct;

@Slf4j
@SpringBootTest
public class InitTxTest {

    @Autowired
    Hello hello;
    @Test
    void go(){
        // 초기화 코드는 스프링이 초기화 시점에 알아서 호출한다.
    }

    @TestConfiguration
    static class HelloConfig{
        @Bean
        Hello hello(){
            return new Hello();
        }
    }

    @Slf4j
    static class Hello{
        @PostConstruct
        @Transactional
        public void initV1(){
            boolean actualTransactionActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("Hello init @PostConstruct tx active = {}",actualTransactionActive);
        }

        @EventListener(value= ApplicationReadyEvent.class)
        @Transactional
        public void initV2(){
            boolean actualTransactionActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("Hello init @EventListener tx active = {}",actualTransactionActive);
        }
    }
}
