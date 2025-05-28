package com.kensbunker.sec06;

import com.kensbunker.models.sec06.TransferRequest;
import com.kensbunker.models.sec06.TransferResponse;
import com.kensbunker.models.sec06.TransferServiceGrpc.TransferServiceImplBase;
import io.grpc.stub.StreamObserver;

public class TransferService extends TransferServiceImplBase {

  @Override
  public StreamObserver<TransferRequest> transfer(
      StreamObserver<TransferResponse> responseObserver) {

  }
}
