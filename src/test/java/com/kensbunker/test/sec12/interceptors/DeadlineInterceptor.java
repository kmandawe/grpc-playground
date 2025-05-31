package com.kensbunker.test.sec12.interceptors;

import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ClientInterceptor;
import io.grpc.MethodDescriptor;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class DeadlineInterceptor implements ClientInterceptor {

  private final Duration duration;

  public DeadlineInterceptor(Duration duration) {
    this.duration = duration;
  }

  @Override
  public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(
      MethodDescriptor<ReqT, RespT> methodDescriptor, CallOptions callOptions, Channel channel) {
    callOptions = callOptions.withDeadlineAfter(duration.toMillis(), TimeUnit.MILLISECONDS);
    return channel.newCall(methodDescriptor, callOptions);
  }
}
