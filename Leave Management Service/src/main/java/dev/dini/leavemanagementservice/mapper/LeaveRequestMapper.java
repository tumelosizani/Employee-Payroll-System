package dev.dini.leavemanagementservice.mapper;

import dev.dini.leavemanagementservice.dto.LeaveRequestDTO;
import dev.dini.leavemanagementservice.dto.LeaveResponseDTO;
import dev.dini.leavemanagementservice.leave.LeaveRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface LeaveRequestMapper {

    LeaveRequestMapper INSTANCE = Mappers.getMapper(LeaveRequestMapper.class);

    @Mapping(target = "employeeId", source = "leaveRequest.employeeId")
    LeaveResponseDTO toResponseDTO(LeaveRequest leaveRequest);

    @Mapping(target = "employeeId", source = "leaveRequestDTO.employeeId")
    LeaveRequest toEntity(LeaveRequestDTO leaveRequestDTO);

    @Mapping(target = "employeeId", source = "leaveRequest.employeeId")
    LeaveRequestDTO toRequestDTO(LeaveRequest leaveRequest);


}
