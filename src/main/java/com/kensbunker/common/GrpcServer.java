package com.kensbunker.common;

import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.ServerServiceDefinition;
import io.grpc.ServiceDescriptor;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GrpcServer {
  private static final Logger LOG = LoggerFactory.getLogger(GrpcServer.class);

  private final Server server;

  public GrpcServer(Server server) {
    this.server = server;
  }

  public static GrpcServer create(BindableService... services) {
    return create(6565, services);
  }

  public static GrpcServer create(int port, BindableService... services) {
    var builder =
        ServerBuilder.forPort(port)
            .keepAliveTime(10, TimeUnit.SECONDS)
            .keepAliveTimeout(1, TimeUnit.SECONDS)
            .maxConnectionIdle(25, TimeUnit.SECONDS);
    Arrays.asList(services).forEach(builder::addService);
    return new GrpcServer(builder.build());
  }

  public GrpcServer start() {
    var services =
        server.getServices().stream()
            .map(ServerServiceDefinition::getServiceDescriptor)
            .map(ServiceDescriptor::getName)
            .toList();
    try {
      server.start();
      LOG.info("server started, listening on port: {}, services: {}", server.getPort(), services);
      return this;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void await() {
    try {
      server.awaitTermination();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  public void stop() {
    server.shutdownNow();
    LOG.info("server stopped");
  }
}
