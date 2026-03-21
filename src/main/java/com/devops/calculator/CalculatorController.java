package com.devops.calculator;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
 
/**
 * Calculator Controller - Saare REST endpoints yahan hain
 *
 * Base URL: /api/calculator
 *
 * Endpoints:
 *   GET /api/calculator/add?num1=10&num2=5       → 15.0
 *   GET /api/calculator/subtract?num1=10&num2=5  → 5.0
 *   GET /api/calculator/multiply?num1=10&num2=5  → 50.0
 *   GET /api/calculator/divide?num1=10&num2=5    → 2.0
 *   GET /api/calculator/modulo?num1=10&num2=3    → 1.0
 *   GET /api/calculator/power?num1=2&num2=8      → 256.0
 *   GET /api/calculator/sqrt?num1=16             → 4.0
 *   GET /api/calculator/cuberoot?num1=27         → 3.0
 *
 * @RequestParam ke baare mein:
 *   URL mein "?" ke baad jo values aate hain unhe query parameters kehte hain.
 *   @RequestParam("num1") woh value padhta hai aur variable mein deta hai.
 */
@RestController
@RequestMapping("/api/calculator")
@CrossOrigin(origins = "*")  // Frontend se access allow karne ke liye
public class CalculatorController {
 
    // Service inject karo (Spring automatically dega)
    @Autowired
    private CalculatorService calculatorService;
 
    /**
     * GET /api/calculator/add?num1=10&num2=5
     * Try karo: curl "http://localhost:8080/api/calculator/add?num1=10&num2=5"
     */
    @GetMapping("/add")
    public CalculatorResult add(
            @RequestParam("num1") double num1,
            @RequestParam("num2") double num2) {
        return calculatorService.add(num1, num2);
    }
 
    /**
     * GET /api/calculator/subtract?num1=10&num2=3
     */
    @GetMapping("/subtract")
    public CalculatorResult subtract(
            @RequestParam("num1") double num1,
            @RequestParam("num2") double num2) {
        return calculatorService.subtract(num1, num2);
    }
 
    /**
     * GET /api/calculator/multiply?num1=4&num2=5
     */
    @GetMapping("/multiply")
    public CalculatorResult multiply(
            @RequestParam("num1") double num1,
            @RequestParam("num2") double num2) {
        return calculatorService.multiply(num1, num2);
    }
 
    /**
     * GET /api/calculator/divide?num1=20&num2=4
     * Agar num2=0 ho toh error return hoga
     */
    @GetMapping("/divide")
    public CalculatorResult divide(
            @RequestParam("num1") double num1,
            @RequestParam("num2") double num2) {
        return calculatorService.divide(num1, num2);
    }
 
    /**
     * GET /api/calculator/modulo?num1=10&num2=3
     * Returns remainder: 10 % 3 = 1
     */
    @GetMapping("/modulo")
    public CalculatorResult modulo(
            @RequestParam("num1") double num1,
            @RequestParam("num2") double num2) {
        return calculatorService.modulo(num1, num2);
    }
 
    /**
     * GET /api/calculator/power?num1=2&num2=8
     * Returns: 2^8 = 256
     */
    @GetMapping("/power")
    public CalculatorResult power(
            @RequestParam("num1") double num1,
            @RequestParam("num2") double num2) {
        return calculatorService.power(num1, num2);
    }
 
    /**
     * GET /api/calculator/sqrt?num1=16
     * Returns: √16 = 4.0
     */
    @GetMapping("/sqrt")
    public CalculatorResult sqrt(
            @RequestParam("num1") double num1) {
        return calculatorService.sqrt(num1);
    }
 
    /**
     * GET /api/calculator/cuberoot?num1=27
     * Returns: ∛27 = 3.0
     */
    @GetMapping("/cuberoot")
    public CalculatorResult cubeRoot(
            @RequestParam("num1") double num1) {
        return calculatorService.cubeRoot(num1);
    }
 
    /**
     * GET /api/calculator/health
     * Quick check: app chal rahi hai ya nahi
     */
    @GetMapping("/health")
    public String health() {
        return "Calculator App is running!";
    }
}