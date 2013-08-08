#!/bin/bash

# create .pdfmate directory under the $HOME
PDFMATE_ROOT=$HOME/.pdfmate
if [ ! -d $PDFMATE_ROOT ]; then
    echo "[Notice]: Create $PDFMATE_ROOT"
    mkdir $PDFMATE_ROOT
else
    echo "[Notice]: Use existing $PDFMATE_ROOT"
    if [ ! -z "`ls $PDFMATE_ROOT`" ]; then
        echo "[Notice]: $PDFMATE_ROOT is not empty"
        ls -l $PDFMATE_ROOT
        if [ -e $PDFMATE_ROOT/pdfmate.jar ]; then
            VERSION=`java -jar $PDFMATE_ROOT/pdfmate.jar -v`
            if [ -e $PDFMATE_ROOT/install.info ]; then
                COMMIT_HASH=`awk '/prev_commit_hash/ { print $3; }' $PDFMATE_ROOT/install.info`
            fi
            echo "[Notice]: You have installed PdfMate $VERSION in commit $COMMIT_HASH."
        fi
    fi
fi

# copy files into $PDFMATE_ROOT
cp ./pdfmate.jar $PDFMATE_ROOT
cp ./pdfmate $PDFMATE_ROOT && chmod a+x $PDFMATE_ROOT/pdfmate
cp ./setup.sh $PDFMATE_ROOT
cp ./install.info $PDFMATE_ROOT

if [[ "$SHELL" =~ "bash" ]]; then
    echo "[Notice]: Set BASH environment in $HOME/.bashrc."
    if ! grep 'source .*\.pdfmate/setup\.sh' $HOME/.bashrc > /dev/null; then
        echo -e "\n\n# Added by PdfMate\nsource $HOME/.pdfmate/setup.sh" >> $HOME/.bashrc
    fi
elif [[ "$SHELL" =~ "tcsh" ]]; then
    echo "[Notice]: Set TCSH environment."
    if ! grep 'source .*\.pdfmate/setup\.sh' $HOME/.bashrc > /dev/null; then
        echo -e "\n\n# Added by PdfMate\nsource $HOME/.pdfmate/setup.sh" >> $HOME/.cshrc
    fi
fi
