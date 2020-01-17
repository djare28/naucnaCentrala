import { Component, OnInit } from '@angular/core';
import { AuthenticationService } from '../services/authentication.service';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { EndpointsService } from '../services/endpoints.service';
import { DataService } from '../services/data.service';

@Component({
  selector: 'app-side-bar',
  templateUrl: './side-bar.component.html',
  styleUrls: ['./side-bar.component.scss']
})
export class SideBarComponent implements OnInit {

  currentUser$: Observable<any>;

  constructor(
    private authenticationService: AuthenticationService, 
    private router: Router, 
    private data:DataService) { 
      this.currentUser$=this.authenticationService.currentUser;  
  }

  ngOnInit() {


    this.currentUser$.subscribe(res => {
      var user = this.authenticationService.currentUserValue;
    });
  }

  logout() {
    this.authenticationService.logout();
    this.router.navigate(['/login']);
}
}
