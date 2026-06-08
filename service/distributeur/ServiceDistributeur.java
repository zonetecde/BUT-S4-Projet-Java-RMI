package service.distributeur;

import java.rmi.Remote;
import java.rmi.RemoteException;
import service.noeud.ServiceNoeudCalcul;

public interface ServiceDistributeur extends Remote {
    public void enregistrerClient(ServiceNoeudCalcul snc) throws RemoteException;

    public ServiceNoeudCalcul recupererNoeud() throws RemoteException;

    public void supprimerClient(ServiceNoeudCalcul snc) throws RemoteException;

    public void libererNoeud(ServiceNoeudCalcul snc) throws RemoteException;
}
