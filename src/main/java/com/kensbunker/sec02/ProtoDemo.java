package com.kensbunker.sec02;

import com.kensbunker.models.sec02.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProtoDemo {
  private static final Logger LOG = LoggerFactory.getLogger(ProtoDemo.class);

  public static void main(String[] args) {
    var person = Person.newBuilder().setName("Sam").setAge(12).build();

    LOG.info("Person: {}", person);
  }
}
