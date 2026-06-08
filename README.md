# Raytracer

L'image est découpée en zones calculées en parallèle par des noeuds de calcul distants, coordonnés par un distributeur central.

## Structure du projet

```
├── client/                        # Client (lanceur + threads)
│   ├── LancerRaytracer.java
│   └── CalculThread.java
├── raytracer/                     # Moteur de rendu
├── service/
│   ├── distributeur/              # Service central (distributeur)
│   │   ├── Distributeur.java
│   │   ├── LancerServiceDistributeur.java
│   │   └── ServiceDistributeur.java
│   └── noeud/                     # Noeuds de calcul
│       ├── LancerServiceNoeudCalcul.java
│       ├── NoeudCalcul.java
│       └── ServiceNoeudCalcul.java
├── simple.txt                     # Fichier de description de la scène
└── README.md
```

## Compilation

Depuis la racine du projet :

```bash
javac -d . service/distributeur/*.java service/noeud/*.java raytracer/*.java client/*.java
```

## Exécution

### 1. Lancer l'annuaire RMI

```bash
rmiregistry
```

### 2. Lancer le service central (distributeur)

```bash
java service.distributeur.LancerServiceDistributeur
```

### 3. Lancer un ou plusieurs noeuds de calcul

```bash
java service.noeud.LancerServiceNoeudCalcul
```

Lancez cette commande dans autant de terminaux que voulu pour ajouter des noeuds de calcul.

### 4. Lancer le client

```bash
java LancerRaytracer <ip> <port> <nbre_zone_par_ligne>
```

**Arguments :**

-   `ip` : adresse IP du serveur hébergeant l'annuaire RMI avec le service distributeur
-   `port` : port de l'annuaire RMI
-   `nbre_zone_par_ligne` : nombre de divisions de l'image par ligne (ex: `2` = 4 zones, `3` = 9 zones)

**Exemple :**

```bash
java LancerRaytracer localhost 1099 2
```
