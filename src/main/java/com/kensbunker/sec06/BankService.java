package com.kensbunker.sec06;

import com.google.common.util.concurrent.Uninterruptibles;
import com.google.protobuf.Empty;
import com.kensbunker.models.sec06.AccountBalance;
import com.kensbunker.models.sec06.AllAccountsResponse;
import com.kensbunker.models.sec06.BalanceCheckRequest;
import com.kensbunker.models.sec06.BankServiceGrpc;
import com.kensbunker.models.sec06.DepositRequest;
import com.kensbunker.models.sec06.Money;
import com.kensbunker.models.sec06.WithdrawRequest;
import com.kensbunker.sec06.repository.AccountRepository;
import com.kensbunker.sec06.requesthandlers.DepositRequestHandler;
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

    responseObserver.onNext(accountBalance);
    responseObserver.onCompleted();
  }

  @Override
  public void getAllAccounts(Empty request, StreamObserver<AllAccountsResponse> responseObserver) {
    var accounts =
        AccountRepository.getAllAccounts().entrySet().stream()
            .map(
                e ->
                    AccountBalance.newBuilder()
                        .setAccountNumber(e.getKey())
                        .setBalance(e.getValue())
                        .build())
            .toList();

    var response = AllAccountsResponse.newBuilder().addAllAccounts(accounts).build();
    responseObserver.onNext(response);
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
      responseObserver.onCompleted(); // change to proper error later
      return;
    }

    for (int i = 0; i < requestedAmount / 10; i++) {
      var money = Money.newBuilder().setAmount(10).build();
      responseObserver.onNext(money);
      LOG.info("money sent {}", money);
      AccountRepository.decuctAmount(accountNumber, 10);
      Uninterruptibles.sleepUninterruptibly(1, TimeUnit.SECONDS);
    }
    responseObserver.onCompleted();
  }

  @Override
  public StreamObserver<DepositRequest> deposit(StreamObserver<AccountBalance> responseObserver) {
    return new DepositRequestHandler(responseObserver);
  }
}
