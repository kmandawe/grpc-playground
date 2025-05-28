package com.kensbunker.test.sec06;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.kensbunker.models.sec06.TransferRequest;
import com.kensbunker.models.sec06.TransferResponse;
import com.kensbunker.models.sec06.TransferStatus;
import com.kensbunker.test.common.ResponseObserver;
import java.util.List;
import org.junit.jupiter.api.Test;

public class Lec05BiDirectionalStreamingTest extends AbstractTest{

  @Test
  public void transferTest() {
    var responseObserver = ResponseObserver.<TransferResponse>create();
    var requestObserver = this.transferStub.transfer(responseObserver);
    var requests = List.of(
        TransferRequest.newBuilder().setFromAccount(6).setToAccount(6).setAmount(10).build(),
        TransferRequest.newBuilder().setFromAccount(6).setToAccount(7).setAmount(110).build(),
        TransferRequest.newBuilder().setFromAccount(6).setToAccount(7).setAmount(10).build(),
        TransferRequest.newBuilder().setFromAccount(7).setToAccount(6).setAmount(10).build()
    );
    requests.forEach(requestObserver::onNext);
    requestObserver.onCompleted();
    responseObserver.await();

    assertEquals(4, responseObserver.getItems().size());
    validate(responseObserver.getItems().get(0), TransferStatus.REJECTED, 100, 100);
    validate(responseObserver.getItems().get(1), TransferStatus.REJECTED, 100, 100);
    validate(responseObserver.getItems().get(2), TransferStatus.COMPLETED, 90, 110);
    validate(responseObserver.getItems().get(3), TransferStatus.COMPLETED, 100, 100);
  }

  private void validate(TransferResponse response, TransferStatus status, int fromAccountBalance, int toAccountBalance) {
    assertEquals(status, response.getStatus());
    assertEquals(fromAccountBalance, response.getFromAccount().getBalance());
    assertEquals(toAccountBalance, response.getToAccount().getBalance());
  }
}
