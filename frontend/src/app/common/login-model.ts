export class LoginModel {
  email: string;

  constructor(email: string){
    this.email = email;
  }
}


export class CreationModel {
  email: string;
  firstName: string;
  lastName: string;

  constructor(email: string, firstName: string, lastName: string){
    this.email = email;
    this.firstName = firstName;
    this.lastName = lastName;
  }
}