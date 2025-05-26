package com.kensbunker.sec03;

import com.kensbunker.models.sec03.Address;
import com.kensbunker.models.sec03.School;
import com.kensbunker.models.sec03.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Lec04Composition {
  private static final Logger LOG = LoggerFactory.getLogger(Lec04Composition.class);

  public static void main(String[] args){
    // create student
    var address = Address.newBuilder()
        .setStreet("123 main st")
        .setCity("atlanta")
        .setState("GA")
        .build();

    var student = Student.newBuilder()
        .setName("sam")
        .setAddress(address);

    // create school
    var school = School.newBuilder()
        .setId(1)
        .setName("high school")
        .setAddress(address.toBuilder().setStreet("234 main st").build())
        .build();
    LOG.info("school: {}", school);
    LOG.info("student: {}", student);

  }
}
