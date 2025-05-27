package com.kensbunker.sec06;

import com.kensbunker.models.sec06.AccountBalance;
import com.kensbunker.models.sec06.BalanceCheckRequest;
import com.kensbunker.models.sec06.BankServiceGrpc;
import io.grpc.stub.StreamObserver;

public class BankService extends BankServiceGrpc.BankServiceImplBase {

  @Override
  public void getAccountBalance(BalanceCheckRequest request,
      StreamObserver<AccountBalance> responseObserver) {


  }
}
