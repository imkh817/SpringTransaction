package hellod.springtx.apply;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
public class TxApplyBasicTest {

    @Autowired BasicService basicService;

    @Test
    void proxyCheck(){
        // Aop class = class hellod.springtx.TxApplyBasicTest$BasicService$$EnhancerBySpringCGLIB$$f718850f
        // BasicService의 하나의 메소드라도 @Transaction이 선언되어있으면 Spring에서 해당 객체의 프록시를 Bean으로 등록한다.
        log.info("Aop class = {}", basicService.getClass());
        assertThat(AopUtils.isAopProxy(basicService)).isTrue();
    }

    @Test
    void callTx(){
        basicService.tx();
        basicService.nonTx();
    }


    @TestConfiguration
    static class TxApplyBasicTestConfig{
        @Bean
        public BasicService basicService(){
            return new BasicService();
        }
    }


    @Slf4j
    static class BasicService{
        @Transactional
        public void tx(){
            log.info("call tx");
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("tx actice = {}",txActive);
        }

        public void nonTx(){
            log.info("call nonTx");
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("tx active = {}",txActive);
        }
    }
}
