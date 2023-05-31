export interface UploadData {
  name: string;
  title: string;
  comment: string;
}

export interface BundleId {
  bundleId: string;
}

export interface RetrievedData {
  title: string;
  name: string;
  date: string;
  comments: string;
  urls: string;
}

export interface DateTitleId {
  title: string;
  date: string;
  bundleId: string;
}
