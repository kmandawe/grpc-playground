package com.kensbunker.sec02;

import com.kensbunker.models.sec02.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProtoDemo {
  private static final Logger LOG = LoggerFactory.getLogger(ProtoDemo.class);

  public static void main(String[] args) {
    var person1 = createPerson();
    var person2 = createPerson();

    // compare
    LOG.info("equals {}", person1.equals(person2));
    LOG.info("== {}", person1 == person2);

    // mutable? No
//    person1.setName("mike");

    var person3 = person1.toBuilder().setName("mike").build();
    LOG.info("equals {}", person1.equals(person3));
    LOG.info("== {}", person1 == person3);
    LOG.info("person: {}", person3);

    // null?
//    var person4 = person1.toBuilder().setName(null).build();
    var person4 = person1.toBuilder().clearName().build();
    LOG.info("person: {}", person4);
  }

  private static Person createPerson() {
    return Person.newBuilder().setName("Sam").setAge(12).build();
  }
}
