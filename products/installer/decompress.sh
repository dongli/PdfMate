#!/bin/bash

echo "[Notice]: Installing PdfMate ..."

export TMPDIR=`mktemp -d /tmp/pdfmate.installation.XXXXX`

ARCHIVE=`awk '/^__ARCHIVE_BELOW_/ { print NR+1; exit 0; }' $0`

tail -n+$ARCHIVE $0 | tar xz -C $TMPDIR

OLDDIR=`pwd`

cd $TMPDIR

bash ./installer.sh

cd $OLDDIR

rm -rf $TMPDIR

exit 0

__ARCHIVE_BELOW_
