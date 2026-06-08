package service;

import java.util.ArrayList;

public class Distributeur implements ServiceDistributeur{
    // liste des services de noeuds de calcul disponibles
    ArrayList<ServiceNoeudCalcul> services  = new ArrayList<>() ;

    /**
     * Enregistre un nouveau noeud de calcul
     */
    public void enregistrerClient(ServiceNoeudCalcul c) {
        services.add(c) ;
        System.out.println("Nouveau noeud de calcul disponible");
    }

    /**
     * Envoie la liste des noeuds de calcul disponibles
     */
    public ArrayList<ServiceNoeudCalcul> envoyerClients() {
        return services ;
    }

    /**
     * Supprime un noeud de calcul
     */
    public void supprimerClient(ServiceNoeudCalcul c) {
        services.remove(c);
        System.out.println("Noeud de calcul supprimé");
    }
}

