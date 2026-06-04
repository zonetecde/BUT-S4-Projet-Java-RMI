import java.time.Instant;
import java.time.Duration;

import raytracer.Disp;
import raytracer.Scene;
import raytracer.Image;

public class LancerRaytracer {

    public static String aide = "Raytracer : synthèse d'image par lancé de rayons (https://en.wikipedia.org/wiki/Ray_tracing_(graphics))\n\nUsage : java LancerRaytracer [fichier-scène] [largeur] [hauteur]\n\tfichier-scène : la description de la scène (par défaut simple.txt)\n\tlargeur : largeur de l'image calculée (par défaut 512)\n\thauteur : hauteur de l'image calculée (par défaut 512)\n";
    
    /**
     * Calcule les zones de découpe pour diviser une image en n x n carrés.
     * 
     * @param nbreDecoupes nombre de lignes/colonnes de découpe (ex: 2 pour 4 carrés, 3 pour 9 carrés)
     * @param width largeur de l'image
     * @param height hauteur de l'image
     * @return tableau contenant pour chaque sous-tableau: [x, y, w, h]
     */
    public static int[][] calculerZones(int nbreDecoupes, int width, int height) {
        // tableau qui va stocker les coordonnées et tailles de chaque zone : [x, y, w, h]
        int[][] zones = new int[nbreDecoupes * nbreDecoupes][4];
        
        // calcul de la largeur et hauteur de chaque zone
        int largeurZone = width / nbreDecoupes;
        int hauteurZone = height / nbreDecoupes;
        
        // on parcourt les lignes et colonnes pour calculer les coordonnées de chaque zone
        int index = 0;
        for(int ligne = 0; ligne < nbreDecoupes; ligne++) {
            for(int colonne = 0; colonne < nbreDecoupes; colonne++) {
                int x = colonne * largeurZone;
                int y = ligne * hauteurZone;
                
                // pour la dernière colonne/ligne, on ajuste la taille pour couvrir le reste
                // de l'image au cas ou width ou height ne soit pas divisible par nbreDecoupes
                int w = (colonne == nbreDecoupes - 1) ? (width - x) : largeurZone;
                int h = (ligne == nbreDecoupes - 1) ? (height - y) : hauteurZone;
                
                // stock les coordonnées et tailles de la zone dans le tableau
                zones[index][0] = x;
                zones[index][1] = y;
                zones[index][2] = w;
                zones[index][3] = h;
                index++;
            }
        }
        
        return zones;
    }
     
    public static void main(String args[]){

        // Le fichier de description de la scène si pas fournie
        String fichier_description="simple.txt";

        // largeur et hauteur par défaut de l'image à reconstruire
        int largeur = 512, hauteur = 512;

        var zones = calculerZones(2, largeur, hauteur);
        
        // création d'une fenêtre 
        Disp disp = new Disp("Raytracer", largeur, hauteur);
        
        // Initialisation d'une scène depuis le modèle 
        Scene scene = new Scene(fichier_description, largeur, hauteur);


        // Chronométrage du temps de calcul
        Instant debut = Instant.now();

        System.out.println("Calcul de l'image :");


        System.out.println(" - Zone haut gauche : coordonnées " + zones[0][0] + "," + zones[0][1]
                           + " taille " + zones[0][2] + "x" + zones[0][3]);

        // Appel de la méthode de calcul pour cette zone
        Image imageHautGauche = scene.compute(zones[0][0], zones[0][1], zones[0][2], zones[0][3]);
        disp.setImage(imageHautGauche, zones[0][0], zones[0][1]);

        System.out.println(" - Zone bas droite : coordonnées " + zones[3][0] + "," + zones[3][1]
                           + " taille " + zones[3][2] + "x" + zones[3][3]);

        // Appel de la méthode de calcul pour cette zone
        Image imageBasDroite = scene.compute(zones[3][0], zones[3][1], zones[3][2], zones[3][3]);
        disp.setImage(imageBasDroite, zones[3][0], zones[3][1]);

        Instant fin = Instant.now();

        long duree = Duration.between(debut, fin).toMillis();
        
        // Affichage du temps de calcul
        System.out.println("Image calculée en :" + duree + " ms");
    }	
}
