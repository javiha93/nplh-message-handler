

export interface PatientInfo {
  code: string;
  firstName: string;
  middleName: string;
  lastName: string;
  sex: string;
  dateOfBirth: string;
}

export interface ContactInfo {
  address: string;
  city: string;
  country: string;
  state: string;
  zip: string;
  homePhone: string;
  workPhone: string;
  mobile: string;
  email: string;
}

export interface PersonInfo extends ContactInfo {
  code: string;
  lastName: string;
  firstName: string;
  middleName: string;
}

export interface Patient extends PersonInfo {
  suffix: string;
  secondSurname: string;
  dateOfBirth: string;
  sex: string;
  orders: OrderList;
}

export interface Physician extends PersonInfo {
  suffix: string;
  prefix: string;
}

export interface Pathologist extends PersonInfo {
  suffix: string;
  prefix: string;
}

export interface Technician {
   code?: string;
   firstName?: string;
   lastName?: string;
   middleName?: string;
 }

export interface OrderList {
  orderList: Order[];
}

export interface Order {
  entityName: string;
     registerDate?: string;
     actionCode?: string;
     status?: string;
     sampleId: string;
     extSampleId?: string;
     prefix?: string;
     originCode?: string;
     originDescription?: string;
     workFlow?: string;
     stat?: string;
     tags?: string;
     pathologist?: Pathologist;
     technician?: Technician;
     specimens?: SpecimensList;
}

export interface SpecimensList {
   specimenList: Specimen[];
 }

 export interface Specimen {
    id: string;
    sequence: string;
    externalId?: string;
    entityName: string;
    collectDateTime?: string;
    receivedDateTime?: string;
    procedure?: Procedure;
    blocks?: BlocksList;
  }

export interface MessageType {
  id: string;
  name: string;
}

export interface MessageHeader {
  sendingApplication?: string;
  sendingFacility?: string;
  receivingApplication?: string;
  receivingFacility?: string;
  messageDateTime?: string;
  messageType?: string;
  messageEvent?: string;
  messageControlId?: string;
  processingId?: string;
  versionId?: string;
}

export interface Message {
  header: MessageHeader;
  patient: Patient;
  physician: Physician;
  pathologist?: Pathologist;
  // Add other properties as needed
}

