package com.example.demotrungtam.studentattendance.service;

import com.example.demotrungtam.common.Constant;
import com.example.demotrungtam.entity.*;
import com.example.demotrungtam.exception.CustomException;
import com.example.demotrungtam.exception.CustomNotFoundException;
import com.example.demotrungtam.studentattendance.dto.AttendanceDto;
import com.example.demotrungtam.studentattendance.dto.ClassDto;
import com.example.demotrungtam.studentattendance.dto.StudentAttendanceDailyDto;
import com.example.demotrungtam.studentattendance.dto.StudentAttendanceDto;
import com.example.demotrungtam.studentattendance.dto.student_attendance_daily_post_dto.AttendancePostDto;
import com.example.demotrungtam.studentattendance.dto.student_attendance_daily_post_dto.StudentAttendanceDailyPostDto;
import com.example.demotrungtam.studentattendance.dto.student_attendance_daily_post_dto.StudentAttendancePostDto;
import com.example.demotrungtam.studentattendance.repository.*;
import com.example.demotrungtam.studentmanage.repository.UserManageRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.time.LocalDate;

@Service
public class StudentAttendanceService {
    private final StudentRepository studentRepository;
    private final ClassRepository classRepository;
    private final AttendanceRepository attendanceRepository;
    private final StudentAttendanceRepository studentAttendanceRepository;
    private final UserManageRepository userManageRepository;
    private final StudentClassRepository studentClassRepository;

    @Autowired
    public StudentAttendanceService(
            StudentRepository studentRepository,
            ClassRepository classRepository,
            AttendanceRepository attendanceRepository,
            StudentAttendanceRepository studentAttendanceRepository,
            UserManageRepository userManageRepository,
            StudentClassRepository studentClassRepository
    ) {
        this.studentRepository = studentRepository;
        this.classRepository = classRepository;
        this.attendanceRepository = attendanceRepository;
        this.studentAttendanceRepository = studentAttendanceRepository;
        this.userManageRepository = userManageRepository;
        this.studentClassRepository = studentClassRepository;
    }

    /**
     * Tra ve danh sach diem danh cua tung hoc vien trong lop hoc
     *
     * @param classId
     * @param date
     * @return
     */
    public List<Map<String, Object>> getStudentAttendance(int classId, String date) {
        List<StudentAttendanceDto> studentAttendance = studentRepository.getStudentAttendance(classId, date);
        return formatStudentAttendance(studentAttendance, findClassSchedule(classId, date));
    }

    /**
     * Tra ve danh sach ngay diem danh trong thang
     *
     * @param classId
     * @param date
     * @return
     */
    public List<Map<String, Object>> getAttendanceDate(int classId, String date) {
        List<String> classSchedule = findClassSchedule(classId, date);
        List<AttendanceDto> attendanceList = attendanceRepository.findAttendanceByDateAndClassId(classId, date);
        return mergeClassScheduleAndAttendanceList(classSchedule, attendanceList);
    }

    /**
     * Đếm số lượng học viên trong lớp học
     *
     * @param classId
     * @param date
     * @return
     */
    public int countStudentAttendance(int classId, String date) {
        return studentRepository.countStudentAttendance(classId, date);
    }

    /**
     * Tra ve danh sach diem danh cua tung hoc vien trong lop hoc theo ngay
     *
     * @param classId
     * @param date
     * @return
     */
    public List<StudentAttendanceDailyDto> getStudentAttendanceDaily(int classId, String date) {
        // Validate date
        checkExistLessonDate(classId, date);
        return studentRepository.getStudentAttendanceDaily(classId, date);
    }

    @Transactional
    public void saveStudentAttendanceDaily(StudentAttendanceDailyPostDto studentAttendanceDailyPostDto) {
        // Validate date
        checkExistLessonDate(
                studentAttendanceDailyPostDto.getAttendance().getClassId(),
                studentAttendanceDailyPostDto.getAttendance().getAttendanceDate());

        AttendancePostDto attendancePostDto = studentAttendanceDailyPostDto.getAttendance();
        List<StudentAttendancePostDto> studentAttendance = studentAttendanceDailyPostDto.getStudentAttendance();

        // Save - find attendance
        Attendance attendance;
        if (attendancePostDto.getAttendanceId() == null) {
            // validate unique classId and attendanceDate
            checkExistAttendanceByClassIdAndAttendanceDate(attendancePostDto.getClassId(), attendancePostDto.getAttendanceDate());

            Attendance tempAttendance = new Attendance();
            tempAttendance.setAttendanceId(0);
            tempAttendance.setClassInfo(classRepository.findById(attendancePostDto.getClassId()).orElse(null));
            tempAttendance.setAttendanceDate(attendancePostDto.getAttendanceDate());
            attendance = attendanceRepository.save(tempAttendance);
        } else {
            Optional<Attendance> tempAttendance = attendanceRepository.findById(attendancePostDto.getAttendanceId());
            if (tempAttendance.isEmpty()) {
                throw new CustomNotFoundException(Constant.NOT_FOUND, Constant.ER005);
            }
            attendance = tempAttendance.get();
        }

        // Save student attendance
        for (StudentAttendancePostDto item : studentAttendance) {
            StudentAttendance _studentAttendance = new StudentAttendance();
            if (item.getStudentAttendanceId() == null) {
                _studentAttendance.setStudentAttendanceId(0);
            } else {
                _studentAttendance.setStudentAttendanceId(item.getStudentAttendanceId());
            }
            _studentAttendance.setIsAttend(item.getIsAttend());
            _studentAttendance.setScore(item.getScore());
            _studentAttendance.setDoHomework(item.getDoHomework());
            _studentAttendance.setSessionComment(item.getSessionComment());
            _studentAttendance.setAdditionalAttendance(item.getAdditionalAttendance());
            Optional<Student> tempStudent = studentRepository.findById(item.getStudentId());
            if (tempStudent.isEmpty()) {
                throw new CustomNotFoundException(Constant.NOT_FOUND, Constant.ER005);
            }
            _studentAttendance.setStudent(tempStudent.get());
            _studentAttendance.setAttendance(attendance);
            studentAttendanceRepository.save(_studentAttendance);
        }
    }

    /**
     * Tra ve danh sach diem danh cua tung hoc vien trong lop hoc
     *
     * @param studentAttendance
     * @return
     */
    private List<Map<String, Object>> formatStudentAttendance(List<StudentAttendanceDto> studentAttendance, List<String> classSchedule) {
        try {
            Map<Integer, List<StudentAttendanceDto>> groupedResult = studentAttendance.stream()
                    .collect(Collectors.groupingBy(
                            StudentAttendanceDto::getStudentId, // Assuming studentId is the first element in each row
                            LinkedHashMap::new, // Use a LinkedHashMap to preserve the order
                            Collectors.toList()));

            List<Map<String, Object>> finalResult = groupedResult.entrySet().stream()
                    .map(entry -> {
                        Map<String, Object> studentMap = new LinkedHashMap<>();
                        studentMap.put("studentId", entry.getKey());
                        studentMap.put("studentName", entry.getValue().get(0).getStudentName());
                        studentMap.put("monthlyNoteId", entry.getValue().get(0).getMonthlyNoteId()); // Assuming studentName is the second element
                        studentMap.put("monthComment", entry.getValue().get(0).getMonthComment()); // Assuming studentName is the second element
                        List<Map<String, Object>> attendanceList = new ArrayList<>();
                        for (String date : classSchedule) {
                            Map<String, Object> attendanceItem = findAttendanceByDateFormatStudent(entry.getValue(), date);
                            if (attendanceItem != null) {
                                attendanceList.add(attendanceItem);
                            } else {
                                attendanceList.add(createDefaultAttendance(date));
                            }
                        }
                        if (attendanceList.get(0).get("attendanceDate") == null) {
                            studentMap.put("attendance", new ArrayList<>());
                        } else {
                            studentMap.put("attendance", attendanceList);
                        }
                        return studentMap;
                    }).toList();
            return finalResult;
        } catch (IndexOutOfBoundsException e) {
            throw new CustomNotFoundException(Constant.NOT_FOUND, Constant.ER005);
        }
    }

    /**
     * Tra ve danh sach ngay diem danh cua lop hoc
     *
     * @param classId
     * @param date
     * @return
     */
    private List<String> findClassSchedule(int classId, String date) {
        List<ClassDto> classDetails = classRepository.findClassDetail(classId, date);
        int selectedYear = Integer.parseInt(date.split("-")[0]);
        int selectedMonth = Integer.parseInt(date.split("-")[1]);

        List<String> attendanceDate = new ArrayList<>();

        // Tính toán ngày đầu tiên của tháng từ selectedDate
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, selectedYear);
        calendar.set(Calendar.MONTH, selectedMonth - 1); // Giảm đi 1 vì tháng trong Calendar bắt đầu từ 0
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0); // Đặt giờ về 0
        calendar.set(Calendar.MINUTE, 0);      // Đặt phút về 0
        calendar.set(Calendar.SECOND, 0);      // Đặt giây về 0
        calendar.set(Calendar.MILLISECOND, 0); // Đặt mili-giây về 0
        Date firstDayOfMonth = calendar.getTime();

        // Tính toán ngày cuối cùng của tháng từ selectedDate
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date lastDayOfMonth = calendar.getTime();

        for (ClassDto item : classDetails) {
            // Tính toán ngày bắt đầu từ item.startDate
            Date startDay = Objects.requireNonNull(parseDate(item.getStartDate())).after(firstDayOfMonth) ? parseDate(item.getStartDate()) : firstDayOfMonth;

            // Tính toán ngày kết thúc từ item.endDate hoặc lastDayOfMonth
            Date endDay = (item.getEndDate() == null || Objects.requireNonNull(parseDate(item.getEndDate())).after(lastDayOfMonth)) ?
                    lastDayOfMonth : parseDate(item.getEndDate());

            // Chuyển item.schedule thành mảng các ngày
            String[] scheduleDaysArray = item.getSchedule().split(",");
            int[] scheduleDays = new int[scheduleDaysArray.length];
            for (int i = 0; i < scheduleDaysArray.length; i++) {
                scheduleDays[i] = Integer.parseInt(scheduleDaysArray[i]);
            }

            // Lặp qua mỗi ngày trong khoảng thời gian
            Calendar currentDay = Calendar.getInstance();
            currentDay.setTime(Objects.requireNonNull(startDay));
            while (!currentDay.getTime().after(endDay)) {
                if (contains(scheduleDays, currentDay.get(Calendar.DAY_OF_WEEK))) {
                    // Format ngày và thêm vào danh sách
                    attendanceDate.add(formatDate(currentDay.getTime()));
                }
                // Tăng ngày lên 1
                currentDay.add(Calendar.DAY_OF_MONTH, 1);
            }
        }
        return attendanceDate;
    }

    /**
     * Kiểm tra xem một mảng có chứa một giá trị nào đó không
     *
     * @param array
     * @param target
     * @return
     */
    private boolean contains(int[] array, int target) {
        for (int value : array) {
            if (value == target) {
                return true;
            }
        }
        return false;
    }

    /**
     * Chuyển đổi đối tượng Date thành chuỗi theo định dạng "yyyy-MM-dd"
     *
     * @param date
     * @return
     */
    private String formatDate(Date date) {
        // Format ngày thành chuỗi theo định dạng "yyyy-MM-dd"
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }

    /**
     * Chuyển chuỗi ngày thành đối tượng Date
     *
     * @param dateString
     * @return
     */
    private Date parseDate(String dateString) {
        // Chuyển chuỗi ngày thành đối tượng Date
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return dateFormat.parse(dateString);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Gộp danh sách ngày diem danh và danh sách diem danh
     *
     * @param classSchedule
     * @param attendanceList
     * @return
     */
    private List<Map<String, Object>> mergeClassScheduleAndAttendanceList(List<String> classSchedule, List<AttendanceDto> attendanceList) {
        List<Map<String, Object>> mergedList = new ArrayList<>();

        for (String date : classSchedule) {
            Map<String, Object> mergedItem = new HashMap<>();
            mergedItem.put("attendanceDate", date);

            // Kiểm tra xem ngày có trong danh sách điểm danh hay không
            AttendanceDto attendanceItem = findAttendanceByDate(attendanceList, date);
            if (attendanceItem != null) {
                mergedItem.put("attendanceId", attendanceItem.getAttendanceId());
            } else {
                mergedItem.put("attendanceId", null);
            }

            mergedList.add(mergedItem);
        }

        return mergedList;
    }

    /**
     * Tìm kiếm điểm danh theo ngày
     *
     * @param attendanceList
     * @param date
     * @return
     */
    private AttendanceDto findAttendanceByDate(List<AttendanceDto> attendanceList, String date) {
        for (AttendanceDto item : attendanceList) {
            if (date.equals(item.getAttendanceDate())) {
                return item;
            }
        }
        return null;
    }

    /**
     * Tìm kiếm điểm danh của học viên theo ngày
     *
     * @param attendanceList
     * @param date
     * @return
     */
    private Map<String, Object> findAttendanceByDateFormatStudent(List<StudentAttendanceDto> attendanceList, String date) {
        for (StudentAttendanceDto student : attendanceList) {
            if (date.equals(student.getAttendanceDate())) {
                Map<String, Object> attendanceItem = new LinkedHashMap<>();
                attendanceItem.put("attendanceDate", date);
                attendanceItem.put("isAttend", student.getIsAttend());
                attendanceItem.put("additionalAttendance", student.getAdditionalAttendance());
                return attendanceItem;
            }
        }
        return null;
    }

    /**
     * Tạo điểm danh mặc định
     *
     * @param date
     * @return
     */
    private Map<String, Object> createDefaultAttendance(String date) {
        Map<String, Object> attendanceItem = new LinkedHashMap<>();
        attendanceItem.put("attendanceDate", date);
        attendanceItem.put("isAttend", null);
        attendanceItem.put("additionalAttendance", null);
        return attendanceItem;
    }

    /**
     * Kiểm tra xem ngày có trong danh sách điểm danh hay không
     *
     * @param classId
     * @param date
     */
    private void checkExistLessonDate(int classId, String date) {
        List<String> classSchedule = findClassSchedule(classId, date.substring(0, 7));
        boolean isLessonDate = classSchedule.stream().anyMatch(lessonDate -> lessonDate.equals(date));
        if (!isLessonDate) {
            throw new CustomNotFoundException(Constant.NOT_FOUND, Constant.ER005);
        }
    }

    /**
     * Kiểm tra xem attendance đã tồn tại hay chưa
     * @param classId
     * @param date
     */
    private void checkExistAttendanceByClassIdAndAttendanceDate(Integer classId, String date) {
        if (attendanceRepository.existsAttendanceByClassIdAndAttendanceDate(classId, date)) {
            throw new CustomException(Constant.ACCESS_DENIED, Constant.ER004);
        }
    }



    public void saveStudentClass(Integer classId , String userName){
        // find user
        User userEntity = userManageRepository.findByUsername(userName);

        // find student
        Student student = studentRepository.findByUser(userEntity);

        // find class
        ClassInfo classInfo = classRepository.findByClassId(classId);


        // save student class
        StudentClass studentClass= new StudentClass();
        studentClass.setStudentClassId(0);
        studentClass.setStudent(student);
        studentClass.setClassInfo(classInfo);
        studentClass.setJoinDate(LocalDate.now().toString());
        studentClass.setLeaveDate("2025-12-31");
        studentClassRepository.save(studentClass);


    }

}
