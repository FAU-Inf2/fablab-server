#!/bin/bash

token=$1
repo_id=$2
inputfile=$3
filename=$4

git clone https://$token@gist.github.com/$repo_id.git
mv $inputfile $repo_id/$filename
cd $repo_id
git add $filename
git commit -m "Added $filename"
git push
cd ../
rm -rf $repo_id/
