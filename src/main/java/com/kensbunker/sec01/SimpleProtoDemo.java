package com.kensbunker.sec01;

import com.kensbunker.models.sec01.PersonOuterClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleProtoDemo {
  private static final Logger LOG = LoggerFactory.getLogger(SimpleProtoDemo.class);

  public static void main(String[] args) {
    var person = PersonOuterClass.Person.newBuilder().setName("Sam").setAge(12).build();
    LOG.info("Person: {}", person);
  }
}
