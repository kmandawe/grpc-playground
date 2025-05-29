package com.kensbunker.test.sec10;

import com.kensbunker.common.GrpcServer;
import com.kensbunker.models.sec10.BankServiceGrpc;
import com.kensbunker.models.sec10.ErrorMessage;
import com.kensbunker.models.sec10.ValidationCode;
import com.kensbunker.sec10.BankService;
import com.kensbunker.test.common.AbstractChannelTest;
import io.grpc.Metadata;
import io.grpc.Status;
import io.grpc.protobuf.ProtoUtils;
import java.util.Optional;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

public abstract class AbstractTest extends AbstractChannelTest {

  private static final Metadata.Key<ErrorMessage> ERROR_MESSAGE_KEY = ProtoUtils.keyForProto(ErrorMessage.getDefaultInstance());
  private final GrpcServer grpcServer = GrpcServer.create(new BankService());
  protected BankServiceGrpc.BankServiceStub bankStub;
  protected BankServiceGrpc.BankServiceBlockingStub bankBlockingStub;

  @BeforeAll
  public void setup() {
    this.grpcServer.start();
    this.bankStub = BankServiceGrpc.newStub(channel);
    this.bankBlockingStub = BankServiceGrpc.newBlockingStub(channel);
  }

  @AfterAll
  public void stop() {
    this.grpcServer.stop();
  }

  protected ValidationCode getValidationCode(Throwable throwable) {
    return Optional.ofNullable(Status.trailersFromThrowable(throwable))
        .map(m -> m.get(ERROR_MESSAGE_KEY))
        .map(ErrorMessage::getValidationCode)
        .orElseThrow();
  }
}
