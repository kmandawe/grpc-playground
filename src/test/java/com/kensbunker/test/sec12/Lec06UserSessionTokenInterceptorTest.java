package com.kensbunker.test.sec12;

import com.kensbunker.common.GrpcServer;
import com.kensbunker.models.sec12.BalanceCheckRequest;
import com.kensbunker.models.sec12.Money;
import com.kensbunker.models.sec12.WithdrawRequest;
import com.kensbunker.sec12.BankService;
import com.kensbunker.sec12.Constants;
import com.kensbunker.sec12.interceptors.UserTokenInterceptor;
import com.kensbunker.test.common.ResponseObserver;
import io.grpc.CallCredentials;
import io.grpc.ClientInterceptor;
import io.grpc.Metadata;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Lec06UserSessionTokenInterceptorTest extends AbstractInterceptorTest {

  private static final Logger LOG =
      LoggerFactory.getLogger(Lec06UserSessionTokenInterceptorTest.class);

  @Override
  protected List<ClientInterceptor> getClientInterceptors() {
    return Collections.emptyList();
  }

  @Override
  protected GrpcServer createServer() {
    return GrpcServer.create(
        6565,
        builder -> {
          builder.addService(new BankService()).intercept(new UserTokenInterceptor());
        });
  }

  @Test
  public void unaryUserCredentialsDemo() {
    for (int i = 1; i <= 5; i++) {

      var request = BalanceCheckRequest.newBuilder().setAccountNumber(1).build();
      var response =
          this.bankBlockingStub
              .withCallCredentials(new UserSessionToken("user-token-" + i))
              .getAccountBalance(request);
      LOG.info("{}", response);
    }
  }

  @Test
  public void streamingUserCredentialsDemo() {
    for (int i = 1; i <= 5; i++) {
      var observer = ResponseObserver.<Money>create();
      var request = WithdrawRequest.newBuilder().setAccountNumber(i).setAmount(30).build();
      this.bankStub
          .withCallCredentials(new UserSessionToken("user-token-" + i))
          .withdraw(request, observer);
      observer.await();
    }
  }

  private static class UserSessionToken extends CallCredentials {

    private static final String TOKEN_FORMAT = "%s %s";
    private final String jwt;

    private UserSessionToken(String jwt) {
      this.jwt = jwt;
    }

    @Override
    public void applyRequestMetadata(
        RequestInfo requestInfo, Executor executor, MetadataApplier metadataApplier) {
      executor.execute(
          () -> {
            var metadata = new Metadata();
            metadata.put(Constants.USER_TOKEN_KEY, TOKEN_FORMAT.formatted(Constants.BEARER, jwt));
            metadataApplier.apply(metadata);
          });
    }
  }
}
