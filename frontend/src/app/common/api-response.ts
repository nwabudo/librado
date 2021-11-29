export class ApiResponse {
  status: boolean;
  message: string;
  data: any;

  constructor(status: boolean, message: string, data: any){
    this.status = status;
    this.message = message;
    this.data = data;
  }
}
