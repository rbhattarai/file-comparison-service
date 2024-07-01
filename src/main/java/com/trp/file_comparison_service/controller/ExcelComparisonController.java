package com.trp.file_comparison_service.controller;

import com.trp.file_comparison_service.service.ExcelComparisonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/compare")
public class ExcelComparisonController {

    public static final String ERROR_MESSAGE_FAILED_DELETE = "Failed to delete file: {}";

    @Autowired
    private ExcelComparisonService excelComparisonService;

    @PostMapping(value = "/excel", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Map<String, Object>>> compareFiles(@RequestParam("actualFile") MultipartFile actualFile,
                                                                  @RequestParam("expectedFile") MultipartFile expectedFile,
                                                                  @RequestParam("fileType") String fileType) {
        File actualTempFile=null, expectedTempFile = null;

        try {
            if (fileType == null) {
                return ResponseEntity.badRequest().body(null);
            }
            actualTempFile = File.createTempFile(actualFile.getName(), ".xlsx");
            expectedTempFile = File.createTempFile(expectedFile.getName(), ".xlsx");

            actualFile.transferTo(actualTempFile);
            expectedFile.transferTo(expectedTempFile);

            List<Map<String, Object>> result = excelComparisonService.compareFiles(actualTempFile, expectedTempFile);
            log.info("File Compared Successfully - Actual: {} vs Expected: {}", actualFile.getName(), expectedFile.getName());
            return ResponseEntity.ok(result);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } finally {
            if(actualTempFile != null && actualTempFile.exists()) {
                if(!actualTempFile.delete()) {
                    log.warn(ERROR_MESSAGE_FAILED_DELETE, actualTempFile.getAbsolutePath());
                }
            }

            if(expectedTempFile != null && expectedTempFile.exists()) {
                if(!expectedTempFile.delete()) {
                    log.warn(ERROR_MESSAGE_FAILED_DELETE, expectedTempFile.getAbsolutePath());
                }
            }

        }
    }
}
