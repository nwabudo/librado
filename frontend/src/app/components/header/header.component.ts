import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { TokenStorageService } from 'src/app/services/token-storage.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {
  isLoggedIn = false;
  email: string = "";
  firstName: string = "";

  constructor(
    private tokenStorageService: TokenStorageService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.isLoggedIn = this.tokenStorageService.isLoggedIn();

    if (this.isLoggedIn) {
      const user = this.tokenStorageService.getUser();
      this.firstName = user.firstName;
      this.email = user.email;
    }
  }

  logout() {
    this.tokenStorageService.signOut();
    window.location.reload();
  }

}
