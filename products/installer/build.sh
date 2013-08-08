#!/bin/bash

# preparation
# - prepare the JAR file
ant -f create_jar.xml
# - prepare the standalone command
cat stub.sh payload/pdfmate.jar > payload/pdfmate
# - prepare the previous GIT commit hash for identifying commits
echo "prev_commit_hash = $(git log -1 --pretty="%H")" > payload/install.info

cd payload
tar czf ../payload.tar.gz ./*
cd ..

if [ -e "payload.tar.gz" ]; then
    cat decompress.sh payload.tar.gz > pdfmate.installer
    chmod a+x pdfmate.installer
    rm payload.tar.gz
else
    echo "Failed to create payload.tar.gz"
    exit 1
fi

echo "pdfmate.installer is created"
exit 0
