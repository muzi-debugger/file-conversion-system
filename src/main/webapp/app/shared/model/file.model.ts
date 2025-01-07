import dayjs from 'dayjs';

export interface IFile {
  id?: number;
  fileName?: string;
  fileType?: string;
  lastModified?: dayjs.Dayjs;
  converted?: boolean | null;
  s3Url?: string | null;
  category?: string;
}

export const defaultValue: Readonly<IFile> = {
  converted: false,
};
