package com.example.demo.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Service
public class CrawlingService {

    @Scheduled(fixedRate = 10800000) // 6시간마다 실행 (밀리초 단위)
    public void performCrawling() {
        try {
            // 프로젝트의 resources 디렉토리에 있는 crawler.py 파일을 실행
            Process process = Runtime.getRuntime().exec("python3 src/main/resources/crawler.py");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
