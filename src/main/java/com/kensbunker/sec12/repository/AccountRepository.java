package com.kensbunker.sec12.repository;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AccountRepository {
  public static final Map<Integer, Integer> DB =
      IntStream.rangeClosed(1, 10)
          .boxed()
          .collect(Collectors.toConcurrentMap(Function.identity(), v -> 100));

  public static Integer getBalance(int accountNumber) {
    return DB.get(accountNumber);
  }

  public static Map<Integer, Integer> getAllAccounts() {
    return Collections.unmodifiableMap(DB);
  }

  public static void addAmount(int accountNumber, int amount) {
    DB.computeIfPresent(accountNumber, (k, v) -> v + amount);
  }

  public static void decuctAmount(int accountNumber, int amount) {
    DB.computeIfPresent(accountNumber, (k, v) -> v - amount);
  }
}
