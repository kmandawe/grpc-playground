package com.kensbunker.sec04;

import com.google.protobuf.Int32Value;
import com.google.protobuf.Timestamp;
import com.kensbunker.models.sec04.Sample;
import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Lec02WellKnownTypes {
  private static final Logger LOG = LoggerFactory.getLogger(Lec02WellKnownTypes.class);

  public static void main(String[] args) {
    var sample =
        Sample.newBuilder()
            .setAge(Int32Value.of(12))
            .setLoginTime(Timestamp.newBuilder().setSeconds(Instant.now().getEpochSecond()))
            .build();

    LOG.info("{}", Instant.ofEpochSecond(sample.getLoginTime().getSeconds()));
  }
}
