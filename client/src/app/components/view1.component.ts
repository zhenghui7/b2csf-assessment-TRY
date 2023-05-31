import { Component, ElementRef, OnInit, ViewChild, inject } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { firstValueFrom } from 'rxjs';
import { BundleId, UploadData } from 'src/app/models';
import { UploadService } from 'src/app/upload.service';

@Component({
  selector: 'app-view1',
  templateUrl: './view1.component.html',
  styleUrls: ['./view1.component.css']
})
export class View1Component implements OnInit {

  @ViewChild('uploadFile')
  uploadFile!: ElementRef
  
  form!: FormGroup
  fb = inject(FormBuilder)

  uploadSvc = inject(UploadService)
  router = inject(Router)

  ngOnInit(): void {
    this.form = this.fb.group({
      name: this.fb.control<string>('', [ Validators.required, Validators.minLength(3)] ),
      title: this.fb.control<string>('', [ Validators.required, Validators.minLength(3)] ),
      comment: this.fb.control<string>(''),
      file: this.fb.control<File | null>(null, [ Validators.required] )
    })
  }


  upload() {
    const f: File = this.uploadFile.nativeElement.files[0]
    const data: UploadData = this.form.value

    this.uploadSvc.upload(data, f)  //need to return an observable first, if not http request wont be sent
    .then( (b: BundleId) => {
      alert('uploaded')
      console.info(">>>>>", b.bundleId)
      this.router.navigate(['/view2', b.bundleId])
    })
    .catch(err => {
      alert(JSON.stringify(err))
    })
  }
  
}
