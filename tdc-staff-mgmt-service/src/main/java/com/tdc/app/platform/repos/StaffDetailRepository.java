package com.tdc.app.platform.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.tdc.app.platform.entities.StaffDetail;

import jakarta.transaction.Transactional;

public interface StaffDetailRepository extends JpaRepository<StaffDetail, Integer> {

	@Transactional
	@Modifying
    @Query("DELETE FROM StaffDetail s WHERE s.staffId IN :staffIds")
	void deleteByStaffIdIn(List<Integer> staffIds);
	// @Query("SELECT sd FROM StaffDetail sd WHERE sd.date = :date")
	// List<StaffDetail> findByDate(@Param("date") LocalDate date);

}
