package service;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServiceDistributeur extends Remote {
    public void enregistrerClient(ServiceNoeudCalcul snc) throws RemoteException;

    public ServiceNoeudCalcul recupererNoeud() throws RemoteException;

    public void supprimerClient(ServiceNoeudCalcul snc) throws RemoteException;

}
