package com.kensbunker.test.sec06;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.google.common.util.concurrent.Uninterruptibles;
import com.kensbunker.models.sec06.AccountBalance;
import com.kensbunker.models.sec06.DepositRequest;
import com.kensbunker.models.sec06.Money;
import com.kensbunker.test.common.ResponseObserver;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;

public class Lec06ClientStreamingTest extends AbstractTest {

  @Test
  public void depositTest() {

    var responseObserver = ResponseObserver.<AccountBalance>create();
    var requestObserver = this.asyncStub.deposit(responseObserver);

    // initial message - account number
    requestObserver.onNext(DepositRequest.newBuilder().setAccountNumber(5).build());
    Uninterruptibles.sleepUninterruptibly(1, TimeUnit.SECONDS);
    requestObserver.onError(new RuntimeException());

//    IntStream.rangeClosed(1, 10)
//        .mapToObj(i -> Money.newBuilder().setAmount(10).build())
//        .map(m -> DepositRequest.newBuilder().setMoney(m).build())
//        .forEach(requestObserver::onNext);
//    // notifying the server that we are done
//    requestObserver.onCompleted();

    // at this point our response observer should receive a response
    responseObserver.await();
    assertEquals(1, responseObserver.getItems().size());
    assertEquals(200, responseObserver.getItems().getFirst().getBalance());
    assertNull(responseObserver.getThrowable());
  }
}
