package com.devops.calculator;

import org.springframework.stereotype.Service;

/**
 * Calculator Service - Saari calculation logic yahan hai
 *
 * @Service = Yeh Spring ka component hai jo business logic handle karta hai.
 *            Controller sirf request lega aur yahan pass karega.
 *            Separation of concerns - ek class ka ek kaam.
 */
@Service
public class CalculatorService {

    /**
     * Addition: num1 + num2
     */
    public CalculatorResult add(double num1, double num2) {
        double result = num1 + num2;
        return new CalculatorResult(num1, num2, "add", result);
    }

    /**
     * Subtraction: num1 - num2
     */
    public CalculatorResult subtract(double num1, double num2) {
        double result = num1 - num2;
        return new CalculatorResult(num1, num2, "subtract", result);
    }

    /**
     * Multiplication: num1 * num2
     */
    public CalculatorResult multiply(double num1, double num2) {
        double result = num1 * num2;
        return new CalculatorResult(num1, num2, "multiply", result);
    }

    /**
     * Division: num1 / num2
     * Special case: zero se divide nahi kar sakte!
     */
    public CalculatorResult divide(double num1, double num2) {
        if (num2 == 0) {
            return new CalculatorResult("Error: Division by zero is not allowed!");
        }
        double result = num1 / num2;
        return new CalculatorResult(num1, num2, "divide", result);
    }

    /**
     * Modulo: num1 % num2 (remainder)
     * Example: 10 % 3 = 1
     */
    public CalculatorResult modulo(double num1, double num2) {
        if (num2 == 0) {
            return new CalculatorResult("Error: Modulo by zero is not allowed!");
        }
        double result = num1 % num2;
        return new CalculatorResult(num1, num2, "modulo", result);
    }

    /**
     * Power: num1 ^ num2 (exponent)
     * Example: 2 ^ 3 = 8
     */
    public CalculatorResult power(double num1, double num2) {
        double result = Math.pow(num1, num2);
        return new CalculatorResult(num1, num2, "power", result);
    }

    /**
     * Square Root: √num1
     */
    public CalculatorResult sqrt(double num1) {
        if (num1 < 0) {
            return new CalculatorResult("Error: Square root of negative number is not real!");
        }
        double result = Math.sqrt(num1);
        return new CalculatorResult(num1, 0, "sqrt", result);
    }

    /**
     * Cube Root: ∛num1
     * Example: ∛27 = 3.0
     */
    public CalculatorResult cubeRoot(double num1) {
        double result = Math.cbrt(num1);
        return new CalculatorResult(num1, 0, "cuberoot", result);
    }

    /**
     * Absolute Value: |num1|
     * Example: |-5| = 5.0
     */
    public CalculatorResult absolute(double num1) {
        double result = Math.abs(num1);
        return new CalculatorResult(num1, 0, "absolute", result);
    }
}