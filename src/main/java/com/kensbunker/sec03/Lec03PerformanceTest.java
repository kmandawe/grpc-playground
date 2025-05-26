package com.kensbunker.sec03;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.InvalidProtocolBufferException;
import com.kensbunker.models.sec03.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Lec03PerformanceTest {

  private static final Logger LOG = LoggerFactory.getLogger(Lec03PerformanceTest.class);
  private static final ObjectMapper MAPPER = new ObjectMapper();

  public static void main(String[] args) {

    var protoPerson =
        Person.newBuilder()
            .setLastName("sam")
            .setAge(12)
            .setEmail("sam@gmail.com")
            .setEmployed(true)
            .setSalary(1000.2345)
            .setBankAccountNumber(123456789012L)
            .setBalance(-10000)
            .build();
    var jsonPerson =
        new JsonPerson("sam", 12, "sam@gmail.com", true, 1000.2345, 123456789012L, -10000);

//    json(jsonPerson);
//    proto(protoPerson);
    for (int i = 0; i < 5; i++) {
      runTest("json", () -> json(jsonPerson));
      runTest("proto", () -> proto(protoPerson));
    }
  }

  private static void proto(Person person) {
    try {
      var bytes = person.toByteArray();
//      LOG.info("bytes: {}", bytes.length);
      Person.parseFrom(bytes);
    } catch (InvalidProtocolBufferException e) {
      throw new RuntimeException(e);
    }
  }

  private static void json(JsonPerson person) {
    try {
      var bytes = MAPPER.writeValueAsBytes(person);
//      LOG.info("bytes: {}", bytes.length);
      MAPPER.readValue(bytes, JsonPerson.class);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private static void runTest(String testName, Runnable runnable) {
    var start = System.currentTimeMillis();
    for (int i = 0; i < 1_000_000; i++) {
      runnable.run();
    }
    var end = System.currentTimeMillis();
    LOG.info("{}: {}ms", testName, end - start);
  }
}
