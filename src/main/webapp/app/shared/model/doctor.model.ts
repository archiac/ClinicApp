export interface IDoctor {
  id?: number;
  name?: string;
  surname?: string;
  patronymic?: string;
  phone?: string;
}

export class Doctor implements IDoctor {
  constructor(public id?: number, public name?: string, public surname?: string, public patronymic?: string, public phone?: string) {}
}
