/**
 *
 *
 */

package io.mosip.kernel.masterdata.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import io.mosip.kernel.masterdata.constant.MachineHistoryErrorCode;
import io.mosip.kernel.masterdata.dto.MachineHistoryDto;
import io.mosip.kernel.masterdata.dto.MachineHistoryResponseDto;
import io.mosip.kernel.masterdata.entity.MachineHistory;
import io.mosip.kernel.masterdata.exception.DataNotFoundException;
import io.mosip.kernel.masterdata.exception.MasterDataServiceException;
import io.mosip.kernel.masterdata.repository.MachineHistoryRepository;
import io.mosip.kernel.masterdata.service.MachineHistoryService;
import io.mosip.kernel.masterdata.utils.ObjectMapperUtil;
import io.mosip.kernel.masterdata.utils.StringToLocalDateTimeConverter;

/**
 * This class have methods to fetch a Machine History Details
 * 
 * @author Megha Tanga
 * @since 1.0.0
 *
 */
@Service
public class MachineHistoryServiceImpl implements MachineHistoryService {

	/**
	 * Field to hold Machine History Repository object
	 */
	@Autowired
	MachineHistoryRepository macRepo;

	/**
	 * Field to hold ModelMapper object
	 */
	

	/**
	 * Field to hold ObjectMapperUtil object
	 */
	@Autowired
	ObjectMapperUtil objMapper;

	/**
	 * Field to hold stringToLocalDateTimeConverter object
	 * 
	 */
	@Autowired
	StringToLocalDateTimeConverter stringToLocalDateTimeConverter;

	/**
	 * Method used for retrieving Machine history details based on given Machine ID
	 * Language code and effective date and time in LocalDateTime formate
	 * 
	 * @param id
	 *            pass Machine ID as String
	 * 
	 * @param langCode
	 *            pass Language code as String
	 * 
	 * @param effDtime
	 *            pass effDtime as String
	 * 
	 * @return MachineHistoryDto returning the Machine History Detail for the given
	 *         Machine ID and Language code
	 * 
	 * @throws MachineDetailFetchException
	 *             While Fetching Machine History Detail If fails to fetch required
	 *             Machine Detail
	 * 
	 * 
	 * @throws MachineHistroyNotFoundException
	 *             If given required Machine ID, language code or effective date
	 *             time not found
	 * 
	 */
	@Override
	public MachineHistoryResponseDto getMachineHistroyIdLangEffDTime(String id, String langCode, String effDtime) {

		LocalDateTime lDateAndTime = stringToLocalDateTimeConverter.convert(effDtime);

		List<MachineHistory> macHistoryList = null;
		List<MachineHistoryDto> machineHistoryDtoList = null;
		MachineHistoryResponseDto machineHistoryResponseDto = new MachineHistoryResponseDto();
		try {
			macHistoryList = macRepo.findByIdAndLangCodeAndEffectDtimesLessThanEqualAndIsActiveTrueAndIsDeletedFalse(id,
					langCode, lDateAndTime);
		} catch (DataAccessException dataAccessLayerException) {
			throw new MasterDataServiceException(MachineHistoryErrorCode.MACHINE_HISTORY_FETCH_EXCEPTION.getErrorCode(),
					MachineHistoryErrorCode.MACHINE_HISTORY_FETCH_EXCEPTION.getErrorMessage());
		}
		if (macHistoryList != null && !macHistoryList.isEmpty()) {
			machineHistoryDtoList = objMapper.mapAll(macHistoryList, MachineHistoryDto.class);
		} else {
			throw new DataNotFoundException(MachineHistoryErrorCode.MACHINE_HISTORY_NOT_FOUND_EXCEPTION.getErrorCode(),
					MachineHistoryErrorCode.MACHINE_HISTORY_NOT_FOUND_EXCEPTION.getErrorMessage());
		}
		machineHistoryResponseDto.setMachineHistoryDetails(machineHistoryDtoList);
		return machineHistoryResponseDto;
	}
}
