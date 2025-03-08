package com.example.demotrungtam.classlist.service;

import com.example.demotrungtam.classlist.dto.classEditDto.ClassDetailDto;
import com.example.demotrungtam.classlist.dto.classUpdateDto.ClassDetailUpdateDto;
import com.example.demotrungtam.classlist.repository.AddressListRepository;
import com.example.demotrungtam.classlist.repository.AttendanceClassListRepository;
import com.example.demotrungtam.classlist.repository.ClassDetailListRepository;
import com.example.demotrungtam.classlist.repository.ClassListRepository;
import com.example.demotrungtam.common.Constant;
import com.example.demotrungtam.entity.AddressInfo;
import com.example.demotrungtam.entity.ClassDetail;
import com.example.demotrungtam.entity.ClassInfo;
import com.example.demotrungtam.exception.CustomException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ClassDetailClassListService {
    private final ClassDetailListRepository classDetailRepository;
    private final AttendanceClassListRepository attendanceRepository;
    private final AddressListRepository addressRepository;
    private final ClassListRepository classRepository;
    private final AddressListRepository addressListRepository;

    @Autowired
    public ClassDetailClassListService(
            ClassDetailListRepository classDetailRepository,
            AttendanceClassListRepository attendanceRepository,
            AddressListRepository addressRepository,
            ClassListRepository classRepository,
            AddressListRepository addressListRepository
    ) {
        this.classDetailRepository = classDetailRepository;
        this.attendanceRepository = attendanceRepository;
        this.addressRepository = addressRepository;
        this.classRepository = classRepository;
        this.addressListRepository = addressListRepository;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    public void saveClassDetail(ClassDetailUpdateDto classDetailUpdateDto) {
        // check valid data
        boolean isValidData = classDetailRepository.existClassDetail(
                classDetailUpdateDto.getClassId(),
                classDetailUpdateDto.getClassDetailId()
        );
        if (!isValidData) {
            throw new CustomException(Constant.INVALID_DATA, Constant.ER007);
        }

        Optional<ClassDetail> classDetail = classDetailRepository.findById(classDetailUpdateDto.getClassDetailId());
        String latestAttendanceDate = attendanceRepository.findLatestAttendanceDate(classDetailUpdateDto.getClassId());

        // kiem tra data co phai moi nhat khong
        if (latestAttendanceDate != null && !latestAttendanceDate.equals(classDetailUpdateDto.getLatestAttendanceDate())) {
            throw new CustomException(Constant.OLD_DATA, Constant.ER006);
        }
        if (classDetail.isPresent()) {
            // neu ngay diem danh cuoi la null
            if (latestAttendanceDate == null) {
                classDetail.get().setClassName(classDetailUpdateDto.getClassName());
                Optional<AddressInfo> addressInfo = addressRepository.findById(classDetailUpdateDto.getAddressId());
                addressInfo.ifPresent(info -> classDetail.get().setAddressInfo(info));
                classDetail.get().setSchedule(classDetailUpdateDto.getSchedule());
                classDetail.get().setTuitionSession(classDetailUpdateDto.getTuitionSession());
                classDetail.get().setStudyTime(classDetailUpdateDto.getStudyTime());
                classDetailRepository.save(classDetail.get());
                return;
            }

            LocalDate applyDate = LocalDate.parse(classDetailUpdateDto.getApplyDate());
            LocalDate latestAttendance = LocalDate.parse(latestAttendanceDate);
            LocalDate startDate = LocalDate.parse(classDetail.get().getStartDate());

            // validate applyDate
            if (!applyDate.isAfter(latestAttendance)) {
                throw new CustomException(Constant.INVALID_DATA, Constant.ER007);
            }

            // neu startDate > ngay diem danh cuoi
            if (startDate.isAfter(latestAttendance)) {
                // update ngày kết thúc cho ClassDetail cũ
                Pageable pageable = PageRequest.of(1, 1);
                List<ClassDetail> classDetails = classDetailRepository.getOldClassDetail(classDetailUpdateDto.getClassId(), pageable);
                classDetails.get(0).setEndDate(applyDate.minusDays(1).toString());
                classDetailRepository.save(classDetails.get(0));

                // update thông tin cho ClassDetail mới
                classDetail.get().setClassName(classDetailUpdateDto.getClassName());
                Optional<AddressInfo> addressInfo = addressRepository.findById(classDetailUpdateDto.getAddressId());
                addressInfo.ifPresent(info -> classDetail.get().setAddressInfo(info));
                classDetail.get().setStartDate(applyDate.toString());
                classDetail.get().setSchedule(classDetailUpdateDto.getSchedule());
                classDetail.get().setTuitionSession(classDetailUpdateDto.getTuitionSession());
                classDetail.get().setStudyTime(classDetailUpdateDto.getStudyTime());
                classDetailRepository.save(classDetail.get());
            } else {
                // neu startDate <= ngay diem danh cuoi
                // update ngày kết thúc cho ClassDetail cũ
                LocalDate endDate = applyDate.minusDays(1);
                classDetail.get().setEndDate(endDate.toString());
                classDetailRepository.save(classDetail.get());

                // tạo ClassDetail mới
                ClassDetail newClassDetail = new ClassDetail();
                Optional<ClassInfo> classInfo = classRepository.findById(classDetailUpdateDto.getClassId());
                classInfo.ifPresent(newClassDetail::setClassInfo);
                newClassDetail.setClassName(classDetailUpdateDto.getClassName());
                Optional<AddressInfo> addressInfo = addressListRepository.findById(classDetailUpdateDto.getAddressId());
                addressInfo.ifPresent(newClassDetail::setAddressInfo);
                newClassDetail.setStartDate(applyDate.toString());
                newClassDetail.setEndDate(null);
                newClassDetail.setSchedule(classDetailUpdateDto.getSchedule());
                newClassDetail.setTuitionSession(classDetailUpdateDto.getTuitionSession());
                newClassDetail.setStudyTime(classDetailUpdateDto.getStudyTime());
                classDetailRepository.save(newClassDetail);
            }
        }
    }
}
