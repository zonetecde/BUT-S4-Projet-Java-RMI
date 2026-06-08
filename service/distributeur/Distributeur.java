package service.distributeur;

import java.util.ArrayList;
import service.noeud.ServiceNoeudCalcul;

public class Distributeur implements ServiceDistributeur{
    // liste des services de noeuds de calcul disponibles
    ArrayList<ServiceNoeudCalcul> services = new ArrayList<>();

    // curseur interne pour itérer sur les noeuds dispos
    int curseur = 0;

    /**
     * Enregistre un nouveau noeud de calcul
     */
    public synchronized void enregistrerClient(ServiceNoeudCalcul c) {
        services.add(c);
        System.out.println("Nouveau noeud de calcul disponible");
    }

    /**
     * Envoie un noeud de calcul disponible
     * Le noeud est considéré comme occupé tant qu'il n'a pas été libéré
     */
    public synchronized ServiceNoeudCalcul recupererNoeud() {
        if (services.isEmpty()) {
            return null;
        }

        // ajuste le curseur s'il dépasse la taille
        if (curseur >= services.size()) {
            curseur = 0;
        }

        // retire le noeud à la position du curseur car il est considéré comme occupé
        ServiceNoeudCalcul service = services.remove(curseur);

        // réinitialise le curseur si nécessaire après le retrait
        if (curseur >= services.size()) {
            curseur = 0;
        }

        return service;
    }

    /**
     * Remet un noeud de calcul après la fin de son calcul afin qu'il puisse être réutilisé pour d'autres calculs
     */
    public synchronized void libererNoeud(ServiceNoeudCalcul c) {
        services.add(c);
        System.out.println("Noeud de calcul libéré");
    }

    /**
     * Supprime un noeud de calcul
     */
    public synchronized void supprimerClient(ServiceNoeudCalcul c) {
        services.remove(c);

        if (services.isEmpty()) {
            curseur = 0;
        } else if (curseur >= services.size()) {
            curseur = 0;
        }
        System.out.println("Noeud de calcul supprimé");
    }
}
