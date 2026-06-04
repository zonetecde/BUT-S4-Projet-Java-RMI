import java.time.Instant;
import java.time.Duration;

import raytracer.Disp;
import raytracer.Scene;
import raytracer.Image;

public class LancerRaytracer {

    public static String aide = "Raytracer : synthèse d'image par lancé de rayons (https://en.wikipedia.org/wiki/Ray_tracing_(graphics))\n\nUsage : java LancerRaytracer [fichier-scène] [largeur] [hauteur]\n\tfichier-scène : la description de la scène (par défaut simple.txt)\n\tlargeur : largeur de l'image calculée (par défaut 512)\n\thauteur : hauteur de l'image calculée (par défaut 512)\n";
     
    public static void main(String args[]){

        // Le fichier de description de la scène si pas fournie
        String fichier_description="simple.txt";

        // largeur et hauteur par défaut de l'image à reconstruire
        int largeur = 512, hauteur = 512;
        
        if(args.length > 0){
            fichier_description = args[0];
            if(args.length > 1){
                largeur = Integer.parseInt(args[1]);
                if(args.length > 2)
                    hauteur = Integer.parseInt(args[2]);
            }
        }else{
            System.out.println(aide);
        }
        // création d'une fenêtre 
        Disp disp = new Disp("Raytracer", largeur, hauteur);
        
        // Initialisation d'une scène depuis le modèle 
        Scene scene = new Scene(fichier_description, largeur, hauteur);

        // On découpe l'image en deux dimensions pour avoir 2x2 zones
        int demiLargeur = largeur / 2;
        int demiHauteur = hauteur / 2;

        // Chronométrage du temps de calcul
        Instant debut = Instant.now();

        System.out.println("Calcul de l'image :");

        // Première zone : haut gauche
        // Celle-ci commence à x = 0 et y = 0, et sa taille est de demiLargeur x demiHauteur
        int x0 = 0;
        int y0 = 0;
        int l = demiLargeur;
        int h = demiHauteur;

        System.out.println(" - Zone haut gauche : coordonnées " + x0 + "," + y0
                           + " taille " + l + "x" + h);

        // Appel de la méthode de calcul pour cette zone
        Image imageHautGauche = scene.compute(x0, y0, l, h);
        disp.setImage(imageHautGauche, x0, y0);

        // Deuxième zone : bas droite
        // Celle-ci commence à x = demiLargeur et y = demiHauteur, et sa taille est de (largeur - demiLargeur) x (hauteur - demiHauteur)
        // Note : quand on a une taille PAIRE, demiLargeur x demiLargeur aurait été suffisant, mais quand on a une taille IMPAIRE, il faut faire attention à ne pas dépasser la taille totale de l'image du coup on a fait (largeur - demiLargeur) et (hauteur - demiHauteur)
        x0 = demiLargeur;
        y0 = demiHauteur;
        l = largeur - demiLargeur;
        h = hauteur - demiHauteur;

        System.out.println(" - Zone bas droite : coordonnées " + x0 + "," + y0
                           + " taille " + l + "x" + h);

        // Appel de la méthode de calcul pour cette zone
        Image imageBasDroite = scene.compute(x0, y0, l, h);
        disp.setImage(imageBasDroite, x0, y0);

        Instant fin = Instant.now();

        long duree = Duration.between(debut, fin).toMillis();
        
        // Affichage du temps de calcul
        System.out.println("Image calculée en :" + duree + " ms");
    }	
}
