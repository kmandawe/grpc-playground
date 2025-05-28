package com.kensbunker.sec06.requesthandlers;

import com.kensbunker.models.sec06.AccountBalance;
import com.kensbunker.models.sec06.DepositRequest;
import com.kensbunker.sec06.repository.AccountRepository;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DepositRequestHandler implements StreamObserver<DepositRequest> {

  private static final Logger LOG = LoggerFactory.getLogger(DepositRequestHandler.class);

  private final StreamObserver<AccountBalance> responseObserver;
  private int accountNumber;

  public DepositRequestHandler(StreamObserver<AccountBalance> responseObserver) {
    this.responseObserver = responseObserver;
  }

  @Override
  public void onNext(DepositRequest depositRequest) {
    switch (depositRequest.getRequestCase()) {
      case ACCOUNT_NUMBER -> this.accountNumber = depositRequest.getAccountNumber();
      case MONEY ->
          AccountRepository.addAmount(this.accountNumber, depositRequest.getMoney().getAmount());
    }
  }

  @Override
  public void onError(Throwable throwable) {
    LOG.info("client error: {}", throwable.getMessage());
  }

  @Override
  public void onCompleted() {
    var accountBalance =
        AccountBalance.newBuilder()
            .setAccountNumber(this.accountNumber)
            .setBalance(AccountRepository.getBalance(this.accountNumber))
            .build();
    this.responseObserver.onNext(accountBalance);
    this.responseObserver.onCompleted();
  }
}
