package com.kensbunker.sec05.parser;

import com.google.protobuf.InvalidProtocolBufferException;
import com.kensbunker.models.sec05.v3.Television;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class V3Parser {
  private static final Logger LOG = LoggerFactory.getLogger(V3Parser.class);

  public static void parse(byte[] bytes) throws InvalidProtocolBufferException {
    var tv = Television.parseFrom(bytes);
    LOG.info("brand: {}", tv.getBrand());
    LOG.info("type: {}", tv.getType());
  }
}
