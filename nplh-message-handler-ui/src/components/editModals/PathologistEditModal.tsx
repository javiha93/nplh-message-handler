
import React, { useState, useEffect } from 'react';
import { Pathologist } from '../types/MessageType';

interface PathologistEditModalProps {
  isOpen: boolean;
  onClose: () => void;
  pathologistInfo: Pathologist | null;
  onSave: (updatedInfo: Pathologist) => void;
}

const PathologistEditModal: React.FC<PathologistEditModalProps> = ({ 
  isOpen, 
  onClose, 
  pathologistInfo, 
  onSave 
}) => {
  const [editedInfo, setEditedInfo] = useState<Pathologist>({
    code: '',
    firstName: '',
    lastName: '',
    middleName: '',
    prefix: '',
    suffix: '',
    address: '',
    city: '',
    country: '',
    state: '',
    zip: '',
    homePhone: '',
    workPhone: '',
    mobile: '',
    email: ''
  });

  useEffect(() => {
    if (pathologistInfo) {
      setEditedInfo({
        code: pathologistInfo.code || '',
        firstName: pathologistInfo.firstName || '',
        lastName: pathologistInfo.lastName || '',
        middleName: pathologistInfo.middleName || '',
        prefix: pathologistInfo.prefix || '',
        suffix: pathologistInfo.suffix || '',
        address: pathologistInfo.address || '',
        city: pathologistInfo.city || '',
        country: pathologistInfo.country || '',
        state: pathologistInfo.state || '',
        zip: pathologistInfo.zip || '',
        homePhone: pathologistInfo.homePhone || '',
        workPhone: pathologistInfo.workPhone || '',
        mobile: pathologistInfo.mobile || '',
        email: pathologistInfo.email || ''
      });
    }
  }, [pathologistInfo]);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
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
        <h2 className="text-xl font-bold mb-4 text-gray-800">Editar Información del Patólogo</h2>
        
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
            <label htmlFor="prefix" className="block text-sm font-medium text-gray-700 mb-1">
              Prefijo
            </label>
            <input
              type="text"
              id="prefix"
              name="prefix"
              value={editedInfo.prefix}
              onChange={handleChange}
              className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500"
            />
          </div>
          
          <div>
            <label htmlFor="suffix" className="block text-sm font-medium text-gray-700 mb-1">
              Sufijo
            </label>
            <input
              type="text"
              id="suffix"
              name="suffix"
              value={editedInfo.suffix}
              onChange={handleChange}
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

export default PathologistEditModal;
