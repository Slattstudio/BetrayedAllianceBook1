#! /bin/bash

command -v dosbox >/dev/null 2>&1 || { echo >&2 "`basename $0`: error: dosbox is not installed or in PATH. Aborting..."; exit 1; }

dosbox -noconsole Betrayed\ Alliance.exe
