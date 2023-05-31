import { Component, Input, OnInit, inject } from '@angular/core';
import { UploadService } from '../upload.service';
import { Observable } from 'rxjs';
import { RetrievedData } from '../models';

@Component({
  selector: 'app-view2',
  templateUrl: './view2.component.html',
  styleUrls: ['./view2.component.css'],
})
export class View2Component implements OnInit {
  @Input()
  bundleId!: String;

  bundle$!: Observable<RetrievedData>;
  urls: string[] = [];

  uploadSvc = inject(UploadService);

  ngOnInit(): void {
    this.bundle$ = this.uploadSvc.retrieve(this.bundleId);

    this.bundle$.subscribe((d) => {
      const formattedUrls = d.urls.substring(1, d.urls.length - 1);
      this.urls = formattedUrls.split(',').map((url) => url.trim());
    });
  }
}
