
import React, { useState, useEffect } from 'react';
import { Patient } from '../../types/MessageType';

interface PatientEditModalProps {
  isOpen: boolean;
  onClose: () => void;
  patientInfo: Patient | null;
  onSave: (updatedInfo: Patient) => void;
}

const PatientEditModal: React.FC<PatientEditModalProps> = ({ 
  isOpen, 
  onClose, 
  patientInfo, 
  onSave 
}) => {
  const [editedInfo, setEditedInfo] = useState<Patient>({
    code: '',
    firstName: '',
    lastName: '',
    middleName: '',
    suffix: '',
    secondSurname: '',
    dateOfBirth: '',
    sex: 'M',
    address: '',
    city: '',
    country: '',
    state: '',
    zip: '',
    homePhone: '',
    workPhone: '',
    mobile: '',
    email: '',
    orders: { orderList: [] }
  });

  useEffect(() => {
    if (patientInfo) {
      setEditedInfo({
        code: patientInfo.code || '',
        firstName: patientInfo.firstName || '',
        lastName: patientInfo.lastName || '',
        middleName: patientInfo.middleName || '',
        suffix: patientInfo.suffix || '',
        secondSurname: patientInfo.secondSurname || '',
        dateOfBirth: patientInfo.dateOfBirth || '',
        sex: patientInfo.sex || 'M',
        address: patientInfo.address || '',
        city: patientInfo.city || '',
        country: patientInfo.country || '',
        state: patientInfo.state || '',
        zip: patientInfo.zip || '',
        homePhone: patientInfo.homePhone || '',
        workPhone: patientInfo.workPhone || '',
        mobile: patientInfo.mobile || '',
        email: patientInfo.email || '',
        orders: patientInfo.orders || { orderList: [] }
      });
    }
  }, [patientInfo]);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = e.target;
    setEditedInfo(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    onSave(editedInfo);
    onClose();
  };

  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div className="bg-white rounded-lg p-6 w-full max-w-md">
        <h2 className="text-xl font-bold mb-4 text-gray-800">Editar Información del Paciente</h2>
        
        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label htmlFor="code" className="block text-sm font-medium text-gray-700 mb-1">
              Código
            </label>
            <input
              type="text"
              id="code"
              name="code"
              value={editedInfo.code}
              onChange={handleChange}
              className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
            />
          </div>
          
          <div>
            <label htmlFor="firstName" className="block text-sm font-medium text-gray-700 mb-1">
              Nombre
            </label>
            <input
              type="text"
              id="firstName"
              name="firstName"
              value={editedInfo.firstName}
              onChange={handleChange}
              className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
            />
          </div>
          
          <div>
            <label htmlFor="middleName" className="block text-sm font-medium text-gray-700 mb-1">
              Segundo Nombre
            </label>
            <input
              type="text"
              id="middleName"
              name="middleName"
              value={editedInfo.middleName}
              onChange={handleChange}
              className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
            />
          </div>
          
          <div>
            <label htmlFor="lastName" className="block text-sm font-medium text-gray-700 mb-1">
              Apellido
            </label>
            <input
              type="text"
              id="lastName"
              name="lastName"
              value={editedInfo.lastName}
              onChange={handleChange}
              className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
            />
          </div>
          
          <div>
            <label htmlFor="sex" className="block text-sm font-medium text-gray-700 mb-1">
              Sexo
            </label>
            <select
              id="sex"
              name="sex"
              value={editedInfo.sex}
              onChange={handleChange}
              className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500 bg-white"
            >
              <option value="M">Masculino</option>
              <option value="F">Femenino</option>
              <option value="O">Otro</option>
            </select>
          </div>
          
          <div>
            <label htmlFor="dateOfBirth" className="block text-sm font-medium text-gray-700 mb-1">
              Fecha de Nacimiento (AAAAMMDD)
            </label>
            <input
              type="text"
              id="dateOfBirth"
              name="dateOfBirth"
              value={editedInfo.dateOfBirth}
              onChange={handleChange}
              placeholder="AAAAMMDD"
              className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
            />
          </div>

          <div className="flex justify-end space-x-3 pt-4">
            <button
              type="button"
              onClick={onClose}
              className="px-4 py-2 border border-gray-300 rounded-md text-gray-700 hover:bg-gray-100 focus:outline-none focus:ring-2 focus:ring-gray-500"
            >
              Cancelar
            </button>
            <button
              type="submit"
              className="px-4 py-2 bg-indigo-600 text-white rounded-md hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-indigo-500"
            >
              Guardar
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default PatientEditModal;
