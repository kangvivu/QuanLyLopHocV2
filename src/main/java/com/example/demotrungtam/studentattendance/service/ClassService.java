package com.example.demotrungtam.studentattendance.service;

import com.example.demotrungtam.common.Constant;
import com.example.demotrungtam.exception.CustomNotFoundException;
import com.example.demotrungtam.studentattendance.dto.ClassDto;
import com.example.demotrungtam.studentattendance.repository.ClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ClassService {
    private final ClassRepository classRepository;

    @Autowired
    public ClassService(ClassRepository classRepository) {
        this.classRepository = classRepository;

    }

    /**
     * Get class detail
     *
     * @param classId
     * @param date
     * @return
     */
    public Map<String, Object> findClassDetail(int classId, String date) {
        try {
            List<ClassDto> classDetails = classRepository.findClassDetail(classId, date);
            Map<String, Object> formatClassDetail = new LinkedHashMap<>();
            formatClassDetail.put("classId", classDetails.get(0).getClassId());
            formatClassDetail.put("className", classDetails.get(0).getClassName());
            List<Map<String, String>> classScheduleTime = classDetails.stream()
                    .map(scheduleTime -> new LinkedHashMap<String, String>() {{
                        put("schedule", scheduleTime.getSchedule());
                        put("startDate", scheduleTime.getStartDate());
                        put("endDate", scheduleTime.getEndDate());
                    }})
                    .collect(Collectors.toList());
            formatClassDetail.put("classDetail", classScheduleTime);
            return formatClassDetail;
        } catch (IndexOutOfBoundsException e) {
            throw new CustomNotFoundException(Constant.NOT_FOUND, Constant.ER005);
        }
    }
}
