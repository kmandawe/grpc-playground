package com.kensbunker.test.sec12.interceptors;

import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ClientInterceptor;
import io.grpc.MethodDescriptor;

public class GzipRequestInterceptor implements ClientInterceptor {


  @Override
  public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(
      MethodDescriptor<ReqT, RespT> methodDescriptor, CallOptions callOptions, Channel channel) {
    return channel.newCall(methodDescriptor, callOptions.withCompression("gzip"));
  }


}

