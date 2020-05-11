export interface ISpecialty {
  id?: number;
  name?: string;
}

export class Specialty implements ISpecialty {
  constructor(public id?: number, public name?: string) {}
}
