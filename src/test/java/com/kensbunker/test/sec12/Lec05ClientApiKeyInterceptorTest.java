package com.kensbunker.test.sec12;

import com.kensbunker.common.GrpcServer;
import com.kensbunker.models.sec12.BalanceCheckRequest;
import com.kensbunker.sec12.BankService;
import com.kensbunker.sec12.Constants;
import com.kensbunker.sec12.interceptors.ApiKeyValidationInterceptor;
import io.grpc.ClientInterceptor;
import io.grpc.Metadata;
import io.grpc.stub.MetadataUtils;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Lec05ClientApiKeyInterceptorTest extends AbstractInterceptorTest {
  private static final Logger LOG = LoggerFactory.getLogger(Lec05ClientApiKeyInterceptorTest.class);

  @Override
  protected List<ClientInterceptor> getClientInterceptors() {
    return List.of(MetadataUtils.newAttachHeadersInterceptor(getApiKey()));
  }

  @Override
  protected GrpcServer createServer() {
    return GrpcServer.create(
        6565,
        builder -> {
          builder.addService(new BankService())
              .intercept(new ApiKeyValidationInterceptor());
        });
  }

  @Test
  public void clientApiKeyDemo() {
    var request = BalanceCheckRequest.newBuilder().setAccountNumber(1).build();
    var response = this.bankBlockingStub.getAccountBalance(request);
    LOG.info("{}", response);
  }

  private Metadata getApiKey() {
    var metadata = new Metadata();
    metadata.put(Constants.API_KEY, "bank-client-secre");
    return metadata;
  }
}
