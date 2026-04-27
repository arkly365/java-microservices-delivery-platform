package com.example.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class HelloController {

    @Value("${app.environment:unknown}")
    private String environment;

    @Value("${app.branch:unknown}")
    private String branch;

    @Value("${app.imageTag:unknown}")
    private String imageTag;

    @GetMapping("/hello")
    public Map<String, Object> hello() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("service", "service-a");
		result.put("message", "Hello from service-a");
        result.put("environment", environment);
        result.put("branch", branch);
        result.put("name", "小華2");
        result.put("imageTag", imageTag);
        
        return result;
    }

    @GetMapping("/version")
    public Map<String, Object> version() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("environment", environment);
        result.put("branch", branch);
		result.put("service", "service-a");
        result.put("imageTag", imageTag);
        return result;
    }
	
	@GetMapping("/")
    public ProbeResponse root() {
        return new ProbeResponse("service-a", "UP");
    }

    @GetMapping(value = "/robots.txt", produces = MediaType.TEXT_PLAIN_VALUE)
    public String robots() {
        return "User-agent: *\nDisallow:\n";
    }

    @GetMapping(value = "/sitemap.xml", produces = MediaType.APPLICATION_XML_VALUE)
    public String sitemap() {
        return """
            <?xml version="1.0" encoding="UTF-8"?>
            <urlset xmlns="http://www.sitemaps.org/schemas/sitemap/0.9">
              <url>
                <loc>/</loc>
              </url>
              <url>
                <loc>/hello</loc>
              </url>
              <url>
                <loc>/actuator/health</loc>
              </url>
              <url>
                <loc>/version</loc>
              </url>
            </urlset>
            """;
    }

    public record ProbeResponse(String service, String status) {}
}