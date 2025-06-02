package com.kensbunker.sec12;

import com.google.common.util.concurrent.Uninterruptibles;
import com.kensbunker.models.sec12.AccountBalance;
import com.kensbunker.models.sec12.BalanceCheckRequest;
import com.kensbunker.models.sec12.BankServiceGrpc;
import com.kensbunker.models.sec12.Money;
import com.kensbunker.models.sec12.WithdrawRequest;
import com.kensbunker.sec11.repository.AccountRepository;
import io.grpc.Context;
import io.grpc.Status;
import io.grpc.stub.ServerCallStreamObserver;
import io.grpc.stub.StreamObserver;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BankService extends BankServiceGrpc.BankServiceImplBase {

  private static final Logger LOG = LoggerFactory.getLogger(BankService.class);

  @Override
  public void getAccountBalance(
      BalanceCheckRequest request, StreamObserver<AccountBalance> responseObserver) {
    var accountNumber = request.getAccountNumber();
    var balance = AccountRepository.getBalance(accountNumber);
    var accountBalance =
        AccountBalance.newBuilder().setAccountNumber(accountNumber).setBalance(balance).build();

//    Uninterruptibles.sleepUninterruptibly(3, TimeUnit.SECONDS);
//
//    ((ServerCallStreamObserver<AccountBalance>) responseObserver).setCompression("gzip");
    responseObserver.onNext(accountBalance);
    responseObserver.onCompleted();
  }

  @Override
  public void withdraw(WithdrawRequest request, StreamObserver<Money> responseObserver) {
    /*
       Assumption: account # 1 - 10 & withdraw amount is multiple of $10
    */
    var accountNumber = request.getAccountNumber();
    var requestedAmount = request.getAmount();
    var accountBalance = AccountRepository.getBalance(accountNumber);

    if (requestedAmount > accountBalance) {
      responseObserver.onError(Status.FAILED_PRECONDITION.asRuntimeException());
      return;
    }

    for (int i = 0; i < (requestedAmount / 10) && !Context.current().isCancelled(); i++) {
      var money = Money.newBuilder().setAmount(10).build();
      responseObserver.onNext(money);
      LOG.info("money sent {}", money);
      AccountRepository.decuctAmount(accountNumber, 10);
      Uninterruptibles.sleepUninterruptibly(1, TimeUnit.SECONDS);
    }
    LOG.info("streaming completed");
    responseObserver.onCompleted();
  }
}
