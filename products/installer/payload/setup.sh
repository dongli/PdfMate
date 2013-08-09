#!/bin/bash

export PDFMATE_ROOT=$(dirname $BASH_ARGV)
export PATH=$PDFMATE_ROOT:$PATH

# command line completion
function _pdfmate_()
{
    local prev_argv=${COMP_WORDS[COMP_CWORD-1]}
    local curr_argv=${COMP_WORDS[COMP_CWORD]}
    completed_words=""
    case "${prev_argv##*/}" in
    "pdfmate")
        completed_words="help extract-pages insert-toc update"
        ;;
    "help")
        completed_words="extract-pages insert-toc update"
        ;;
    esac
    COMPREPLY=($(compgen -W "$completed_words" -- $curr_argv))
}

complete -o default -F _pdfmate_ pdfmate
