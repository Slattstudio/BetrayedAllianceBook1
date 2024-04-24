#! /bin/bash

command -v dosbox >/dev/null 2>&1 || { echo >&2 "I require dosbox but it's not installed.  Aborting."; exit 1; }

dosbox -noconsole Betrayed\ Alliance.exe
