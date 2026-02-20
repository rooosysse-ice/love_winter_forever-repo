// java
package com.Easylive.admin.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/debug")
public class DebugCategoryController {
    private final Logger logger = LoggerFactory.getLogger(DebugCategoryController.class);
    private final JdbcTemplate jdbc;

    @Autowired
    public DebugCategoryController(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @GetMapping("/categories")
    public ResponseEntity<List<Map<String, Object>>> categories() {
        List<Map<String, Object>> rows = jdbc.queryForList("SELECT id, name, enabled FROM category");
        logger.info("debug/categories -> found {} rows", rows.size());
        return ResponseEntity.ok(rows);
    }
}
