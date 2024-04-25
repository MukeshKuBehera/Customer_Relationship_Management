package com.tdc.app.platform.resource;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tdc.app.platform.dto.StaffRequest;
import com.tdc.app.platform.dto.StaffResponse;
import com.tdc.app.platform.exception.ErrorMessage;
import com.tdc.app.platform.exception.TDCServiceException;
import com.tdc.app.platform.services.StaffService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api")
@Tag(name = "Staff", description = "The Staff api")
public class StaffResourceController {

	@Autowired
	private StaffService staffService;

	private static final Logger LOGGER = LoggerFactory.getLogger(StaffDetailsResourceController.class);

	/**
	 * Get Staff Detail.
	 *
	 * @param staffId - to get particular designation
	 * @return the TDC response
	 * @throws TDCServiceException will thrown if anything went wrong
	 */
	@GetMapping("/staff/{staffId}")
	@Operation(summary = "Fetch staff by id", description = "fetches all staff entities and their data from data source by id")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "successful operation"),
			@ApiResponse(responseCode = "404", description = "some staff details not found") })
	public StaffResponse getStaff(@PathVariable Integer staffId) throws TDCServiceException {
		StaffResponse staffResponse = new StaffResponse();
		try {
			final StaffRequest staff = staffService.getStaffById(staffId);

			if (staff != null) {
				staffResponse.setStatusCode("200");
				staffResponse.setData(staff);
				staffResponse.setStatusMessage("Staff Detail value retrieved");
				LOGGER.info("Staff Detail data pulled successfully");
			} else {
				staffResponse.setStatusCode("404");
				staffResponse.setStatusMessage("Staff Detail not found");
				LOGGER.info("Staff Detail data not found");
			}

		} catch (TDCServiceException ex) {
			staffResponse.setStatusCode("500");
			ErrorMessage error = new ErrorMessage();
			error.setErrorCode("500");
			error.setErrorMessage("Internal Server Error");
			staffResponse.setError(error);
			LOGGER.error("Staff detail list not found ", ex);

		}
		return staffResponse;
	}

	/**
	 * Get all Staff.
	 *
	 * @return the Staff response
	 * @throws TDCServiceException will thrown if anything went wrong
	 */
	@GetMapping("/staff")
	@Operation(summary = "Fetch all Staff details", description = "fetches all staff entities and their data from data source")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "successful operation"),
			@ApiResponse(responseCode = "404", description = "some staff details not found") })
	public StaffResponse getAllStaffDetails() throws TDCServiceException {
		StaffResponse staffResponse = new StaffResponse();
		try {

			final List<StaffRequest> staffDto = staffService.getAllStaffs();

			if (staffDto != null) {
				staffResponse.setStatusCode("200");
				staffResponse.setDataList(staffDto);
				staffResponse.setStatusMessage("Staff details value retrieved");
				LOGGER.info("Staff details data pulled successfully");
			} else {
				staffResponse.setStatusCode("404");
				staffResponse.setStatusMessage("Staff details not found");
				LOGGER.info("Staff details data not found");
			}

		} catch (Exception ex) {
			staffResponse.setStatusCode("500");
			ErrorMessage error = new ErrorMessage();
			error.setErrorCode("500");
			error.setErrorMessage("Internal Server Error");
			staffResponse.setError(error);
			LOGGER.error("Designation list not found", ex);
		}
		return staffResponse;
	}

	/**
	 * Get Staff Detail.
	 *
	 * @param - to get particular designation
	 * @return the TDC response
	 * @throws TDCServiceException will thrown if anything went wrong
	 */
	@PostMapping("/staff")
	@Operation(summary = "Save or Update Staff Records", description = "Persist/Update staff record in database")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "successful operation") })
	public StaffResponse createUpdateStaff(@RequestHeader("userId") String user, @RequestBody StaffRequest staff)
			throws TDCServiceException {
		LOGGER.info("createUpdateStaff entered and incoming request {}", staff);
		StaffResponse staffResponse = new StaffResponse();

		try {
			final StaffRequest staffDto = staffService.saveUpdateStaff(staff);
			staffResponse.setStatusCode("200");
			staffResponse.setData(staffDto);
			staffResponse.setStatusMessage("Staff record processed successfully");

			LOGGER.info("Staff record processed successfully");

		} catch (TDCServiceException ex) {
			staffResponse.setStatusCode("500");

			ErrorMessage error = new ErrorMessage();
			error.setErrorCode("500");
			error.setErrorMessage("Internal Server Error");
			staffResponse.setError(error);
			LOGGER.error("Staff record not processed {}", ex);

		}
		return staffResponse;
	}

	/**
	 * Get Staff Detail.
	 *
	 * @param staffId - to get particular designation
	 * @return the TDC response
	 * @throws TDCServiceException will thrown if anything went wrong
	 */
	@DeleteMapping("/staff/delete")
	@Operation(summary = "Delete Staff Records", description = "Delete staff record in database")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "successful operation"),
			@ApiResponse(responseCode = "404", description = "some staff details not found") })
	public StaffResponse deleteStaff(@PathVariable int staffId) throws TDCServiceException {

		StaffResponse staffResponse = new StaffResponse();

		try {
			final List<StaffRequest> staffList = staffService.deleteStaff(staffId);
			staffResponse.setStatusCode("205");
			staffResponse.setDataList(staffList);
			staffResponse.setStatusMessage("Staff record deleted successfully");

		} catch (TDCServiceException ex) {
			staffResponse.setStatusCode("500");
			staffResponse.setStatusMessage("Internal Server Error");

			ErrorMessage error = new ErrorMessage();
			error.setErrorCode("500");
			error.setErrorMessage("Internal Server Error");
			staffResponse.setError(error);
			LOGGER.error("Staff record not processed {}", ex);
		}
		return staffResponse;

	}

	/**
	 * Fetch staff details based on phoneNumber
	 * 
	 * @param phoneNumber
	 * @return
	 * @throws ResourceNotFoundException
	 */
	@GetMapping("/staff/phoneNumber")
	@Operation(summary = "Fetch staff by id", description = "Get the Record based on PhoneNumber")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "successFul operation"),
			@ApiResponse(responseCode = "404", description = "some staff details not found") })
	public StaffResponse fetchStaffByPhoneNumber(@RequestParam String phoneNumber) throws TDCServiceException {
		System.out.println("Inside this fetch method");
		StaffResponse staffResponse = new StaffResponse();
//		StaffRequest staffRequest = new StaffRequest();
//		int phone = Integer.parseInt(phoneNumber);
		try {
			List<StaffRequest> staffDetails = staffService.getStaffByPhoneNumber(phoneNumber);

			if (!staffDetails.isEmpty()) {
				staffResponse.setStatusCode("200");
				staffResponse.setDataList(staffDetails);
				staffResponse.setStatusMessage("Staff record Fetch successfully");
				LOGGER.info("Staff record fetch successfully by phoneNumber");

			} else {
				staffResponse.setStatusCode("404");
				staffResponse.setStatusMessage("Staff record not found");

				LOGGER.info("Staff record not found");
			}

		} catch (TDCServiceException ex) {
			staffResponse.setStatusCode("500");
			ErrorMessage error = new ErrorMessage();
			error.setErrorCode("500");
			error.setErrorMessage("Internal Server Error");
			staffResponse.setError(error);
			LOGGER.error("Staff record not processed {}", ex);

		}

		return staffResponse;
	}

	/**
	 * Fetch staff details based on country
	 * 
	 * @param country
	 * @return
	 * @throws ResourceNotFoundException
	 */
	@GetMapping("/staff/country")
	@Operation(summary = "Fetch staff by country", description = "Get the Record based on country ")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "successFul operation"),
			@ApiResponse(responseCode = "404", description = "some staff details not found") })
	public StaffResponse fetchStaffsByCountry(@RequestParam String country) throws TDCServiceException {
		StaffResponse staffResponse = new StaffResponse();
		try {
			List<StaffRequest> staffDetails = staffService.getStaffByCounty(country);
			if (!staffDetails.isEmpty()) {
				staffResponse.setStatusCode("200");
				staffResponse.setDataList(staffDetails);
				staffResponse.setStatusMessage("Staff record Fetch successfully");
				LOGGER.info("Staff record fetch successfully by country");

			} else {
				staffResponse.setStatusCode("404");
				staffResponse.setStatusMessage("Staff record not found");

				LOGGER.info("Staff record not found");
			}

		} catch (TDCServiceException ex) {
			staffResponse.setStatusCode("500");
			ErrorMessage error = new ErrorMessage();
			error.setErrorCode("500");
			error.setErrorMessage("Internal Server Error");
			staffResponse.setError(error);
			LOGGER.error("Staff record not processed {}", ex);

		}

		return staffResponse;
	}

	/**
	 * Fetch staff details based on Gender
	 * 
	 * @param Gender
	 * @return
	 * @throws TDCServiceException
	 */
	@GetMapping("/staff/Gender")
	@Operation(summary = "fetch details by Gender", description = "Fetch the stuff Details based on gender")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "successFul operation"),
			@ApiResponse(responseCode = "404", description = "some staff details not found") })
	public StaffResponse fetchStaffBYGender(@RequestParam String Gender) throws TDCServiceException {
		StaffResponse staffResponse = new StaffResponse();
		try {
			List<StaffRequest> staffDetails = staffService.getStaffByGender(Gender);
			if (!staffDetails.isEmpty()) {
				staffResponse.setStatusCode("200");
				staffResponse.setDataList(staffDetails);
				staffResponse.setStatusMessage("Staff record Fetch successfully");
				LOGGER.info("Staff record fetch successfully by gender");

			} else {
				staffResponse.setStatusCode("404");
				staffResponse.setStatusMessage("Staff record not found");

				LOGGER.info("Staff record not found");
			}

		} catch (Exception ex) {
			staffResponse.setStatusCode("500");
			ErrorMessage error = new ErrorMessage();
			error.setErrorCode("500");
			error.setErrorMessage("Internal Server Error");
			staffResponse.setError(error);
			LOGGER.error("Staff record not processed {}", ex);

		}

		return staffResponse;
	}

	/**
	 * Fetch staff details based on email
	 * 
	 * @param email
	 * @return
	 * @throws TDCServiceException
	 */
	@GetMapping("/staff/email")
	@Operation(summary = "fetch details by email", description = "Fetch the stuff Details based on email")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "successFul operation"),
			@ApiResponse(responseCode = "404", description = "some staff details not found") })
	public StaffResponse fetchStaffBYEmail(@RequestParam String email) throws TDCServiceException {
		StaffResponse staffResponse = new StaffResponse();
		try {
			List<StaffRequest> staffDetails = staffService.getStaffByGmail(email);

			if (!staffDetails.isEmpty()) {
				staffResponse.setStatusCode("200");
				staffResponse.setDataList(staffDetails);
				staffResponse.setStatusMessage("Staff record Fetch successfully");
				LOGGER.info("Staff record fetch successfully by emailId");

			} else {

				staffResponse.setStatusCode("404");
				staffResponse.setStatusMessage("Staff record not found");
				LOGGER.info("Staff record not found");
			}

		} catch (Exception ex) {
			staffResponse.setStatusCode("500");
			ErrorMessage error = new ErrorMessage();
			error.setErrorCode("500");
			error.setErrorMessage("Internal Server Error");
			staffResponse.setError(error);
			System.out.println("Exception :" + ex);
			LOGGER.error("Staff record not processed {}", ex);

		}
		return staffResponse;

	}

	/**
	 * Fetch staff details based on IsActive
	 * 
	 * @param isActive
	 * @return
	 * @throws ResourceNotFoundException
	 */
	@GetMapping("/staff/isActive")
	@Operation(summary = "fetch details by Gender", description = "Fetch the stuff Details based on gender")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "successFul operation"),
			@ApiResponse(responseCode = "404", description = "some staff details not found") })
	public StaffResponse fetchStaffByActiveStatus(@RequestParam Boolean isActive) throws TDCServiceException {
		StaffResponse staffResponse = new StaffResponse();
		try {
			List<StaffRequest> staffDetails = staffService.getStaffByIsActive(isActive);
			if (!staffDetails.isEmpty()) {
				staffResponse.setStatusCode("200");
				staffResponse.setDataList(staffDetails);
				staffResponse.setStatusMessage("Staff record Fetch successfully");
				LOGGER.info("Staff record Fetch successfully");
			} else {
				staffResponse.setStatusCode("404");
				staffResponse.setStatusMessage("Staff record not found");
				LOGGER.info("Staff record not found");
			}
			return staffResponse;
		} catch (TDCServiceException ex) {
			staffResponse.setStatusCode("500");
			ErrorMessage error = new ErrorMessage();
			error.setErrorCode("500");
			error.setErrorMessage("Internal Server Error");
			staffResponse.setError(error);
			LOGGER.error("Staff record not processed {}", ex);

		}
		return staffResponse;

	}

	/**
	 * Get Staff based on the joining date
	 * 
	 * @param date - to get staff information based on the date
	 * @return the TDC response
	 * @throws TDCServiceException will thrown if anything went wrong
	 */

	@GetMapping("/staff/date")
	@Operation(summary = "Fetch staff by date", description = "Fetches all staff entities and their data from the data source by date")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successful operation"),
			@ApiResponse(responseCode = "404", description = "No content found") })
	public StaffResponse getStaffDetailsByDate(@RequestParam("date") String date) throws TDCServiceException {
		StaffResponse staffResponse = new StaffResponse();
		try {
			List<StaffRequest> staffDetails = staffService.getStaffByDate(date);
			if (!staffDetails.isEmpty()) {
				staffResponse.setStatusCode("200");
				staffResponse.setDataList(staffDetails);
				staffResponse.setStatusMessage("Staff details retrieved successfully");
				LOGGER.info("Staff details data pulled successfully");
			} else {
				staffResponse.setStatusCode("404");
				staffResponse.setStatusMessage("No content found for the given date");
				LOGGER.info("No content found for the given date");
			}
		} catch (TDCServiceException ex) {
			staffResponse.setStatusCode("500");
			ErrorMessage error = new ErrorMessage();
			error.setErrorCode("500");
			error.setErrorMessage("Internal Server Error");
			staffResponse.setError(error);
			LOGGER.error("Failed to fetch staff details by date", ex);
		}
		return staffResponse;
	}

}