package com.kensbunker.sec05;

import com.google.protobuf.InvalidProtocolBufferException;
import com.kensbunker.models.sec05.v2.Television;
import com.kensbunker.models.sec05.v2.Type;
import com.kensbunker.sec05.parser.V1Parser;
import com.kensbunker.sec05.parser.V2Parser;
import com.kensbunker.sec05.parser.V3Parser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class V2VersionCompatibility {
  private static final Logger LOG = LoggerFactory.getLogger(V2VersionCompatibility.class);

  public static void main(String[] args) throws InvalidProtocolBufferException {
    var tv = Television.newBuilder().setBrand("samsung").setModel(2019).setType(Type.UHD).build();

    V1Parser.parse(tv.toByteArray());
    V2Parser.parse(tv.toByteArray());
    V3Parser.parse(tv.toByteArray());
  }
}
