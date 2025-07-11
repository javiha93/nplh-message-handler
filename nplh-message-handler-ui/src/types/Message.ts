export interface SupplementalInfo {
  type: string;
  value: string;
  artifact?: string;
  qualityIssueType?: string;
  qualityIssueValue?: string;
  optionalType?: string;
  optionalValue?: string;
}

export interface SupplementalInfoList {
  supplementalInfoList: SupplementalInfo[];
}

export interface Reagent {
  expirationDateTime?: string;
  intendedUseFlag?: string;
  lotNumber?: string;
  lotSerialNumber?: string;
  catalogNumber?: string;
  manufacturer?: string;
  receivedDateTime?: string;
  substanceName: string;
  substanceOtherName?: string;
  substanceType?: string;
}

export interface ReagentsList {
  reagentList: Reagent[];
}

export interface Control {
  name?: string;
  description?: string;
  scoring?: string;
  clone?: string;
  vendor?: string;
}

export interface StainProtocol {
  number?: string;
  name?: string;
  identifier?: string;
  description?: string;
}

export interface Slide {
  id: string;
  sequence: string;
  externalId?: string;
  entityName: string;
  actionCode?: string;
  stainProtocol?: StainProtocol;
  control?: Control;
  comment?: string;
  labelPrinted?: string;
  isRescanned?: boolean;
  rescanReason?: string;
  rescanComment?: string;
  supplementalInfos?: SupplementalInfoList;
  reagents?: Reagent[]; // Changed to direct array to match backend
}

export interface SlidesList {
  slideList: Slide[];
}

export interface Block {
  id: string;
  sequence: string;
  externalId?: string;
  entityName: string;
  parentId?: string;
  slides?: SlidesList;
  supplementalInfos?: SupplementalInfoList;
}

export interface BlocksList {
  blockList: Block[];
}

export interface Procedure {
  [key: string]: any;
  tissue?: {
    type?: string;
    description?: string;
    subtype?: string;
    subtypeDescription?: string;
  };
  surgical?: {
    name?: string;
    description?: string;
  };
  anatomic?: {
    site?: string;
    description?: string;
  };
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
  supplementalInfos?: {
    supplementalInfoList: any[];
  };
}

export interface SpecimensList {
  specimenList: Specimen[];
}

export interface Technician {
  code?: string;
  firstName?: string;
  lastName?: string;
  middleName?: string;
}

export interface Pathologist {
  code?: string;
  firstName?: string;
  lastName?: string;
  middleName?: string;
  prefix?: string;
  suffix?: string;
  address?: string;
  city?: string;
  state?: string;
  zip?: string;
  country?: string;
  homePhone?: string;
  workPhone?: string;
  mobile?: string;
  email?: string;
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

export interface Patient {
  code?: string;
  firstName?: string;
  lastName?: string;
  middleName?: string;
  secondSurname?: string;
  dateOfBirth?: string;
  sex?: string;
  address1?: string;
  city?: string;
  state?: string;
  zip?: string;
  country?: string;
  homeTel?: string;
  workTel?: string;
  mobileTel?: string;
  email?: string;
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
  channelType?: string;
  registerTime?: string;
  header?: MessageHeader;
  patient?: Patient;
  physician?: Pathologist;
  actionCode?: string;
  error?: string;
}
