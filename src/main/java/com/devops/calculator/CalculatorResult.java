package com.devops.calculator;

/**
 * Calculator ka result return karne ke liye yeh class use hogi.
 * Jab bhi koi calculation hogi, iska object JSON mein return hoga.
 *
 * Example response:
 * {
 *   "num1": 10.0,
 *   "num2": 5.0,
 *   "operation": "add",
 *   "result": 15.0,
 *   "expression": "10.0 + 5.0 = 15.0"
 * }
 */
public class CalculatorResult {

    private double num1;
    private double num2;
    private String operation;
    private double result;
    private String expression;
    private String status;

    // Constructor
    public CalculatorResult(double num1, double num2, String operation, double result) {
        this.num1      = num1;
        this.num2      = num2;
        this.operation = operation;
        this.result    = result;
        this.status    = "success";

        // Human-readable expression banana
        String symbol  = getSymbol(operation);
        this.expression = num1 + " " + symbol + " " + num2 + " = " + result;
    }

    // Error ke liye alag constructor
    public CalculatorResult(String errorMessage) {
        this.status     = "error";
        this.expression = errorMessage;
    }

    // Operation ka symbol return karta hai
    private String getSymbol(String operation) {
        switch (operation.toLowerCase()) {
            case "add":      return "+";
            case "subtract": return "-";
            case "multiply": return "×";
            case "divide":   return "÷";
            default:         return "?";
        }
    }

    // Getters (Spring JSON conversion ke liye zaroori hain)
    public double getNum1()        { return num1; }
    public double getNum2()        { return num2; }
    public String getOperation()   { return operation; }
    public double getResult()      { return result; }
    public String getExpression()  { return expression; }
    public String getStatus()      { return status; }
}
