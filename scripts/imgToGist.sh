#!/bin/bash

repo_id=$1
inputfile=$2
filename=$3

git clone https://$GITHUB_TOKEN@gist.github.com/$repo_id.git
mv $inputfile $repo_id/$filename
cd $repo_id
git add $filename
git commit -m "Added $filename"
git push
cd ../
rm -rf $repo_id/
