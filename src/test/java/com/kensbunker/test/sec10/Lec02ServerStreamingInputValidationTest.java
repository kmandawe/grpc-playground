package com.kensbunker.test.sec10;

import com.kensbunker.models.sec10.Money;
import com.kensbunker.models.sec10.ValidationCode;
import com.kensbunker.models.sec10.WithdrawRequest;
import com.kensbunker.test.common.ResponseObserver;
import io.grpc.StatusRuntimeException;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class Lec02ServerStreamingInputValidationTest extends AbstractTest {

  @ParameterizedTest
  @MethodSource("testData")
  public void blockingInputValidationTest(WithdrawRequest request, ValidationCode expectedCode) {
    var ex =
        Assertions.assertThrows(
            StatusRuntimeException.class,
            () -> {
              var response = this.bankBlockingStub.withdraw(request).hasNext();
            });
    Assertions.assertEquals(expectedCode, getValidationCode(ex));
  }

  @ParameterizedTest
  @MethodSource("testData")
  public void asyncInputValidationTest(WithdrawRequest request, ValidationCode expectedCode) {
    var observer = ResponseObserver.<Money>create();
    this.bankStub.withdraw(request, observer);
    observer.await();

    Assertions.assertTrue(observer.getItems().isEmpty());
    Assertions.assertNotNull(observer.getThrowable());
    Assertions.assertEquals(expectedCode, getValidationCode(observer.getThrowable()));
  }

  private Stream<Arguments> testData() {
    return Stream.of(
        Arguments.of(
            WithdrawRequest.newBuilder().setAccountNumber(11).setAmount(10).build(),
            ValidationCode.INVALID_ACCOUNT),
        Arguments.of(
            WithdrawRequest.newBuilder().setAccountNumber(1).setAmount(17).build(),
            ValidationCode.INVALID_AMOUNT),
        Arguments.of(
            WithdrawRequest.newBuilder().setAccountNumber(1).setAmount(120).build(),
            ValidationCode.INSUFFICIENT_BALANCE));
  }
}
