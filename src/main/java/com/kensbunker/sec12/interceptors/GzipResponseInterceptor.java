package com.kensbunker.sec12.interceptors;

import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCall.Listener;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;

public class GzipResponseInterceptor implements ServerInterceptor {

  @Override
  public <ReqT, RespT> Listener<ReqT> interceptCall(
      ServerCall<ReqT, RespT> serverCall,
      Metadata metadata,
      ServerCallHandler<ReqT, RespT> serverCallHandler) {
    serverCall.setCompression("gzip");
    return serverCallHandler.startCall(serverCall, metadata);
  }
}
