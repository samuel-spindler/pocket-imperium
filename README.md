# Pocket Imperium

Un jeu de stratégie au tour par tour pour 1 à 3 joueurs, implantation Java du jeu de société éponyme. Les joueurs rivalisent pour conquérir des secteurs d'une galaxie hexagonale en déployant des vaisseaux et en jouant des cartes d'action.

## Équipe du projet

Ce projet a été réalisé en équipe dans le cadre de l'UE LO02 en deuxième année de cycle préparatoire à la formation d'ingénieur
- **Samuel SPINDLER**
- **Margot REMY**

## À Propos

Pocket Imperium est un jeu de conquête spatiale où chaque joueur contrôle une flotte de vaisseaux dans le but de dominer une galaxie divisée en secteurs hexagonaux. Le jeu repose sur un système de cartes d'action où les joueurs doivent anticiper les mouvements de leurs adversaires tout en optimisant leur propre stratégie. La mécanique de force des cartes crée une tension tactique : plus de joueurs choisissent la même action, moins elle est puissante pour chacun.

## Caractéristiques

- **Mode multijoueur** : Support de 1 à 3 joueurs (IA et humains)
- **Gameplay tour par tour** : Saisons et rounds organisés avec système d'ordre de jeu dynamique
- **Cartes d'action** : Trois types de manœuvres stratégiques
  - **Explore** : Déplacer les vaisseaux vers de nouveaux territoires
  - **Expand** : Renforcer votre présence avec des navires supplémentaires
  - **Exterminate** : Éliminer les navires adverses
- **Grille hexagonale** : Carte composée de 54 secteurs organisés en hexagones de trois niveaux
- **Progression** : Gestion des points et hiérarchie des vaisseaux
- **Sauvegarde/Chargement** : Persistance des parties en cours

## Structure du Projet

```
src/
├── Main.java                 # Point d'entrée du jeu
├── Game.java                 # Logique principale et singleton du jeu
├── Round.java                # Gestion des tours de jeu
├── Player.java               # Représentation d'un joueur
├── BotPlayer.java            # Joueur contrôlé par l'IA
├── Strategy.java             # Interface pour les stratégies IA
├── RandomStrategy.java       # Implémentation d'une IA aléatoire
├── ActionCard.java           # Classe abstraite pour les cartes
├── Expand.java               # Carte d'expansion
├── Explore.java              # Carte d'exploration
├── Exterminate.java          # Carte d'élimination
├── Hexagone.java             # Représentation d'un hexagone
├── HexagoneTriPrime.java     # Hexagone de niveau 3 (centre du territoire)
├── Sector.java               # Groupement d'hexagones
├── EnumCard.java             # Énumération des types de cartes
├── EnumColor.java            # Énumération des couleurs de joueurs
README.md                     # Documentation
```

## Compilation et Exécution

### Prérequis
- Java 11 ou supérieur

### Compilation
```bash
javac -d bin src/*.java
```

### Lancement
```bash
java -cp bin Main
```

## Utilisation

### Démarrage d'une Partie

Au lancement du jeu, le menu principal vous propose :
1. **Charger une partie sauvegardée** : Reprendre une session existante
2. **Lancer une nouvelle partie** : Commencer une nouvelle conquête

Définissez le nombre de joueurs humains (1-3). Les positions restantes seront automatiquement complétées par des bots pour atteindre 3 joueurs.

### Déroulement d'une Partie

Chaque partie se compose de plusieurs rounds durant lesquels les joueurs :
1. **Phase de planification** : Sélectionnent secrètement l'ordre de leurs 3 cartes d'action
2. **Phase d'exécution** : Révèlent et appliquent les cartes dans l'ordre choisi
3. **Calcul des scores** : Les points sont attribués selon le contrôle territorial

Le jeu affiche la carte complète avec les positions des vaisseaux et permet de sauvegarder à tout moment.

## Mécanique de Jeu

### Plateau de Jeu
Le plateau est composé de **54 hexagones** organisés en **18 secteurs**. Chaque secteur contient 3 hexagones de niveaux différents :
- **Niveau 1** : Hexagones périphériques
- **Niveau 2** : Hexagones intermédiaires
- **Niveau 3** : Hexagone central (TriPrime) — le plus stratégique

### Déroulement d'un Round

#### 1. Phase de Planification
Chaque joueur organise secrètement ses 3 cartes d'action (Explore, Expand, Exterminate) en choisissant l'ordre dans lequel elles seront révélées.

#### 2. Phase d'Exécution
Les cartes sont révélées position par position :
- **Position 1** : Tous les joueurs révèlent leur première carte
- **Position 2** : Tous les joueurs révèlent leur deuxième carte
- **Position 3** : Tous les joueurs révèlent leur troisième carte

#### 3. Calcul de la Force
La force d'une carte dépend du nombre de joueurs l'ayant choisie à la même position :
- **1 joueur seul** → Force 1 (effet maximal)
- **2 joueurs** → Force 2 (effet moyen)
- **3 joueurs** → Force 3 (effet minimal)

Les actions sont résolues dans cet ordre : **Expand → Explore → Exterminate**

### Cartes d'action

| Carte | Force | Effet |
|-------|-------|-------|
| **Explore** | 1 | 3 déplacements de vaisseaux |
| **Explore** | 2 | 2 déplacements de vaisseaux |
| **Explore** | 3 | 1 déplacement de vaisseau |
| **Expand** | 1 | Placer 3 nouveaux vaisseaux |
| **Expand** | 2 | Placer 2 nouveaux vaisseaux |
| **Expand** | 3 | Placer 1 nouveau vaisseau |
| **Exterminate** | 1 | Détruire 3 vaisseaux adverses |
| **Exterminate** | 2 | Détruire 2 vaisseaux adverses |
| **Exterminate** | 3 | Détruire 1 vaisseau adverse |

### Stratégie

Le cœur du jeu réside dans l'anticipation des choix adverses :
- Choisir une action unique maximise son effet (force 1)
- Partager une action avec d'autres réduit son efficacité
- L'ordre de révélation des cartes peut créer des opportunités tactiques
- Le contrôle des hexagones de niveau 3 est crucial pour marquer des points

## Concepts Techniques

### Patterns de Conception
- **Singleton** : La classe `Game` utilise ce pattern pour garantir une instance unique de la partie
- **Strategy Pattern** : L'interface `Strategy` permet différentes implémentations des bots (actuellement `RandomStrategy`)
- **Héritage** : Les cartes d'action (`Expand`, `Explore`, `Exterminate`) héritent de `ActionCard`

### Système de Coordonnées Hexagonales
Le jeu utilise un système de coordonnées axiales cubiques (q, r, s) où `s = -q - r` pour gérer la grille hexagonale et les déplacements adjacents.

### Sérialisation
Les classes principales implémentent `Serializable` pour permettre la sauvegarde et le chargement des parties en cours via l'écriture d'objets Java sur le disque.



## Améliorations Possibles

- Ajout d'une interface graphique (Swing/JavaFX)
- Implémentation de stratégies pour les bots plus avancées
- Statistiques et historique des parties







