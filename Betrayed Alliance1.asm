; Based on https://www.betamaster.us/blog/?p=419
; Compile with Flat Assembler: https://flatassembler.net/
; Written by Kristofer Munsterhjelm, 2024.

format PE GUI 4.0
entry start
 
include 'win32a.inc'
section '.text' code readable writable executable

start:
        ; Try to launch DOSBox
        invoke CreateProcessA, 0, command, \
               0, 0, 0, \
               0, 0, 0, StartupInfo, ProcessInfo
        ; If we were successful, all done
        test eax, eax
        jne done
        ; Otherwise, show an error.
        invoke  MessageBox, 0, errormsg, caption, MB_ICONERROR+MB_OK
done:
        call [ExitProcess]

; Strings
command db '.\dosbox.exe -noconsole',0
caption db 'Error!',0
errormsg db 'Could not launch DOSBox!',0

; StartupInfo and ProcessInfo structs needed for the process call
StartupInfo STARTUPINFO
ProcessInfo PROCESS_INFORMATION
 
; Imported functions and corresponding names of DLL files:
data import
     library kernel,'KERNEL32.DLL', \
             user, 'USER32.DLL'
 
     import kernel,\
            CreateProcessA, "CreateProcessA",\
            ExitProcess,'ExitProcess'

     import user,\
            MessageBox,'MessageBoxA'

; https://board.flatassembler.net/topic.php?t=19527
; Set up icons.
section '.rsrc' resource data readable

        directory RT_ICON, icons,\
                  RT_GROUP_ICON, group_icons,\
                  RT_VERSION, versions

        resource icons, 1, LANG_NEUTRAL, icon_data
        resource group_icons, 2, LANG_NEUTRAL, main_icon
        resource versions, 1, LANG_NEUTRAL, vinfo

        icon main_icon, icon_data, 'BA-1-Icon.ico'

versioninfo vinfo,VOS__WINDOWS32,VFT_APP,VFT2_UNKNOWN,\
        LANG_ENGLISH+SUBLANG_DEFAULT,0,\
        'CompanyName',          'Slattstudio',\
        'ProductName',          'Betrayed Alliance',\
        'OriginalFilename',     'Betrayed Alliance',\
        'FileVersion',          '1.0',\
        'LegalCopyright',       '2024',\
        'LegalTrademarks',      'The Sierra Help Pages',\
        'FileDescription',      'Game DOSBox Launcher',\
        'Publisher',            'Slattstudio',\
        'Website',              'https://slattstudio.com'

end data
