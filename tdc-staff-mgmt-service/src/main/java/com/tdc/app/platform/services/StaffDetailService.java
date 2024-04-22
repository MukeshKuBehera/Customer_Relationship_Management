package com.tdc.app.platform.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.tdc.app.platform.dto.StaffDetailDto;

public interface StaffDetailService {
	
	List<StaffDetailDto> getAllStaffDetails();
	StaffDetailDto getStaffDetailById(int staffDetailId);
	StaffDetailDto saveUpdateStaffDetails(String detailRequest, MultipartFile document, MultipartFile degreeCertificate);
	//List<StaffDetailDto> getStaffDetailsByDate(LocalDate date);
	void deleteStaffDetailsByIds(List<Integer> ids);
	void deleteStaffDetailById(Integer staffDetId);
}
