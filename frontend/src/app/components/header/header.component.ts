import { Component, OnInit } from '@angular/core';
import { NavigationEnd, NavigationStart, Router } from '@angular/router';
import { TokenStorageService } from 'src/app/services/token-storage.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {
  isLoggedIn = false;

  constructor(
    private tokenStorageService: TokenStorageService,
    private router: Router,
  ) {
    this.router.events.subscribe((val) => {
      if (val instanceof NavigationStart) {
        this.isLoggedIn = !!this.tokenStorageService.getToken();
      }
    });
  }

  ngOnInit(): void {
  }

  logout() {
    this.tokenStorageService.signOut();
    this.reloadComponent();
  }

  reloadComponent() {
    this.router.navigateByUrl('/', { skipLocationChange: true }).then(() => {
      this.router.navigate(['/home']);
    });
  }

}
