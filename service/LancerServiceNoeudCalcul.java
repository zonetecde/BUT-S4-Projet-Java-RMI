package service;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.ConnectException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;

public class LancerServiceNoeudCalcul {
    public static void main (String args[]) {
        try {
//Déclarer l'objet
            NoeudCalcul o = new NoeudCalcul();
//Transformer l'objet en service
            ServiceNoeudCalcul s = (ServiceNoeudCalcul) UnicastRemoteObject.exportObject(o, 0);
//Récuperer l'annuaire
            Registry reg = LocateRegistry.getRegistry(1099);
//Lier annuaire et service
            reg.rebind("noeudCalcul", s);
        } catch (ConnectException e) {
            System.out.println("L'annuaire n'est pas accessible ou pas lancé");
        } catch (Exception e) {
            System.out.println("Autre Exception :" + e);
        }
    }
}