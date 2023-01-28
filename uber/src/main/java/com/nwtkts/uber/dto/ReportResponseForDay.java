package com.nwtkts.uber.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportResponseForDay {

    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate date;
    private Integer numberOfRides;
    private Double price;
    private Double distance;    // distance is in Kilometers

}
