package service;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.ConnectException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;

public class LancerServiceDistributeur{
    public static void main (String args[]) {
        try {
//Déclarer l'objet
            Distributeur o = new Distributeur();
//Transformer l'objet en service
            ServiceDistributeur s = (ServiceDistributeur) UnicastRemoteObject.exportObject(o, 0);
//Récuperer l'annuaire
            Registry reg = LocateRegistry.getRegistry(1099);
//Lier annuaire et service
            reg.rebind("distributeur", s);
        } catch (ConnectException e) {
            System.out.println("L'annuaire n'est pas accessible ou pas lancé");
        } catch (Exception e) {
            System.out.println("Autre Exception :" + e);
        }
    }
}