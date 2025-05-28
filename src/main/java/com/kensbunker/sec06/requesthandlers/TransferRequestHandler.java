package com.kensbunker.sec06.requesthandlers;

import com.kensbunker.models.sec06.AccountBalance;
import com.kensbunker.models.sec06.TransferRequest;
import com.kensbunker.models.sec06.TransferResponse;
import com.kensbunker.models.sec06.TransferStatus;
import com.kensbunker.sec06.repository.AccountRepository;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransferRequestHandler implements StreamObserver<TransferRequest> {

  private static final Logger LOG = LoggerFactory.getLogger(TransferRequestHandler.class);
  private final StreamObserver<TransferResponse> responseObserver;

  public TransferRequestHandler(StreamObserver<TransferResponse> responseObserver) {
    this.responseObserver = responseObserver;
  }

  @Override
  public void onNext(TransferRequest transferRequest) {
    var transferStatus = transfer(transferRequest);
    var response =
        TransferResponse.newBuilder()
            .setStatus(transferStatus)
            .setFromAccount(toAccountBalance(transferRequest.getFromAccount()))
            .setToAccount(toAccountBalance(transferRequest.getToAccount()))
            .build();
    LOG.info("response: {}", response);
    this.responseObserver.onNext(response);
  }

  @Override
  public void onError(Throwable throwable) {
    LOG.info("client error {}", throwable.getMessage());
  }

  @Override
  public void onCompleted() {
    LOG.info("transfer request stream completed");
    this.responseObserver.onCompleted();
  }

  private TransferStatus transfer(TransferRequest request) {
    var amount = request.getAmount();
    var fromAccount = request.getFromAccount();
    var toAccount = request.getToAccount();
    var status = TransferStatus.REJECTED;
    if (AccountRepository.getBalance(fromAccount) >= amount && (fromAccount != toAccount)) {
      AccountRepository.decuctAmount(fromAccount, amount);
      AccountRepository.addAmount(toAccount, amount);
      status = TransferStatus.COMPLETED;
    }
    return status;
  }

  private AccountBalance toAccountBalance(int accountNumber) {
    return AccountBalance.newBuilder()
        .setAccountNumber(accountNumber)
        .setBalance(AccountRepository.getBalance(accountNumber))
        .build();
  }
}
