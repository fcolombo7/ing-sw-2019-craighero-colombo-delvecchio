# Prova Finale Ingegneria del Software 2019
## Gruppo AM53

- ###   10523037    Michele Craighero ([@Michelec1997](https://github.com/Michelec1997))<br>michele.craighero@mail.polimi.it
- ###   10570682    Giovanni Del Vecchio ([@giovannidv8](https://github.com/giovannidv8))<br>giovanni.delvecchio@mail.polimi.it
- ###   10559531    Filippo Colombo ([@fcolombo7](https://github.com/fcolombo7))<br>filippo7.colombo@mail.polimi.it

| Functionality | State |
|:-----------------------|:------------------------------------:|
| Basic rules | [![RED](https://placehold.it/15/f03c15/f03c15)](#) |
| Complete rules | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| Socket | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| RMI | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| GUI | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| CLI | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| Multiple games | [![GREEN](https://placehold.it/15/44bb44/44bb44)](#) |
| Persistence | [![RED](https://placehold.it/15/f03c15/f03c15)](#) |
| Domination or Towers modes | [![RED](https://placehold.it/15/f03c15/f03c15)](#) |
| Terminator | [![RED](https://placehold.it/15/f03c15/f03c15)](#) |

<!--
[![RED](https://placehold.it/15/f03c15/f03c15)](#)
[![YELLOW](https://placehold.it/15/ffdd00/ffdd00)](#)
[![GREEN](https://placehold.it/15/44bb44/44bb44)](#)
-->

####HOW TO RUN:
######Server
Open the terminal and run `java -jar AM53-SERVER.jar`. 
<br>In the same directory of the server jar file must be placed the configuration file `config.xml`. <br>Note that if you run the jar server without the configuration file the output will be 
<br>`ERR: CONFIG.XML - ~~/pathToJar/config.xml (No such file or directory)`

######Client
The Client jar works with arguments. So if you want to run it, you must write in terminal `CLI|GUI hostname rmiPort socketPort`.
<br>Remember that: <br>`hostname` must be the server ip address;
<br>`rmiPort` and `socketPort` must be the same used to configure the server;
<br><br>GUI example:
<br>`java -jar AM53-UI.jar GUI 192.168.1.133 12346 12345`
<br><br>CLI example:
<br>`java -jar AM53-UI.jar CLI 192.168.1.133 12346 12345`
<br><br>The examples refer to the configuration of the current `config.xml`