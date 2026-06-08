package service;

import java.util.ArrayList;
import java.rmi.RemoteException;

public class Distributeur implements ServiceDistributeur{
    ArrayList<ServiceNoeudCalcul> services  = new ArrayList<>() ;

    public void enregistrerClient(ServiceNoeudCalcul c) {
        services.add(c) ;
    }

    public ArrayList<ServiceNoeudCalcul> envoyerClients() {
        return services ;
    }

}

