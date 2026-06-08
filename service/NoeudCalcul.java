package service;

import raytracer.Image;
import raytracer.Scene;

import java.rmi.RemoteException;
import java.rmi.server.RemoteServer;
import java.rmi.server.ServerNotActiveException;

public class NoeudCalcul implements  ServiceNoeudCalcul{

    public Image compute(String contenuFichier, int wScreen, int hScreen, int x0, int y0, int w, int h) throws RemoteException {
        Scene s = new Scene(contenuFichier, wScreen, hScreen);
        //affichage de l'utilisateur
        String host = "";
        try{
            host = RemoteServer.getClientHost();
            System.out.println("id client :" + host);
        }catch(ServerNotActiveException e) {
            System.out.println("ID client introuvable");
        }
        return s.compute(x0 , y0 , w, h);
    }
}
