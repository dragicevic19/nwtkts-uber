package com.nwtkts.uber.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportResponse {

    private List<ReportResponseForDay> list;

    private Double cumulativeSumOfNumberOfRides;
    private Double cumulativeSumOfPrice;
    private Double cumulativeSumOfDistance;

    private Double averageNumberOfRides;
    private Double averagePrice;
    private Double averageDistance;

}
