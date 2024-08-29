package com.apple.location.data;

import com.apple.location.domain.Location;
import com.apple.location.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private LocationRepository locationRepository;

    @Override
    public void run(String... args) throws Exception {
        // 데이터가 이미 있는지 확인
        if (locationRepository.count() > 0) {
            return; // 이미 데이터가 있는 경우 CSV 파일을 다시 로드하지 않음
        }

        // CSV 파일 경로를 지정합니다.
        String csvFile = "data/locationData.csv";  // 실제 경로로 수정해야 합니다
        String line;
        String cvsSplitBy = ",";

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new ClassPathResource(csvFile).getInputStream()))) {
        	br.readLine();
        	while ((line = br.readLine()) != null) {
                // CSV 파일의 각 라인을 분할합니다.
                String[] data = line.split(cvsSplitBy);

                // 새로운 Location 객체를 생성하고, 각 필드를 설정합니다.
                Location location = new Location();
                location.setLocationID(Long.parseLong(data[0].trim())); // 첫 번째 컬럼이 locationID라고 가정
                location.setLocationName(data[1].trim()); // 두 번째 컬럼이 locationName이라고 가정

                // Location 객체를 데이터베이스에 저장합니다.
                locationRepository.save(location);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
