package com.devops.calculator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Info Controller - Build Traceability ke liye
 *
 * GET /info → WAR file ka Git commit SHA aur build info deta hai
 *
 * Yeh endpoint isliye zaroori hai kyunki:
 * Production mein agar koi puche "kaunsa version chal raha hai?"
 * toh yahan se pata chalega - commit SHA, build time, branch sab kuch.
 *
 * @Value annotation kaise kaam karta hai:
 *   - application.properties mein jo ${git.commit.id.full} likha hai
 *   - woh build ke time par git-commit-id-plugin fill karta hai
 *   - Spring Boot startup pe yeh value yahan inject hoti hai
 */
@RestController
public class InfoController {

    @Value("${app.name:Calculator App}")
    private String appName;

    @Value("${app.version:1.0.0}")
    private String version;

    // Yeh value git-commit-id-plugin automatically bharega build ke time
    @Value("${git.commit.id.full:GIT-NOT-CONFIGURED}")
    private String gitCommitFull;

    @Value("${git.commit.id.abbrev:N/A}")
    private String gitCommitShort;

    @Value("${git.branch:unknown}")
    private String gitBranch;

    @Value("${git.commit.time:unknown}")
    private String commitTime;

    @Value("${git.commit.message.short:no-message}")
    private String lastCommitMessage;

    @Value("${build.timestamp:unknown}")
    private String buildTimestamp;

    /**
     * GET /info
     *
     * Response example:
     * {
     *   "appName": "Calculator App",
     *   "version": "1.0.0",
     *   "gitCommitFull": "4d8ef2fcf426ba73554572e4a60d4ac937ac5f43",
     *   "gitCommitShort": "4d8ef2f",
     *   "gitBranch": "main",
     *   "lastCommitMessage": "Add calculator operations",
     *   "commitTime": "2026-03-21T15:30:00+0530",
     *   "buildTimestamp": "2026-03-21 15:30:44",
     *   "javaVersion": "17.0.10"
     * }
     */
    @GetMapping("/info")
    public Map<String, Object> getInfo() {
        Map<String, Object> info = new LinkedHashMap<>();

        // App details
        info.put("appName",           appName);
        info.put("version",           version);

        // Git traceability details (BUILD TRACEABILITY)
        info.put("gitCommitFull",     gitCommitFull);
        info.put("gitCommitShort",    gitCommitShort);
        info.put("gitBranch",         gitBranch);
        info.put("lastCommitMessage", lastCommitMessage);
        info.put("commitTime",        commitTime);

        // Build details
        info.put("buildTimestamp",    buildTimestamp);
        info.put("javaVersion",       System.getProperty("java.version"));
        info.put("availableEndpoints", new String[]{
            "GET  /api/calculator/add?num1=X&num2=Y",
            "GET  /api/calculator/subtract?num1=X&num2=Y",
            "GET  /api/calculator/multiply?num1=X&num2=Y",
            "GET  /api/calculator/divide?num1=X&num2=Y",
            "GET  /api/calculator/modulo?num1=X&num2=Y",
            "GET  /api/calculator/power?num1=X&num2=Y",
            "GET  /api/calculator/sqrt?num1=X",
            "GET  /api/calculator/health",
            "GET  /info"
        });

        return info;
    }
}
