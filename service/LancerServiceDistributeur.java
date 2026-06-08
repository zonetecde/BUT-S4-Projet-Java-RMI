package service;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.ConnectException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;

public class LancerServiceDistributeur{
    public static void main (String args[]) {
        try {
            // déclare l'objet partagé
            Distributeur o = new Distributeur();
            // le transforme en service
            ServiceDistributeur s = (ServiceDistributeur) UnicastRemoteObject.exportObject(o, 0);
            // recup l'annuaire
            Registry reg = LocateRegistry.getRegistry(1099);
            // ajoute l'objet dans l'annuaire   
            reg.rebind("distributeur", s);
        } catch (ConnectException e) {
            System.out.println("L'annuaire n'est pas accessible ou pas lancé");
        } catch (Exception e) {
            System.out.println("Autre Exception :" + e);
        }
    }
}