import { Component, OnInit } from '@angular/core';
import { EndpointsService } from 'src/app/services/endpoints.service';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { DataService } from 'src/app/services/data.service';

@Component({
  selector: 'app-admin-tasks',
  templateUrl: './admin-tasks.component.html',
  styleUrls: ['./admin-tasks.component.scss']
})
export class AdminTasksComponent implements OnInit {

  private tasks = [];
  
  constructor(
    private endpoints:EndpointsService,
    private authenticationService: AuthenticationService,
    private data:DataService) { }

  ngOnInit() {

    this.endpoints.getTasks(this.authenticationService.currentUserValue.username).subscribe(
      res => {
          this.tasks = res;
        },
      err => {
        console.log(err); 
      }
    )

  }

  aktivacijaCasopisa(taskId,answer,index){
    var body = {
      answer:answer,
      taskId:taskId
    }
    
    this.endpoints.aktivacijaCaspisa(body).subscribe(
      res => {
        this.tasks.splice(index,1);
      },
      err => {
        console.log(err); 
      }
    )
  }

  potvrdaRecenzenta(taskId,answer,index){
    
    var body = {
      taskId:taskId,
      answer:answer
    }
    
    this.endpoints.potvrdaRecenzenta(body).subscribe(
      res => {
        this.tasks.splice(index,1);
      },
      err => {
        console.log(err); 
      }
    )
  }

}
