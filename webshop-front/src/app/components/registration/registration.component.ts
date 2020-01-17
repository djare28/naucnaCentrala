import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { EndpointsService } from 'src/app/services/endpoints.service'
import { Router } from '@angular/router';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.scss']
})
export class RegistrationComponent implements OnInit {

  private formFieldsDto = null;
  private formFields = [];

  private formModel = [
    {
      fieldId:"ime",
      fieldValue:""
    },
    {
      fieldId:"prezime",
      fieldValue:""
    },
    {
      fieldId:"grad",
      fieldValue:""
    },
    {
      fieldId:"drzava",
      fieldValue:""
    },
    {
      fieldId:"titula",
      fieldValue:""
    },
    {
      fieldId:"email",
      fieldValue:""
    },
    {
      fieldId:"username",
      fieldValue:""
    },
    {
      fieldId:"password",
      fieldValue:""
    },
    {
      fieldId:"recenzent",
      fieldValue:false
    },
    {
      fieldId:"naucnaOblast1",
      fieldValue:null
    }
  ]

  private processInstance = "";
  private enumValues = [];
  private taskId = "";
  private listaOblasti=[];
  private hidden=false;
  private oblasti=["Naučna oblast 1"];

  constructor(private endpoints:EndpointsService,private router: Router) {

    let x = endpoints.startProcess();

    x.subscribe(
      res => {
        console.log(res);
        //this.categories = res;
        this.taskId = res.taskId;
        this.formFieldsDto = res;
        this.formFields = res.formFields;
        this.processInstance = res.processInstanceId;
        this.formFields.forEach( (field) =>{
          
          if( field.type.name=='enum'){
            this.enumValues = Object.keys(field.type.values);
          }
        });
      },
      err => {
        console.log("Error occured");
      }
    );

   }

  ngOnInit() {
    this.getOblasti();
  }

  addOblast(){

    var index = this.formModel.length - 8;

    var oblast = {
      fieldValue:null,
      fieldId:"naucnaOblast"+index
    }

    this.formModel.push(oblast);
    this.oblasti.push("Naučna oblast "+index);
  }

  getOblasti(){
    this.endpoints.getOblasti().subscribe(
      res => {
          this.listaOblasti=res;
      },
      err => {
        alert("An error has occured while getting science fields");
        console.log(err); 
      }
    )
  }


  onSubmit(value, form){
    this.hidden=true;
    if(form.valid===true){
      console.log(form.value);
      let o = new Array();
      for (var property in value) {
        console.log(property);
        console.log(value[property]);
        o.push({fieldId : property, fieldValue : value[property]});
      }
      console.log(this.taskId);
      this.endpoints.submitRegistration(o,this.taskId).subscribe(
        res => {
            console.log(res);
            this.endpoints.getValidation(res).subscribe(
              res => {
                  if(res==true){
                    this.router.navigate(['/validated']);
                  }else{
                    alert("Forma nije validna");
                    this.hidden=false;
                  }
              },
              err => {
                alert("An error has occured while getting validation");
                console.log(err); 
                this.hidden=false;
              }
            )
        },
        err => {
          alert("An error has occured while registrating");
          console.log(err); 
          this.hidden=false;
        }
      )
    }
  }

}
