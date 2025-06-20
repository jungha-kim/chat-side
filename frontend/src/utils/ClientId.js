import { v4 as uuidv4 } from 'uuid';

export function getClientId() {
  let id = localStorage.getItem('clientId');
  if (!id) {
    id = uuidv4();
    localStorage.setItem('clientId', id);
  }
  return id;
}