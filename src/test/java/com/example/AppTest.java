package com.example;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
public class AppTest {
  @Test void testGreet(){ assertEquals("Hello CI", App.greet()); }
}

