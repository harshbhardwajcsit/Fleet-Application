package com.fleet.common.mapper;

import com.fleet.common.dto.ShiftDto;
import com.fleet.common.entity.Shift;

public class ShiftMapper {

    public static ShiftDto toDto(Shift shift) {
        if (shift == null) return null;

        ShiftDto dto = new ShiftDto();
        dto.setId(shift.getId());
        dto.setStartTime(shift.getStartTime());
        dto.setEndTime(shift.getEndTime());
        dto.setDriverId(shift.getDriver().getId());
        dto.setCreatedAt(shift.getCreatedAt());
        dto.setUpdatedAt(shift.getUpdatedAt());
        dto.setActualStartTime(shift.getActualStartTime());
        dto.setActualEndTime(shift.getActualEndTime());
        dto.setTotalWorkingHours(shift.getTotalWorkingHours());
        dto.setDate(shift.getDate());
        return dto;
    }
}
