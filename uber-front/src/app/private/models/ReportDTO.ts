export class ReportDTO {

  public startDate: Date;
  public endDate: Date;
  public userId: number | null;

  constructor(startDate: Date, endDate: Date, userId: number | null) {
    this.startDate = startDate;
    this.endDate = endDate;
    this.userId = userId;
  }



}
