package com.kensbunker.sec12;

import com.kensbunker.models.sec12.AccountBalance;
import com.kensbunker.models.sec12.BalanceCheckRequest;
import com.kensbunker.models.sec12.BankServiceGrpc;
import com.kensbunker.sec11.repository.AccountRepository;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserRoleBankService extends BankServiceGrpc.BankServiceImplBase {

  private static final Logger LOG = LoggerFactory.getLogger(UserRoleBankService.class);

  @Override
  public void getAccountBalance(
      BalanceCheckRequest request, StreamObserver<AccountBalance> responseObserver) {
    var accountNumber = request.getAccountNumber();
    var balance = AccountRepository.getBalance(accountNumber);
    if (UserRole.STANDARD.equals(Constants.USER_ROLE_KEY.get())) {
      var fee = balance > 0 ? 1 : 0;
      AccountRepository.decuctAmount(accountNumber, fee);
      balance = balance - fee;
    }
    var accountBalance =
        AccountBalance.newBuilder().setAccountNumber(accountNumber).setBalance(balance).build();

    responseObserver.onNext(accountBalance);
    responseObserver.onCompleted();
  }
}
