package service.noeud;

import java.rmi.Remote;
import java.rmi.RemoteException;
import raytracer.Image;
import raytracer.Scene;

public interface ServiceNoeudCalcul extends Remote{

    public Image compute(Scene scene, int x0, int y0, int w, int h) throws RemoteException;
    
}
