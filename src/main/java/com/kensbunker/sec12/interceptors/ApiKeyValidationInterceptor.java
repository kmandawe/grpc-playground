package com.kensbunker.sec12.interceptors;

import com.kensbunker.sec12.Constants;
import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCall.Listener;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import io.grpc.Status;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApiKeyValidationInterceptor implements ServerInterceptor {

  private static final Logger LOG = LoggerFactory.getLogger(ApiKeyValidationInterceptor.class);

  @Override
  public <ReqT, RespT> Listener<ReqT> interceptCall(
      ServerCall<ReqT, RespT> serverCall,
      Metadata metadata,
      ServerCallHandler<ReqT, RespT> serverCallHandler) {

    LOG.info("{}", serverCall.getMethodDescriptor().getFullMethodName());
    var apiKey = metadata.get(Constants.API_KEY);
    if (isValid(apiKey)) {
      return serverCallHandler.startCall(serverCall, metadata);
    }
    serverCall.close(
        Status.UNAUTHENTICATED.withDescription("client must provide valid api key"), metadata);
    return new ServerCall.Listener<ReqT>() {};
  }

  private boolean isValid(String apiKey) {
    return Objects.nonNull(apiKey) && apiKey.equals("bank-client-secret");
  }
}
