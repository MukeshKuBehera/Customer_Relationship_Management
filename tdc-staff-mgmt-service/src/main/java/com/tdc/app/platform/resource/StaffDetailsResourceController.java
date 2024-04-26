package com.tdc.app.platform.resource;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tdc.app.platform.constants.StaffDetailConstants;
import com.tdc.app.platform.dto.StaffDetailDto;
import com.tdc.app.platform.dto.StaffDetailResponse;
import com.tdc.app.platform.exception.ErrorMessage;
import com.tdc.app.platform.exception.TDCServiceException;
import com.tdc.app.platform.services.StaffDetailService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api")
@Tag(name = "StaffDetail", description = "The StaffDetail api")
public class StaffDetailsResourceController {

	@Autowired
	private StaffDetailService staffDetailService;

	private static final Logger LOGGER = LoggerFactory.getLogger(StaffDetailsResourceController.class);

	/**
	 * Get Staff Detail.gokulrajender@gmail.com
	 *
	 * @param staffDetId - to get particular designation
	 * @return the TDC response
	 * @throws TDCServiceException will thrown if anything went wrong
	 */
	@GetMapping("/detail/{staffDetId}")
	@Operation(summary = "Fetch staff detail by id", description = "fetches all staff detail entities and their data from data source by id")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "successful operation") })
	public StaffDetailResponse getStaffDetail(@PathVariable Integer staffDetId) throws TDCServiceException {
		StaffDetailResponse sdResponseDto = new StaffDetailResponse();
		try {
			final StaffDetailDto staffDetResponse = staffDetailService.getStaffDetailByStaffDetailId(staffDetId);

			if (staffDetResponse != null) {
				sdResponseDto.setStatusCode(StaffDetailConstants.STATUS_200);
				sdResponseDto.setData(staffDetResponse);
				sdResponseDto.setStatusMessage(StaffDetailConstants.MESSAGE_200);
				LOGGER.info("Staff Detail data pulled successfully");
			} else {
				sdResponseDto.setStatusCode(StaffDetailConstants.ERROR_CODE_404);
				sdResponseDto.setStatusMessage(StaffDetailConstants.ERROR_CODE_404_MSG);
				LOGGER.info("Staff Detail data not found");
			}

		} catch (TDCServiceException ex) {
			sdResponseDto.setStatusCode(StaffDetailConstants.ERROR_CODE_500);
			ErrorMessage error = new ErrorMessage();
			error.setErrorCode(StaffDetailConstants.ERROR_CODE_500);
			error.setErrorMessage(StaffDetailConstants.ERROR_CODE_500_MSG);
			sdResponseDto.setError(error);
			LOGGER.error("Error occurred while fetching staff details", ex);
			// ex.printStackTrace();
		}
		return sdResponseDto;
	}

	/**
	 * Get Staff details.
	 *
	 * @return the Staff details response
	 * @throws TDCServiceException will thrown if anything went wrong
	 */
	@GetMapping("/detail")
	@Operation(summary = "Fetch all Staff details", description = "fetches all staffdetails entities and their data from data source")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "successful operation"),
			@ApiResponse(responseCode = "404", description = "no staff details found") })
	public StaffDetailResponse getAllStaffDetails() throws TDCServiceException {
		StaffDetailResponse staffDetailResponse = new StaffDetailResponse();
		try {
			final List<StaffDetailDto> staffDetailDto = staffDetailService.getAllStaffDetails();

			if (staffDetailDto != null && !staffDetailDto.isEmpty()) {
				staffDetailResponse.setStatusCode(StaffDetailConstants.STATUS_200);
				staffDetailResponse.setDataList(staffDetailDto);
				staffDetailResponse.setStatusMessage(StaffDetailConstants.MESSAGE_200);
				LOGGER.info("Staff details data pulled successfully");
			} else {
				staffDetailResponse.setStatusCode(StaffDetailConstants.ERROR_CODE_404);
				staffDetailResponse.setStatusMessage(StaffDetailConstants.ERROR_CODE_404_MSG);
				LOGGER.info("Staff Detail data not found");
			}
		} catch (TDCServiceException ex) {
			staffDetailResponse.setStatusCode(StaffDetailConstants.ERROR_CODE_500);
			staffDetailResponse.setStatusMessage("Error occurred while fetching staff details");
			ErrorMessage error = new ErrorMessage();
			error.setErrorCode(StaffDetailConstants.ERROR_CODE_500);
			error.setErrorMessage(StaffDetailConstants.ERROR_CODE_500_MSG);
			staffDetailResponse.setError(error);
			LOGGER.error("Error occurred while fetching staff details", ex);
		}
		return staffDetailResponse;
	}

	/**
	 * Save Staff employment details.
	 *
	 * @return the Staff details response
	 * @throws TDCServiceException will thrown if anything went wrong
	 */
	@PostMapping(value = "/detail", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE }, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	@Operation(summary = "Persist Staff details", description = "persist staffdetails entities and their data to database")
	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "successful operation") })
	public StaffDetailResponse persistStaffDetails(
			@RequestParam(name = "document", required = false) MultipartFile document,
			@RequestParam(name = "degreeCert", required = false) MultipartFile degreeCertificate,
			@RequestParam String detailRequest) throws TDCServiceException {
		LOGGER.info("The incoming request is: {}", detailRequest);

		StaffDetailResponse staffDetailResponse = new StaffDetailResponse();
		try {
			final StaffDetailDto staffDetailDto = staffDetailService.saveUpdateStaffDetails(detailRequest, document,
					degreeCertificate);
			staffDetailResponse.setStatusCode(StaffDetailConstants.STATUS_201);
			staffDetailResponse.setData(staffDetailDto);
			staffDetailResponse.setStatusMessage(StaffDetailConstants.MESSAGE_201);

			LOGGER.info("StaffDetail saved sucessfully");
		} catch (TDCServiceException ex) {
			staffDetailResponse.setStatusCode(StaffDetailConstants.ERROR_CODE_500);
			staffDetailResponse.setStatusMessage("Error occurred while inserting staff details");

			ErrorMessage error = new ErrorMessage();
			error.setErrorCode(StaffDetailConstants.ERROR_CODE_500);
			error.setErrorMessage(StaffDetailConstants.ERROR_CODE_500_MSG);
			staffDetailResponse.setError(error);
			LOGGER.error("StaffDetail not saved {}", ex);
		}
		return staffDetailResponse;
	}

	// GetMethods with other options staffId
	// Based on date we should have get call(more then 1 record)
	// Delete by id
	// Delete within range(IN clause)
	// Remove hard code constants

	/**
	 * Get The Employee Based on Experience
	 * 
	 * @return -> staffDetails
	 * @Parrams -> integer value
	 * @Throws -> exception
	 *
	 */

	@GetMapping("/exp/{experience}")
	@Operation(summary = "Fetch staff detail by Experience", description = "fetches all staff detail entities and their data from data source by Experience")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "successful operation") })
	public StaffDetailResponse getStaffDetailByExperience(@PathVariable int experience) throws TDCServiceException {
		StaffDetailResponse staffDetailResponse = new StaffDetailResponse();
		try {
			List<StaffDetailDto> staffDetResponse = staffDetailService.getStaffByExperience(experience);
			if (staffDetResponse != null && !staffDetResponse.isEmpty()) {
				staffDetailResponse.setStatusCode(StaffDetailConstants.STATUS_200);
				staffDetailResponse.setDataList(staffDetResponse);
				staffDetailResponse.setStatusMessage(StaffDetailConstants.SUCCESS_MSG);
				LOGGER.info("Staff Detail data pulled successfully");
			} else {
				staffDetailResponse.setStatusCode(StaffDetailConstants.ERROR_CODE_404);
				staffDetailResponse.setStatusMessage(StaffDetailConstants.ERROR_CODE_404_MSG);
				LOGGER.info("StaffDetails data not found");
			}

		} catch (TDCServiceException ex) {
			staffDetailResponse.setStatusCode(StaffDetailConstants.ERROR_CODE_500);
			staffDetailResponse.setStatusMessage("Error occurred while fetching staff details");
			ErrorMessage error = new ErrorMessage();
			error.setErrorCode(StaffDetailConstants.ERROR_CODE_500);
			error.setErrorMessage(StaffDetailConstants.ERROR_CODE_500_MSG);
			staffDetailResponse.setError(error);
			LOGGER.error("Error occurred while fetching staff details", ex);
		}
		return staffDetailResponse;
	}

	/**
	 * Get The Employee Based on Company Name
	 * 
	 * @return -> staffDetails
	 * @Parrams -> string value
	 * @Throws -> TDCServiceException
	 *
	 */

	@GetMapping("/company/{companyName}")
	@Operation(summary = "Fetch staff detail by Company Name", description = "fetches all staff detail entities and their data from data source by companyName")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "successful operation") })
	public StaffDetailResponse getStaffDetailByCompanyName(@PathVariable String companyName)
			throws TDCServiceException {
		StaffDetailResponse staffDetailResponse = new StaffDetailResponse();
		try {
			List<StaffDetailDto> staffDetResponse = staffDetailService.getStaffByCompanyName(companyName);
			if (staffDetResponse != null && !staffDetResponse.isEmpty()) {
				staffDetailResponse.setStatusCode("200");
				staffDetailResponse.setDataList(staffDetResponse);
				staffDetailResponse.setStatusMessage("Staff Detail value retrieved");
				LOGGER.info("Staff Detail data pulled successfully");
			} else {
				staffDetailResponse.setStatusCode(StaffDetailConstants.ERROR_CODE_404);
				staffDetailResponse.setStatusMessage(StaffDetailConstants.ERROR_CODE_404_MSG);
				LOGGER.info("Staff Detail data not found");
			}

		} catch (TDCServiceException ex) {
			staffDetailResponse.setStatusCode(StaffDetailConstants.ERROR_CODE_500);
			staffDetailResponse.setStatusMessage("Error occurred while fetching staff details");
			ErrorMessage error = new ErrorMessage();
			error.setErrorCode(StaffDetailConstants.ERROR_CODE_500);
			error.setErrorMessage(StaffDetailConstants.ERROR_CODE_500_MSG);
			staffDetailResponse.setError(error);
			LOGGER.error("Error occurred while fetching staff details", ex);
		}
		return staffDetailResponse;
	}

	/**
	 * Delete Staff Detail based on the staffDetailId
	 * 
	 * @param ids - to delete staff information based on the staffDetailId
	 * @return the TDC response
	 * @throws TDCServiceException will thrown if anything went wrong
	 */

	@DeleteMapping("/detail/{staffDetailId}")
	@Operation(summary = "Delete staff details by staffDetailId", description = "Deletes staff details entities from the data source by their IDs")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "successful operation"),
			@ApiResponse(responseCode = "404", description = "some staff details not found") })
	public StaffDetailResponse deleteByStaffDetailsId(@PathVariable String staffDetailId) throws TDCServiceException {
		StaffDetailResponse staffDetailResponse = new StaffDetailResponse();
		try {
			Integer id = Integer.parseInt(staffDetailId);
			System.out.println("StaffId: " + id);
			boolean isDeletedStaffId = staffDetailService.deleteByStaffDetailId(id);
			System.out.println("Staff ID Deleted: " + isDeletedStaffId);
			if (isDeletedStaffId) {
				staffDetailResponse.setStatusCode(StaffDetailConstants.STATUS_200);
				staffDetailResponse.setStatusMessage(StaffDetailConstants.MESSAGE_200);
				LOGGER.info("Staff details deleted successfully");
			} else {

				staffDetailResponse.setStatusCode(StaffDetailConstants.ERROR_CODE_404);
				staffDetailResponse.setStatusMessage(StaffDetailConstants.ERROR_CODE_404_MSG);
				LOGGER.info("Staff details id does not exist");
			}

		} catch (TDCServiceException ex) {
			staffDetailResponse.setStatusCode(StaffDetailConstants.ERROR_CODE_500);
			staffDetailResponse.setStatusMessage("Error occurred while deleting staff details");
			ErrorMessage error = new ErrorMessage();
			error.setErrorCode(StaffDetailConstants.ERROR_CODE_500);
			error.setErrorMessage(StaffDetailConstants.ERROR_CODE_500_MSG);
			staffDetailResponse.setError(error);
			LOGGER.error("Error occurred while deleting staff details", ex);
		}
		return staffDetailResponse;
	}

}
