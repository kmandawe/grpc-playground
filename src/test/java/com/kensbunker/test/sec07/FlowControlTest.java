package com.kensbunker.test.sec07;

import com.kensbunker.common.GrpcServer;
import com.kensbunker.models.sec07.FlowControlServiceGrpc;
import com.kensbunker.sec07.FlowControlService;
import com.kensbunker.test.common.AbstractChannelTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FlowControlTest extends AbstractChannelTest {

  private  final GrpcServer server = GrpcServer.create(new FlowControlService());
  private FlowControlServiceGrpc.FlowControlServiceStub stub;

  @BeforeAll
  public void setup() {
    this.server.start();
    this.stub = FlowControlServiceGrpc.newStub(channel);
  }

  @AfterAll
  public void stop() {
    this.server.stop();
  }

  @Test
  public void testFlowControlDemo() {
    var responseObserver = new ResponseHandler();
    var requestObserver = this.stub.getMessages(responseObserver);
    responseObserver.setRequestObserver(requestObserver);

    responseObserver.start();
    responseObserver.await();
  }

}
