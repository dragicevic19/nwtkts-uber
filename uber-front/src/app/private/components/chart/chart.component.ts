import { Component, Input } from '@angular/core';
import { ChartGroup } from '../../models/ChartGroup';
import { ChartServiceService } from '../../services/chart-service.service';

@Component({
  selector: 'app-chart',
  templateUrl: './chart.component.html',
  styleUrls: ['./chart.component.scss']
})
export class ChartComponent {

  @Input()
  data: ChartGroup[] = [];
  view: [number, number] = [1000, 600];

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


  constructor(private chartService: ChartServiceService) {
    
  }

  ngOnInit() {
   
  }

  onSelect(data: any): void {
  }

  onActivate(data: any): void {
  }

  onDeactivate(data: any): void {
  }  

}
