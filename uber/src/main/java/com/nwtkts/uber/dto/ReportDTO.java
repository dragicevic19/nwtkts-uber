package com.nwtkts.uber.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReportDTO {

    //@JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startDate;

    //@JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endDate;

    private Long userId;

}
