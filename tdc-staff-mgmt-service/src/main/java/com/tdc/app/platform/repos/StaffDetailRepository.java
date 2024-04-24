package com.tdc.app.platform.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tdc.app.platform.entities.StaffDetail;

import jakarta.transaction.Transactional;

public interface StaffDetailRepository extends JpaRepository<StaffDetail, Integer> {
	
	@Query("SELECT s FROM StaffDetail s WHERE s.staffDetailId = :staffDetailId")
	StaffDetail getByStaffDetailId(@Param("staffDetailId") int staffDetailId);

	@Query("SELECT s FROM StaffDetail s WHERE s.staffId = :staffId")
	StaffDetail getByStaffId(@Param("staffId") int staffId);

	/*
	 * @Transactional
	 * 
	 * @Modifying
	 * 
	 * @Query("DELETE FROM StaffDetail s WHERE s.staffId IN :staffId") void
	 * deleteByStaffId(Integer staffId);
	 */
	@Transactional
	@Modifying
	@Query("DELETE FROM StaffDetail s WHERE s.staffDetailId IN :staffDetailId")
	void deleteByStaffDetailId(Integer staffDetailId);

	@Query("from StaffDetail s where s.experience=:exp")
	List<StaffDetail> findByExperience(@Param("exp") int experience);
	
	@Query("from StaffDetail s where s.companyName=:company")
	List<StaffDetail> findByCompanyName(@Param("company") String companyName);

}
