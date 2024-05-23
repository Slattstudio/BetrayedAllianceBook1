#! /bin/bash

if [ $UID -eq 0 ]; then echo >&2 "$(basename $0): error: Do not run this as root. Aborting..."; exit; fi

command -v dosbox >/dev/null 2>&1 || { echo >&2 "$(basename $0): error: dosbox is not installed or in PATH. Aborting..."; exit 1; }

dosbox -noconsole -conf dosbox.conf
