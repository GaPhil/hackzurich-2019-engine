#!/bin/bash

cd src/main/resources/interviews/

case "$1" in

    f1) f=$(<Application-Transcript-Easy-1.txt) ;;
    f2) f=$(<Transcript-Hard-3.txt) ;;
    f3) f=$(<Transcripts-Easy-1.txt) ;;
    f4) f=$(<Application-Transcript-Easy-2.txt) ;;
    f5) f=$(<Transcript-Medium\ Difficulty-1.txt) ;;
    f6) f=$(<Transcripts-Easy-2.txt) ;;
    f7) f=$(<Transcript-Hard-1.txt) ;;
    f8) f=$(<Transcript-Medium\ Difficulty-2.txt) ;;
    f9) f=$(<Transcripts-Easy-3.txt) ;;
    f10) f=$(<Transcript-Hard-2.txt) ;;
    f11) f=$(<Transcript-Medium\ Difficulty-3.txt) ;;

    *) f=$(<Application-Transcript-Easy-1.txt) ;;

esac

curl -d "$f" -X POST http://localhost:4567/api/analyse
