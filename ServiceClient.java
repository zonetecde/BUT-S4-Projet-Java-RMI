import java.rmi.Remote;
import java.rmi.RemoteException;
import raytracer.Image;

public interface ServiceClient extends Remote{

    public Image compute(String contenuFichier, int wScreen, int hScreen, int x0, int y0, int w, int h) throws RemoteException;
    
}
