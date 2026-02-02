# Pocket Imperium

Un jeu de stratégie au tour par tour pour 1 à 3 joueurs, implantation Java du jeu. Les joueurs rivalisent pour conquérir des secteurs d'une galaxie hexagonale en déployant des vaisseaux et en jouant des cartes d'action.

## Équipe du projet

Ce projet a été réalisé en équipe dans le cadre de l'UE LO02 en deuxième année de cycle préparatoire à la formation d'ingénieur
- **Samuel SPINDLER**
- **Margot REMY**

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

## Architecture

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
```

## Compilation et Exécution

### Prérequis
- Java

### Compilation
```bash
javac -d bin src/*.java
```

### Lancement
```bash
java -cp bin Main
```

## Utilisation

Au lancement du jeu, vous pouvez :
1. Charger une partie sauvegardée
2. Lancer une nouvelle partie

Définissez le nombre de joueurs humains (1-3) et affrontez un bot pour compléter jusqu'à 3 joueurs.

## Mécanique de Jeu

### Déroulement d'une manche
- Les joueurs choisissent simultanément une carte d'action
- L'ordre de résolution dépend de la force de leur carte
- Chaque carte génère un effet différent sur le plateau

### Cartes d'action

| Carte | Force | Effet |
|-------|-------|-------|
| Explore | 1 | 3 déplacements |
| Explore | 2 | 2 déplacements |
| Explore | 3 | 1 déplacement |
| Expand | 1 | 3 vaisseaux |
| Expand | 2 | 2 vaisseaux |
| Expand | 3 | 1 vaisseau |
| Exterminate | 1 | 3 destructions |
| Exterminate | 2 | 2 destructions |
| Exterminate | 3 | 1 destruction |






