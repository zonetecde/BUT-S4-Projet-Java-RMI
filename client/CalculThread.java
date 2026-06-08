import java.rmi.RemoteException;

import raytracer.Disp;
import raytracer.Image;
import raytracer.Scene;
import service.distributeur.ServiceDistributeur;
import service.noeud.ServiceNoeudCalcul;

class CalculThread extends Thread {
    // on doit passer tous ces attributs à notre thread car il en a besoin pour faire son travail
    private ServiceNoeudCalcul noeud;
    private ServiceDistributeur distributeur;
    private Scene scene;
    private Disp disp;
    private int x0;
    private int y0;
    private int w;
    private int h;
    private boolean succes;

    /**
     * Crée un thread de calcul pour une zone de l'image.
     *
     * @param noeud noeud de calcul pour calculer la zone
     * @param distributeur distributeur central
     * @param scene scène
     * @param disp fenêtre d'affichage pour afficher une fois le calcul terminé
     */
    public CalculThread(ServiceNoeudCalcul noeud, ServiceDistributeur distributeur,
                        Scene scene, Disp disp, int x0, int y0, int w, int h) {
        this.noeud = noeud;
        this.distributeur = distributeur;
        this.scene = scene;
        this.disp = disp;
        this.x0 = x0;
        this.y0 = y0;
        this.w = w;
        this.h = h;
    }

    /**
     * Lance le calcul de la zone.
     * Si le noeud de calcul ne répond pas, le retire du distributeur
     */
    public void run() {
        try {
            // calcul via le noeud de calcul distant de la zone
            Image imageZone = noeud.compute(scene, x0, y0, w, h);
            succes = true;
            afficheZone(imageZone);
        } catch (RemoteException e) {
            try {
                // le supprime aussi coté distributeur
                distributeur.supprimerClient(noeud);
            } catch (RemoteException ignored) {
            }
            succes = false;
        }
    }

    /**
     * Affiche la zone calculée dans la fenêtre
     *
     * @param imageZone sous-image
     */
    private void afficheZone(Image imageZone) {
        for (int y = 0; y < imageZone.getHeight(); y++) {
            for (int x = 0; x < imageZone.getWidth(); x++) {
                disp.setImagePixel(x0 + x, y0 + y, imageZone.getPixel(x, y));
            }
            disp.repaint();
        }
    }

    /**
     * Indique si le calcul de la zone a réussi
     *
     * @return true si le calcul a réussi, false sinon
     */
    public boolean estSucces() {
        return succes;
    }

    /**
     * Renvoie la zone de ce thread
     */
    public int[] getZone() {
        return new int[] { x0, y0, w, h };
    }
}
