import { DatePipe } from '@angular/common';
import { Component } from '@angular/core';
import { ChartColumn } from '../../models/ChartColumn';
import { ChartGroup } from '../../models/ChartGroup';
import { ReportDTO } from '../../models/ReportDTO';
import { ReportResponse } from '../../models/ReportResponse';
import { ChartServiceService } from '../../services/chart-service.service';

@Component({
  selector: 'app-chart',
  templateUrl: './chart.component.html',
  styleUrls: ['./chart.component.scss']
})
export class ChartComponent {

  data: ChartGroup[] = [];
  view: [number, number] = [700, 400];

  // options
  showXAxis: boolean = true;
  showYAxis: boolean = true;
  gradient: boolean = true;
  showLegend: boolean = true;
  showXAxisLabel: boolean = true;
  xAxisLabel: string = 'Dates';
  showYAxisLabel: boolean = true;
  yAxisLabel: string = 'Values';
  legendTitle: string = 'Date';

  colorScheme: any = {
    domain: ['#4e31a5',
    '#9c25a7',
    '#3065ab',
    '#57468b',
    '#904497',
    '#46648b',
    '#32118d',
    '#a00fb3',
    '#1052a2',
    '#6e51bd',
    '#b63cc3',
    '#6c97cb',
    '#8671c1',
    '#b455be',
    '#7496c3']
  };

  // saleData = [
  //   { name: "Mobiles", value: 105000 },
  //   { name: "Laptop", value: 55000 },
  //   { name: "AC", value: 15000 },
  //   { name: "Headset", value: 150000 },
  //   { name: "Fridge", value: 20000 }
  // ];

  constructor(private chartService: ChartServiceService) {
    
  }

  ngOnInit() {
    // this.data = [];
    let dto: ReportDTO = new ReportDTO(new Date(2023, 0, 22), new Date(2023, 0, 25));
    this.chartService.geChartData(dto).subscribe({
      next: (res: ReportResponse) => {
        this.data = res.list.map(el => 
          ({"name": el.date, "series": [{"name": "Number of rides", "value": el.numberOfRides}, {"name": "Price", "value": el.price}, {"name": "Distance", "value": el.distance}]})
        );

        // drugi deo ispisa
        // console.log("--------------------------------------------------------------------------------------------------------------");
        // console.log(this.data);
        // console.log("--------------------------------------------------------------------------------------------------------------");

        // jedan deo ispisa
        // console.log("MULTI--------------------------------------------------------------------------------------------------------------");
        // console.log(this.multi);
        // console.log("--------------------------------------------------------------------------------------------------------------");

      },
      error: (err) => {
        console.log(err);
      }
    })
  }

  onSelect(data: any): void {
    console.log('Item clicked', JSON.parse(JSON.stringify(data)));
  }

  onActivate(data: any): void {
    console.log('Activate', JSON.parse(JSON.stringify(data)));
  }

  onDeactivate(data: any): void {
    console.log('Deactivate', JSON.parse(JSON.stringify(data)));
  }

  multi = [
    {
      "name": "Germany",
      "series": [
        {
          "name": "2010",
          "value": 0
        },
        {
          "name": "2011",
          "value": 0
        },
        {
          "name": "2012",
          "value": 0
        }
      ]
    },
    {
      "name": "USA",
      "series": [
        {
          "name": "2010",
          "value": 1
        },
        {
          "name": "2011",
          "value": 150
        },
        {
          "name": "2012",
          "value": 15
        }
      ]
    },
    {
      "name": "France",
      "series": [
        {
          "name": "2010",
          "value": 15.5
        },
        {
          "name": "2011",
          "value": 20
        },
        {
          "name": "2012",
          "value": 16
        }
      ]
    }
  ];
  
  

}
