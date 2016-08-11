#!/bin/bash
function pri(){
read a
if (( ${#a} > 3 ))
then
echo -e "$1 $a ms"
fi

}

for ((i = $1; i < $5+1; i++))
do
for ((j = $2; j < $6+1; j++))
do
for ((k = $3; k < $7+1; k++))
do
for ((l = $4; l < $8+1; l++))
do
if [ $l != $8 ]; then
(timeout 5s ping -c 1 $i.$j.$k.$l | grep 'ms'| awk '{print $7;}' |pri "$i.$j.$k.$l") & (continue;)
else
timeout 5s ping $i.$j.$k.$l | grep 'ms'| awk '{print $7;}' |pri "$i.$j.$k.$l"
fi
done
done
done
done


