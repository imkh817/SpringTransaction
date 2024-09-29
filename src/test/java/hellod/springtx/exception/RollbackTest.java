package hellod.springtx.exception;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class RollbackTest {

    @Autowired RollbackService rollbackService;

    @Test
    @DisplayName("UnCheckedException")
    public void runtimeException(){
        assertThatThrownBy(()->rollbackService.runtimeException())
                    .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("CheckedException")
    public void checkedException() throws MyException {
        assertThatThrownBy(()->rollbackService.checkedException())
                .isInstanceOf(MyException.class);
    }

    @Test
    @DisplayName("RollbackFor")
    public void rollbackFor() throws MyException {
        assertThatThrownBy(()->rollbackService.rollbackFor())
                .isInstanceOf(MyException.class);
    }

    @TestConfiguration
    static class RollbackTestConfig{
        @Bean
        RollbackService rollbackService(){
            return new RollbackService();
        }
    }


    static class MyException extends Exception{
        public MyException(String message) {
            super(message);
        }
    }

    static class RollbackService{

        @Transactional
        public void runtimeException(){
            throw new RuntimeException();
        }

        @Transactional
        public void checkedException() throws MyException {
            throw new MyException("Checked Exception");
        }

        @Transactional(rollbackFor = MyException.class)
        public void rollbackFor() throws MyException {
            throw new MyException("Checked Exception");
        }
    }
}
