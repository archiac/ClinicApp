export interface IDoctor {
  id?: number;
  name?: string;
  surname?: string;
  patronymic?: string;
  phone?: string;
  tickets?: number;
  specialtyName?: string;
  specialtyId?: number;
}

export class Doctor implements IDoctor {
  constructor(
    public id?: number,
    public name?: string,
    public surname?: string,
    public patronymic?: string,
    public phone?: string,
    public tickets?: number,
    public specialtyName?: string,
    public specialtyId?: number
  ) {}
}
