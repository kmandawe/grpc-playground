package com.kensbunker.test.sec09;

import com.kensbunker.models.sec09.AccountBalance;
import com.kensbunker.models.sec09.BalanceCheckRequest;
import com.kensbunker.models.sec09.Money;
import com.kensbunker.models.sec09.WithdrawRequest;
import com.kensbunker.test.common.ResponseObserver;
import io.grpc.Status;
import io.grpc.Status.Code;
import io.grpc.StatusRuntimeException;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class Lec02ServerStreamingInputValidationTest extends AbstractTest {

  @ParameterizedTest
  @MethodSource("testData")
  public void blockingInputValidationTest(WithdrawRequest request, Code expectedCode) {
    var ex =
        Assertions.assertThrows(
            StatusRuntimeException.class,
            () -> {
              var response = this.bankBlockingStub.withdraw(request).hasNext();
            });
    Assertions.assertEquals(expectedCode, ex.getStatus().getCode());
  }

  @ParameterizedTest
  @MethodSource("testData")
  public void asyncInputValidationTest(WithdrawRequest request, Code expectedCode){
    var observer = ResponseObserver.<Money>create();
    this.bankStub.withdraw(request, observer);
    observer.await();

    Assertions.assertTrue(observer.getItems().isEmpty());
    Assertions.assertNotNull(observer.getThrowable());
    Assertions.assertEquals(expectedCode, ((StatusRuntimeException) observer.getThrowable()).getStatus().getCode());
  }

  private Stream<Arguments> testData() {
    return Stream.of(
        Arguments.of(
            WithdrawRequest.newBuilder().setAccountNumber(11).setAmount(10).build(),
            Code.INVALID_ARGUMENT),
        Arguments.of(
            WithdrawRequest.newBuilder().setAccountNumber(1).setAmount(17).build(),
            Code.INVALID_ARGUMENT),
        Arguments.of(
            WithdrawRequest.newBuilder().setAccountNumber(1).setAmount(120).build(),
            Code.FAILED_PRECONDITION));
  }
}
