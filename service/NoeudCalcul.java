package service;

import raytracer.Image;
import raytracer.Scene;

import java.rmi.RemoteException;
import java.rmi.server.RemoteServer;
import java.rmi.server.ServerNotActiveException;

public class NoeudCalcul implements  ServiceNoeudCalcul{

    /**
     * Calcule une partie de l'image correspondant à la scène donnée et aux coordonnées données.
      * @param scene la scène à calculer
      * @param x0 la coordonnée x du coin supérieur gauche de la partie à calculer
      * @param y0 la coordonnée y du coin supérieur gauche de la partie à calculer
      * @param w la largeur de la partie à calculer
      * @param h la hauteur de la partie à calculer
      * @return l'image calculée pour la partie donnée
     */
    public Image compute(Scene scene, int x0, int y0, int w, int h) throws RemoteException {
        //affichage de l'utilisateur
        String host = "";
        try{
            host = RemoteServer.getClientHost();
            System.out.println("id client :" + host);
        }catch(ServerNotActiveException e) {
            System.out.println("ID client introuvable");
        }
        return scene.compute(x0 , y0 , w, h);
    }
}
