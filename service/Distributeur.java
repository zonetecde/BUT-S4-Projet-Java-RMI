package service;

import java.util.ArrayList;

public class Distributeur implements ServiceDistributeur{
    // liste des services de noeuds de calcul disponibles
    ArrayList<ServiceNoeudCalcul> services = new ArrayList<>();

    // curseur interne pour itérer sur les noeuds dispos
    int curseur = 0;

    /**
     * Enregistre un nouveau noeud de calcul
     */
    public void enregistrerClient(ServiceNoeudCalcul c) {
        services.add(c);
        System.out.println("Nouveau noeud de calcul disponible");
    }

    /**
     * Envoie un noeud de calcul disponible
     */
    public ServiceNoeudCalcul recupererNoeud() {
        if (services.isEmpty()) {
            return null;
        }

        // prend le noeud à la position du curseur      
        ServiceNoeudCalcul service = services.get(curseur);
        // avance le curseur de 1, et le remet à 0 s'il dépasse la taille de la liste
        curseur = (curseur + 1) % services.size();
        return service;
    }

    /**
     * Supprime un noeud de calcul
     */
    public void supprimerClient(ServiceNoeudCalcul c) {
        services.remove(c);

        // Si on a plus de noeud dispo on met le cursuer à 0
        // sinon on calcul grace au modulo la nouvelle pos du curseur
        if (services.isEmpty()) {
            curseur = 0;
        } else {
            curseur = curseur % services.size();
        }
        System.out.println("Noeud de calcul supprimé");
    }
}
