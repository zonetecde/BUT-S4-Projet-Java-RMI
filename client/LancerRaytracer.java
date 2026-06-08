import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;

import raytracer.Disp;
import raytracer.Scene;
import service.distributeur.ServiceDistributeur;
import service.noeud.ServiceNoeudCalcul;

public class LancerRaytracer {

    public static String aide = "Raytracer : synthèse d'image par lancé de rayons (https://en.wikipedia.org/wiki/Ray_tracing_(graphics))\n\nUsage : java LancerRaytracer [fichier-scène] [largeur] [hauteur]\n\tfichier-scène : la description de la scène (par défaut simple.txt)\n\tlargeur : largeur de l'image calculée (par défaut 512)\n\thauteur : hauteur de l'image calculée (par défaut 512)\n";

    /**
     * Calcule les zones de découpe pour diviser une image en n x n carrés.
     * 
     * @param nbreDecoupes nombre de lignes/colonnes de découpe (ex: 2 pour 4
     *                     carrés, 3 pour 9 carrés)
     * @param width        largeur de l'image
     * @param height       hauteur de l'image
     * @return tableau contenant pour chaque sous-tableau: [x, y, w, h]
     */
    public static int[][] calculerZones(int nbreDecoupes, int width, int height) {
        // tableau qui va stocker les coordonnées et tailles de chaque zone : [x, y, w,
        // h]
        int[][] zones = new int[nbreDecoupes * nbreDecoupes][4];
        // calcul de la largeur et hauteur de chaque zone
        int largeurZone = width / nbreDecoupes;
        int hauteurZone = height / nbreDecoupes;

        // on parcourt les lignes et colonnes pour calculer les coordonnées de chaque
        // zone
        int index = 0;
        for (int ligne = 0; ligne < nbreDecoupes; ligne++) {
            for (int colonne = 0; colonne < nbreDecoupes; colonne++) {
                int x = colonne * largeurZone;
                int y = ligne * hauteurZone;

                // pour la dernière colonne/ligne, on ajuste la taille pour couvrir le reste
                // de l'image au cas où width ou height ne soit pas divisible par nbreDecoupes
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

    public static void main(String args[]) {
        // Args : ip du point central, port de l'annuaire du point central, nbre de
        // "zone" sur une ligne
        if (args.length < 3) {
            System.out.println("Utilisation: java LancerRaytracer ip_point_central port_annuaire nbre_zone_par_ligne");
            return;
        }

        String ip = args[0];
        int port = Integer.parseInt(args[1]);
        int nbrZone = Integer.parseInt(args[2]);

        // Le fichier de description de la scène si pas fournie
        String fichier_description = "simple.txt";

        // largeur et hauteur par défaut de l'image à reconstruire
        int largeur = 512, hauteur = 512;

        int[][] zones = calculerZones(nbrZone, largeur, hauteur);
        // list qui va contenir toutes les zones qui ont échoué a être calculé
        ArrayList<int[]> zonesRestantes = new ArrayList<>(Arrays.asList(zones));

        // création d'une fenêtre
        Disp disp = new Disp("Raytracer", largeur, hauteur);

        // Initialisation d'une scène depuis le modèle
        Scene scene = new Scene(fichier_description, largeur, hauteur);

        // Chronométrage du temps de calcul
        Instant debut = Instant.now();

        try {
            // PARTIE RMI
            // 1. On récupère le service distributeur
            // Trouve l'annuaire du point central
            Registry reg = LocateRegistry.getRegistry(ip, port);
            ServiceDistributeur distributeur = (ServiceDistributeur) reg.lookup("distributeur");

            System.out.println("Calcul de l'image :");

            // 3. On calcule les différentes zones
            // On itère sur les noeuds de calcul dispo et on calcule en parallèle les zones
            // On affiche dès que reçu l'image calculée sur la fenêtre (géré par
            // CalculThread.java)
            while (!zonesRestantes.isEmpty()) {
                // liste de tout les threads qu'on va créé
                ArrayList<CalculThread> threads = new ArrayList<>();

                // nbre de calculs à faire dans cette itération
                int nombreCalculs = zonesRestantes.size();

                // itère sur les calculs à faire
                for (int i = 0; i < nombreCalculs; i++) {
                    // demande au service central un noeud de calcul disponible
                    ServiceNoeudCalcul noeud = distributeur.recupererNoeud();

                    // Si il retourne null c'est qu'il n'a aucun noeud de dispo
                    if (noeud == null) {
                        break;
                    }
                    // prend la zone i à calculer
                    int[] zone = zonesRestantes.get(i);

                    // créé le thread est lance le calcul sur le noeud
                    CalculThread thread = new CalculThread(noeud, distributeur, scene, disp, zone[0], zone[1],
                            zone[2], zone[3]);
                    threads.add(thread);
                    thread.start();
                }

                int nombreCalculsEnCours = threads.size();
                // si aucun calcul n'a été lancé, on attend qu'un noeud se libère ou qu'un nouveau s'enregistre
                if (nombreCalculsEnCours == 0) {
                    try {
                        // met un sleep pour pas spam le distributeur
                        Thread.sleep(200);
                    } catch (InterruptedException ignored) {
                    }
                    continue;
                }

                // Att la fin de tout les threads pour voir quelles zones ont échoué
                for (CalculThread thread : threads) {
                    thread.join();
                }

                // on enlève toutes les zones déjà calculées
                for (int i = 0; i < nombreCalculsEnCours; i++) {
                    zonesRestantes.remove(0);
                }
                // toutes les zones qui ont succes en false on va les refaire
                ArrayList<int[]> zonesEnEchec = new ArrayList<>();

                for (CalculThread thread : threads) {
                    if (!thread.estSucces()) {
                        zonesEnEchec.add(thread.getZone());
                    }
                }

                for (int[] zoneEchec : zonesEnEchec) {
                    zonesRestantes.add(zoneEchec);
                }
            }
        } catch (Exception e) {
            System.out.println("Erreur lors du calcul : " + e);
        }

        Instant fin = Instant.now();
        long duree = Duration.between(debut, fin).toMillis();
        // Affichage du temps de calcul
        System.out.println("Image calculée en :" + duree + " ms");
    }
}
