import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServiceDistributeur extends Remote{
    
public void enregistrerClient(ServiceClient sc) throws RemoteException;

public void computeAll() throws RemoteException;
    
}
