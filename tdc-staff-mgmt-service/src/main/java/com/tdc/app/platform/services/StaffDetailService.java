package com.tdc.app.platform.services;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.tdc.app.platform.dto.StaffDetailDto;

public interface StaffDetailService {
	
	List<StaffDetailDto> getAllStaffDetails();
	StaffDetailDto getStaffDetailByStaffDetailId(int staffDetailId);
	StaffDetailDto getStaffDetailByStaffId(int staffId);
	StaffDetailDto saveUpdateStaffDetails(String detailRequest, MultipartFile document, MultipartFile degreeCertificate);
	List<StaffDetailDto> getStaffByExperience(int experience);
	List<StaffDetailDto> getStaffByCompanyName(String companyName);
	//boolean deleteById(Integer staffId);
	boolean deleteByStaffDetailId(Integer staffDetailId);
}
