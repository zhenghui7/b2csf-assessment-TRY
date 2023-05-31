import { Component, OnInit, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { DateTitleId } from '../models';
import { UploadService } from '../upload.service';

@Component({
  selector: 'app-view0',
  templateUrl: './view0.component.html',
  styleUrls: ['./view0.component.css'],
})
export class View0Component implements OnInit {
  allBundles$!: Observable<DateTitleId[]>;

  uploadSvc = inject(UploadService);

  ngOnInit(): void {
    this.allBundles$ = this.uploadSvc.retrieveAll()

  }
}
