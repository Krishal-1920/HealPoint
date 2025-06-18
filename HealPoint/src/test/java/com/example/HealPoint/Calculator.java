package com.example.HealPoint;

import org.junit.jupiter.api.*;

public class Calculator {

    public int add(int a, int b) {
        return a + b;
    }

    @BeforeAll
    static void setupAll() {
        System.out.println("✔ Runs ONCE before all tests");
    }

    @BeforeEach
    void setup() {
        System.out.println("➡ Runs BEFORE each test");
    }

    @Test
    void testAdd() {
        Calculator calc = new Calculator();
        int result = calc.add(2, 3);
        System.out.println(5 +  result +  "2 + 3 should equal 5");
    }

//    @Test
//    void testAd(){
//        testAdd();
//    }

    @AfterEach
    void tearDown() {
        System.out.println("⬅ Runs AFTER each test");
    }

    @AfterAll
    static void tearDownAll() {
        System.out.println("👈 Runs ONCE after all tests");
    }

}
