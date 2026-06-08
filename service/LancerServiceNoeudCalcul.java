package service;

import java.rmi.ConnectException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class LancerServiceNoeudCalcul {
    public static void main(String args[]) {
        try {
            // on commence par déclarer l'objet partagé
            NoeudCalcul o = new NoeudCalcul();

            // on le tranforme en service
            ServiceNoeudCalcul s = (ServiceNoeudCalcul) UnicastRemoteObject.exportObject(o, 0);

            // recup l'annuaire
            Registry reg = LocateRegistry.getRegistry(1099);

            // on recup ensuite le distributeur et on s'y enregistre
            ServiceDistributeur distributeur = (ServiceDistributeur) reg.lookup("distributeur");
            
            distributeur.enregistrerClient(s);
        } catch (ConnectException e) {
            System.out.println("L'annuaire n'est pas accessible ou pas lancé");
        } catch (java.rmi.NotBoundException e) {
            System.out.println("Le distributeur n'est pas disponible dans l'annuaire");
        } catch (Exception e) {
            System.out.println("Autre Exception :" + e);
        }
    }
}