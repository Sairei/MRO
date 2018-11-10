files=`ls CP`

for f in $files
do
	name=${f%.*}
	echo $name
	gawk -f cp2wcsp.awk "CP/"$f > WCSP/$name".wcsp"
done
