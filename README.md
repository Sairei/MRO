# MRO
> Modelisation et Resolution pour l'Optimisation
----
----
# Sujet

Dans ce projet, on s’intèresse aux problèmes d’allocation de fréquences entre des stations.

On dispose de n stations réparties dans k régions distinctes. Chaque station est composé d’un émetteur et
d’un récepteur et peut exploiter certaines fréquences. 

Pour pouvoir communiquer avec d’autres stations, chaque station doit disposer de deux fréquences : 
une pour son émetteur et une pour son récepteur. Pour des raisons matérielles, l’écart entre les deux fréquences
de la station i doit être égal à δi. 
Deux stations différentes peuvent donc avoir le même écart comme des écarts différents. 

Si deux stations sont proches l’une de l’autre, les fréquences utilisées par ces stations doivent être suffisamment
espacées pour éviter les interférences. On notera ∆ij l’écart minimun à garantir entre les fréquences des stations i et j. 

Enfin, pour chaque région, on souhaite limiter le nombre de fréquences différentes utilisées. On notera ni
le nombre maximum de fréquences diff´erentes utilisées pour la région i.

---
## 1. Problème d'optimisation sous contraintes

Dans cette première partie, on souhaite envisager différents problèmes d'optimisation.
- Minimiser le nombre de fréquences utilisées
- Utiliser les fréquences les plus basses possibles
- Minimiser la largeur de la bande de fréquences utilisées


### _Fichiers_
**- DONNEES :** Fichiers au format json

**- SOURCES :** Classe java implémentant l'API [MCSP3](https://github.com/xcsp3team/XCSP3-Java-Tools)

**- SORTIES :** Fichiers au format [XCSP3](http://www.xcsp.org/specifications), extension xml

### _A Faire_

- [x] Modéliser chacun de ces problèmes sous forme d’un COP.
- [x] Produire les instances correspondantes à partir des donn´ees fournies. On utilisera le format XCSP3
- [ ] Comparer l'efficacité de différents solveurs pour résoudre ces instances.

---
## 2. Problèmes de satisfaction de contraintes valués

Il est possible que, pour certaines données fournies, il n'existe pas de solution au sens CSP. 
Dans ce cas, il peut être pertinent d'exploiter le cadre des CSP valués pour proposer des solutions satisfaisant
autant que possible les contraintes.

### _Fichiers_
**- DONNEES :** Fichiers au format json

**- SORTIES :** Fichiers au format [WCSP](https://github.com/toulbar2/toulbar2/raw/master/doc/wcspformat.pdf)

### _A Faire_

- [x] Modéliser le problème sous forme d’un CSP valué.
- [ ] Produire les instances correspondantes à partir des données fournies. On utilisera le format WCSP.
- [ ] Résoudre les instances à l’aide du solveur Toulbar2.
