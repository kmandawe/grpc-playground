package com.kensbunker.sec07;

import com.kensbunker.models.sec07.FlowControlServiceGrpc.FlowControlServiceImplBase;
import com.kensbunker.models.sec07.Output;
import com.kensbunker.models.sec07.RequestSize;
import io.grpc.stub.StreamObserver;
import java.util.stream.IntStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlowControlService extends FlowControlServiceImplBase {
  private static final Logger LOG = LoggerFactory.getLogger(FlowControlService.class);

  @Override
  public StreamObserver<RequestSize> getMessages(StreamObserver<Output> responseObserver) {
    return new RequestHandler(responseObserver);
  }

  private static class RequestHandler implements StreamObserver<RequestSize> {

    private final StreamObserver<Output> responseObserver;
    private Integer emitted;

    private RequestHandler(StreamObserver<Output> responseObserver) {
      this.responseObserver = responseObserver;
      this.emitted = 0;
    }

    @Override
    public void onNext(RequestSize requestSize) {
      IntStream.rangeClosed((emitted + 1), 100)
          .limit(requestSize.getSize())
          .forEach(
              i -> {
                LOG.info("emitting: {}", i);
                responseObserver.onNext(Output.newBuilder().setValue(i).build());
              });
      emitted = emitted + requestSize.getSize();
      if (emitted >= 100) {
        responseObserver.onCompleted();
      }
    }

    @Override
    public void onError(Throwable throwable) {}

    @Override
    public void onCompleted() {
      this.responseObserver.onCompleted();
    }
  }
}
