package service;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ServiceDistributeur extends Remote {
    public void enregistrerClient(ServiceNoeudCalcul snc) throws RemoteException;

    public ArrayList<ServiceNoeudCalcul> envoyerClients() throws RemoteException;

    public void supprimerClient(ServiceNoeudCalcul snc) throws RemoteException;

}
