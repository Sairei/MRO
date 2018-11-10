#!/bin/bash

# lancer maven ou gradle avec la classe COP dans le dossier problems de modeler
# changer les chemins d'acces avec de lancer le fichier

path_xcsp='/home/cauet/Bureau/XCSP3-Java-Tools/target/xcsp3-tools-1.0.1-SNAPSHOT.jar'
path_json='/home/cauet/javax.json-1.1.jar'

path_file='/home/cauet/Bureau/XCSP3-Java-Tools/src/main/java/donnees_cop/'

file=`ls $path_file`

#model m1
for f in $file
do
	java -cp $path_xcsp:$path_json org.xcsp.modeler.Compiler org.xcsp.modeler.problems.COP -data=$path_file$f -model=m1
done

#model m2
for f in $file
do
	java -cp $path_xcsp:$path_json org.xcsp.modeler.Compiler org.xcsp.modeler.problems.COP -data=$path_file$f -model=m2
done

#model m3
for f in $file
do
	java -cp $path_xcsp:$path_json org.xcsp.modeler.Compiler org.xcsp.modeler.problems.COP -data=$path_file$f -model=m3
done
