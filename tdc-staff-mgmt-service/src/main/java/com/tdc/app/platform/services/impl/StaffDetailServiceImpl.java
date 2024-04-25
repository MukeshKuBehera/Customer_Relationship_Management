package com.tdc.app.platform.services.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import com.tdc.app.platform.dto.StaffDetailDto;
import com.tdc.app.platform.dto.StaffDetailRequest;
import com.tdc.app.platform.entities.StaffDetail;
import com.tdc.app.platform.repos.StaffDetailRepository;
import com.tdc.app.platform.services.StaffDetailService;
import com.tdc.app.platform.utility.ImageSaver;
import com.tdc.app.platform.utility.TDCObjectMapper;

import jakarta.transaction.Transactional;

@Service
public class StaffDetailServiceImpl implements StaffDetailService {

	private static final Logger LOGGER = LoggerFactory.getLogger(StaffDetailServiceImpl.class);

	@Autowired
	private StaffDetailRepository staffDetailRepository;

	@Autowired
	private TDCObjectMapper objectMapper;

	@Autowired
	private ImageSaver imageSaver;

	@Override
	public List<StaffDetailDto> getAllStaffDetails() {
		try {
			List<StaffDetailDto> detailBeansList = new ArrayList<>();
			List<StaffDetail> dbDetails = staffDetailRepository.findAll();
			System.out.println(dbDetails.isEmpty());
			if (!CollectionUtils.isEmpty(dbDetails)) {
				detailBeansList = dbDetails.stream()
						.map(detail -> objectMapper.mapEntityToDto(detail, StaffDetailDto.class))
						.collect(Collectors.toList());
			}
			return detailBeansList;
		} catch (Exception e) {
			LOGGER.error("Error occurred while fetching all staff details", e);
			return new ArrayList<>();
		}
	}

	@Override
	public StaffDetailDto getStaffDetailByStaffDetailId(int staffDetailId) {

		return objectMapper.mapEntityToDto(staffDetailRepository.getByStaffDetailId(staffDetailId),
				StaffDetailDto.class);
	}

	@Override
	public StaffDetailDto getStaffDetailByStaffId(int staffId) {

		return objectMapper.mapEntityToDto(staffDetailRepository.getByStaffId(staffId), StaffDetailDto.class);
	}

	@Override
	public StaffDetailDto saveUpdateStaffDetails(String detailRequest, MultipartFile document,
			MultipartFile degreeCertificate) {
		StaffDetailRequest request = objectMapper.mapStringToDto(detailRequest, StaffDetailRequest.class);
		StaffDetailDto empDetails = null;
		try {
			String doc = imageSaver.saveImageToStorage(document);
			String degree = imageSaver.saveImageToStorage(degreeCertificate);
			request.setUploadDocument(doc);
			request.setDegreeCertificate(degree);
			// Inorder to map dto to entity pass dto object and entity class to same method
			StaffDetail entity = objectMapper.mapEntityToDto(request, StaffDetail.class);
			StaffDetail staffDetail = staffDetailRepository.save(entity);
			empDetails = objectMapper.mapEntityToDto(staffDetail, StaffDetailDto.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return empDetails;
	}

	@Override
	public List<StaffDetailDto> getStaffByExperience(int experience) {
		List<StaffDetailDto> listOfEmployeeDto = new ArrayList<>();
		List<StaffDetail> dbDetails = staffDetailRepository.findByExperience(experience);
		if (!CollectionUtils.isEmpty(dbDetails)) {
			listOfEmployeeDto = dbDetails.stream().map(detail -> {

				return objectMapper.mapEntityToDto(detail, StaffDetailDto.class);

			}).collect(Collectors.toList());
		}
		return listOfEmployeeDto;
	}

	/*
	 * @Override public boolean deleteById(Integer staffId) { try { StaffDetail
	 * staffDetail = staffDetailRepository.getByStaffId(staffId); if (staffDetail !=
	 * null) { staffDetailRepository.delete(staffDetail); return true; } else {
	 * throw new ResourceNotFoundException("Staff Detail", "staffId",
	 * String.valueOf(staffId)); } } catch (Exception ex) {
	 * LOGGER.error("Failed to delete staff detail with ID: " + staffId, ex); return
	 * false; } }
	 */

	@Override
	@Transactional
	public boolean deleteByStaffDetailId(Integer staffDetailId) {
		try {
			StaffDetail staffDetail = staffDetailRepository.getByStaffDetailId(staffDetailId);
			if (staffDetail != null) {
				staffDetailRepository.delete(staffDetail);
				LOGGER.info("StaffDetails with Id "+staffDetailId+" deleted successfully");
				return true;
			} else {
				LOGGER.info("StaffDetails with Id "+staffDetailId+" does not exist");
				return false;
			}
		}catch (Exception ex) {

			LOGGER.error("Failed to delete staff detail with ID: " + staffDetailId, ex);
			return false;
		}
	}

	@Override
	public List<StaffDetailDto> getStaffByCompanyName(String companyName) {
		List<StaffDetailDto> listOfEmployeeDto = new ArrayList<>();
		List<StaffDetail> dbDetails = staffDetailRepository.findByCompanyName(companyName);
		if (!CollectionUtils.isEmpty(dbDetails)) {
			listOfEmployeeDto = dbDetails.stream().map(detail -> {

				return objectMapper.mapEntityToDto(detail, StaffDetailDto.class);

			}).collect(Collectors.toList());
		}
		return listOfEmployeeDto;
	}

}
