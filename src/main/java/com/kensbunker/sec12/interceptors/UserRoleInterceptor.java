package com.kensbunker.sec12.interceptors;

import com.kensbunker.sec12.Constants;
import com.kensbunker.sec12.UserRole;
import io.grpc.Context;
import io.grpc.Contexts;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.Status;
import java.util.Objects;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
    We have only getAccountBalance feature
    user-token-1, user-token-2 => prime users, return the balance as is
    user-token-3, user-token-4 => standard users, deduct 1$ and return the balance
    any other token => not valid!
 */

public class UserRoleInterceptor implements ServerInterceptor {
  private static final Logger LOG = LoggerFactory.getLogger(UserRoleInterceptor.class);

  private static final Set<String> PRIME_SET = Set.of("user-token-1", "user-token-2");
  private static final Set<String> STANDARD_SET = Set.of("user-token-3", "user-token-4");

  @Override
  public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(
      ServerCall<ReqT, RespT> serverCall,
      Metadata metadata,
      ServerCallHandler<ReqT, RespT> serverCallHandler) {

    var token = extractToken(metadata.get(Constants.USER_TOKEN_KEY));
    LOG.info("token: {}", token);
    var ctx = toContext(token);

    if (Objects.nonNull(ctx)) {
      return Contexts.interceptCall(ctx, serverCall, metadata, serverCallHandler);
    }

    return close(
        serverCall,
        metadata,
        Status.PERMISSION_DENIED.withDescription("user is not allowed to do this operation"));
  }

  private String extractToken(String value) {
    return Objects.nonNull(value) && value.startsWith(Constants.BEARER)
        ? value.substring(Constants.BEARER.length()).trim()
        : null;
  }

  private Context toContext(String token) {
   if(Objects.nonNull(token) && (PRIME_SET.contains(token) || STANDARD_SET.contains(token))) {
     var role = PRIME_SET.contains(token) ? UserRole.PRIME : UserRole.STANDARD;
     return Context.current().withValue(Constants.USER_ROLE_KEY, role);
   }
   return null;
  }

  private <ReqT, RespT> ServerCall.Listener<ReqT> close(
      ServerCall<ReqT, RespT> serverCall, Metadata metadata, Status status) {
    serverCall.close(status, metadata);
    return new ServerCall.Listener<ReqT>() {};
  }
}
