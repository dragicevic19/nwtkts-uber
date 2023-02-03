import { Component, Input } from '@angular/core';
import { ChartGroup } from '../../models/ChartGroup';

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
  showXAxis = true;
  showYAxis = true;
  gradient = true;
  showLegend = true;
  showXAxisLabel = true;
  xAxisLabel = 'Dates';
  showYAxisLabel = true;
  yAxisLabel = 'Values';
  legendTitle = 'Date';

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
}
